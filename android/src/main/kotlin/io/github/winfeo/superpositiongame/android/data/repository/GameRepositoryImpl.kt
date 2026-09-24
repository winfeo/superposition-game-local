package io.github.winfeo.superpositiongame.android.data.repository

import android.util.Log
import io.github.winfeo.superpositiongame.android.data.dto.move.DoubleTapEffectDTO
import io.github.winfeo.superpositiongame.android.data.dto.move.MoveCommandDTO
import io.github.winfeo.superpositiongame.android.data.dto.move.MoveDTO
import io.github.winfeo.superpositiongame.android.data.dto.move.PlayCardDTO
import io.github.winfeo.superpositiongame.android.data.dto.move.ReshuffleCardDTO
import io.github.winfeo.superpositiongame.android.data.dto.move.RotateDiceDTO
import io.github.winfeo.superpositiongame.android.data.dto.move.SurrenderDTO
import io.github.winfeo.superpositiongame.android.data.dto.move.SwapDicesDTO
import io.github.winfeo.superpositiongame.android.data.dto.socket.ActiveGameResponseDTO
import io.github.winfeo.superpositiongame.android.data.dto.socket.GameLifecycleEventDTO
import io.github.winfeo.superpositiongame.android.data.dto.socket.TimerUpdatePacketDTO
import io.github.winfeo.superpositiongame.android.data.dto.state.GameStateDTO
import io.github.winfeo.superpositiongame.android.data.source.socket.Network
import io.github.winfeo.superpositiongame.android.data.util.toDomain
import io.github.winfeo.superpositiongame.android.data.util.toDto
import io.github.winfeo.superpositiongame.android.domain.game.GameRepository
import io.github.winfeo.superpositiongame.android.domain.game.model.ActiveGame
import io.github.winfeo.superpositiongame.android.domain.game.model.GameLifecycleEvent
import io.github.winfeo.superpositiongame.android.domain.game.model.TimerUpdatePacket
import io.github.winfeo.superpositiongame.android.ui.screen.game.GameStartEvent
import io.github.winfeo.superpositiongame.model.game.GameState
import io.github.winfeo.superpositiongame.model.game.Move
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

class GameRepositoryImpl: GameRepository {
    private companion object {
        const val SUBSCRIPTION_READY_DELAY_MS = 300L
        const val GAME_START_TOPIC = "/user/queue/game.start"
        const val ACTIVE_GAME_TOPIC = "/user/queue/game.active"
        const val ACTIVE_GAME_DESTINATION = "/app/game/active"
    }

    private val json = Json {
        ignoreUnknownKeys = true
        classDiscriminator = "type"
        serializersModule = SerializersModule {
            polymorphic(MoveDTO::class) {
                subclass(PlayCardDTO::class)
                subclass(RotateDiceDTO::class)
                subclass(SwapDicesDTO::class)
                subclass(DoubleTapEffectDTO::class)
                subclass(ReshuffleCardDTO::class)
                subclass(SurrenderDTO::class)
            }
        }
    }
//    val topic = "/user/queue/game"
//    val sendTopic = "/app/game/"
    val gameStart = "/user/queue/game.start"

    override suspend fun sendMove(
        gameId: String,
        move: Move
    ) {
        val topic = "/app/game/$gameId/move"

        val dto = move.toDto()
        val command = MoveCommandDTO(
            expectedTurnNumber = move.expectedTurnNumber,
            move = dto
        )
        val payload = json.encodeToString(
            MoveCommandDTO.serializer(),
            command
        )
        Log.d("GAME_SEND", payload)
        Network.sendMessage(
            destination = topic,
            message = payload
        )
    }

    override fun observeGameState(gameId: String, playerId: String): Flow<GameState> {
        return callbackFlow {
            val topic = "/user/queue/game/$gameId"
            Log.d("GAME_SET", "Работа метода")
            Log.d("GAME_SOCKET", "SUBSCRIBE: $topic, GAME: $gameId")
            Network.subscribeToTopic(topic) { message ->
                try {
                    Log.d("GAME_SOCKET", "MESSAGE: $topic, GAME: $gameId")
                    val stateDto = json.decodeFromString<GameStateDTO>(message)
                    Log.d("GAME_STATE", """
                            Получено состояние:
                            ${stateDto.phase}
                            ${stateDto.currentPlayerId}
                            ${stateDto.turnNumber}
                            players:
                                ${stateDto.players.entries.joinToString("\n") { (id, player) ->
                        "Player ${id.take(5)} | hand=${player.hand.size} | slots=${player.slots.size}" +
                            "DiceState=${player.slots.joinToString { it.initialDice.state }}" +
                            "SlotOwner=${player.slots.joinToString { it.ownerId }}"
                    }}
                    """.trimIndent())
                    val gameState = stateDto.toDomain(playerId)
                    trySend(gameState)
                } catch (e: Exception) {
                    Log.d("GAME_STATE", "Ошибка получения состояния: ${e.message}")
                }
            }

            awaitClose {
                Log.d("GAME_SOCKET", "UNSUBSCRIBE: $topic, GAME: $gameId")
                Network.unsubscribeToTopic(topic)
            }
        }
    }

