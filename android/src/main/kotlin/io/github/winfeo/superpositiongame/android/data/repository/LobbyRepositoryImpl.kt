package io.github.winfeo.superpositiongame.android.data.repository

import android.util.Log
import io.github.winfeo.superpositiongame.android.data.dto.socket.InvitationDTO
import io.github.winfeo.superpositiongame.android.data.dto.socket.LobbyResponseDTO
import io.github.winfeo.superpositiongame.android.data.source.socket.Network
import io.github.winfeo.superpositiongame.android.data.util.toDomain
import io.github.winfeo.superpositiongame.android.domain.lobby.LobbyRepository
import io.github.winfeo.superpositiongame.android.domain.lobby.model.Player
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class LobbyRepositoryImpl: LobbyRepository {
    private val json = Json { ignoreUnknownKeys = true }
    private val topic = "/topic/lobby"
    private val initialData = "/app/lobby"
    private val sendInvite = "/app/invite"

    override fun observePlayersInLobby(currentUserId: String): Flow<List<Player>> {
        return callbackFlow {
            val connectionJob = launch {
                Network.connectionState.collect { isConnected ->
                    if (isConnected) {
                        Log.d("STOMP", "Подключение успешно")
                        Network.subscribeToTopic(topic) { message ->
                            handleMessage(
                                message = message,
                                userId = currentUserId
                            )
                        }

                        launch {
                            delay(500)
                            Network.sendMessage(initialData, "")
                        }
                    }
                }
            }

            awaitClose {
                connectionJob.cancel()
                Network.unsubscribeToTopic(topic)
                Network.unsubscribeToTopic(initialData)
            }
        }
    }

    private fun ProducerScope<List<Player>>.handleMessage(
        message: String,
        userId: String
    ) {
        try {
            val dto = json.decodeFromString<LobbyResponseDTO>(message)
            Log.d("STOMP", "Данные из ДТО: ${dto.players.joinToString { it.id }}"
            )
            val lobby = dto.toDomain()
            val users = lobby.players.filter { it.id != userId }
            trySend(users)
        } catch (e: Exception) {
            Log.d("LOBBY", "Ошибка парсинга: ${e.message}")
        }
    }

    override suspend fun sendInvitation(
        senderId: String,
        senderNickname: String?,
        receiverId: String
    ) {
        val dto = InvitationDTO(
            senderId = senderId,
            senderNickname = senderNickname,
            receiverId = receiverId,
            sendTime = null
        )
        Log.d("INVITE","senderId = $senderId, receiverId = $receiverId")
        val jsonString = json.encodeToString(InvitationDTO.serializer(), dto)

        Network.sendMessage(
            destination = sendInvite,
            message = jsonString
        )
    }

}