    override fun observeGameLifecycle(gameId: String): Flow<GameLifecycleEvent> {
        return callbackFlow {
            val topic = "/user/queue/game/$gameId/lifecycle"

            Network.subscribeToTopic(topic) { message ->
                try {
                    val event = json.decodeFromString<GameLifecycleEventDTO>(message)
                    trySend(event.toDomain())
                } catch (e: Exception) {
                    Log.e("GAME_LIFECYCLE", "Ошибка получения lifecycle-события", e)
                }
            }

            awaitClose {
                Network.unsubscribeToTopic(topic)
            }
        }
    }

    override fun observeActiveGame(): Flow<ActiveGame?> {
        return callbackFlow {
            Network.subscribeToTopic(ACTIVE_GAME_TOPIC) { message ->
                try {
                    val response = json.decodeFromString<ActiveGameResponseDTO>(message)
                    trySend(response.toDomain())
                } catch (e: Exception) {
                    Log.e("ACTIVE_GAME", "Ошибка получения активного матча", e)
                }
            }

            val requestJob = launch {
                Network.connectionState
                    .filter { it }
                    .collectLatest {
                        delay(SUBSCRIPTION_READY_DELAY_MS)
                        requestActiveGame()
                    }
            }

            awaitClose {
                requestJob.cancel()
                Network.unsubscribeToTopic(ACTIVE_GAME_TOPIC)
            }
        }
    }

    override fun requestActiveGame() {
        Network.sendMessage(
            destination = ACTIVE_GAME_DESTINATION,
            message = ""
        )
    }

    override fun observeConnectionState(): Flow<Boolean> {
        return Network.connectionState
    }

    override fun sendHeartbeat(gameId: String) {
        Network.sendMessage(
            destination = "/app/game/$gameId/heartbeat",
            message = ""
        )
    }

    override fun reconnectToGame(gameId: String) {
        Network.sendMessage(
            destination = "/app/game/$gameId/reconnect",
            message = ""
        )
    }

    override fun markGameInactive(gameId: String) {
        Network.sendMessage(
            destination = "/app/game/$gameId/inactive",
            message = ""
        )
    }

    override fun declineReconnect(gameId: String) {
        Network.sendMessage(
            destination = "/app/game/$gameId/reconnect.decline",
            message = ""
        )
    }

    override fun observeGameStart(): Flow<String> {
        return callbackFlow {
            Network.subscribeToTopic(GAME_START_TOPIC) { message ->
                try {
                    val event = json.decodeFromString<GameStartEvent>(message)
                    trySend(event.gameId)
                    Log.d("GAME_START", "Получен gameId: ${event.gameId}")
                } catch (e: Exception) {
                    Log.d("GAME_START", "Ошибка получения gameId: ${e.message}")
                }
            }

            awaitClose {
                Network.unsubscribeToTopic(GAME_START_TOPIC)
            }
        }
    }

    override fun observeTimerUpdates(gameId: String): Flow<TimerUpdatePacket> {
        return callbackFlow {
            val topic = "/user/queue/game/$gameId/timer"

            Network.subscribeToTopic(topic) { message ->
                try {
                    val dto = json.decodeFromString<TimerUpdatePacketDTO>(message)
                    val packet = dto.toDomain()
                    trySend(packet)
                    Log.d(
                        "GAME_TIMER",
                        "Пакет таймера получен. " +
                            "Ход: ${packet.turnNumber}, " +
                            "Версия: ${packet.revision}, " +
                            "Осталось времени: ${packet.timeLeftMs}"
                    )
                } catch (e: Exception) {
                    Log.d("GAME_TIMER", "Ошибка: ${e.message}")
                }
            }

            awaitClose {
                Network.unsubscribeToTopic(topic)
            }
        }
    }
}
