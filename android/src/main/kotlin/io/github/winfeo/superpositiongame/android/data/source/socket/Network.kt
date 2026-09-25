package io.github.winfeo.superpositiongame.android.data.source.socket

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network as AndroidNetwork
import android.net.NetworkCapabilities
import android.os.SystemClock
import android.util.Log
import io.github.winfeo.superpositiongame.android.data.util.toGameState
import io.github.winfeo.superpositiongame.android.data.util.toIntList
import io.github.winfeo.superpositiongame.android.data.util.toPlayer
import io.github.winfeo.superpositiongame.android.data.util.toPlayers
import io.github.winfeo.superpositiongame.android.domain.game.model.BoardPlayer
import io.github.winfeo.superpositiongame.android.domain.game.model.BoardSession
import io.github.winfeo.superpositiongame.android.domain.game.model.BoardTarget
import io.github.winfeo.superpositiongame.android.domain.game.model.ConnectionStatus
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

private const val BOARD_URL = "ws://192.168.4.1:81"
private const val CONNECT_TIMEOUT_MS = 5_000L
private const val CUBIT_COUNT = 4

//Реалзация веб-сокетов для общения с платой
class Network(
    context: Context
) {
    private val connectivity = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val _state = MutableStateFlow(BoardSession())
    val state: StateFlow<BoardSession> = _state.asStateFlow()

    private lateinit var scope: CoroutineScope
    private var attemptStartedAt = 0L
    private var boardNetwork: AndroidNetwork? = null
    private var client: OkHttpClient? = null
    private var socket: WebSocket? = null
    private var generation = 0

    fun start(coroutineScope: CoroutineScope) {
        scope = coroutineScope

        retry()
        scope.launch {
            while (isActive) {
                try {
                    driveConnection()
                } catch (error: Exception) {
                    if (error is CancellationException) throw error
                    Log.e("BoardNetwork", "Ошибка при подключении к плате", error)
                    runCatching { disconnectSocket() }
                    _state.value = BoardSession(connection = ConnectionStatus.ERROR)
                }
                delay(500L)
            }
        }
    }

    fun retry() {
        disconnectSocket()
        boardNetwork = null
        attemptStartedAt = SystemClock.elapsedRealtime()
        _state.value = BoardSession(connection = ConnectionStatus.CONNECTING)
    }

    fun startGame(opponentId: Int): Boolean {
        val current = state.value
        val selfId = current.selfId?: return false

        if (current.connection != ConnectionStatus.CONNECTED || current.gameStarted ||
            opponentId == selfId || current.players.none { it.id == opponentId }
        ) return false

        val data = JSONObject()
            .put("players", JSONArray().put(selfId).put(opponentId))
            .put("cubitCount", CUBIT_COUNT)
        return send("startGame", data)
    }

    fun selectTarget(
        playerIndex: Int,
        cubitIndex: Int
    ): Boolean {
        val current = state.value
        val game = current.game?: return false

        if (current.connection != ConnectionStatus.CONNECTED ||
            game.currentPlayerId != current.selfId ||
            playerIndex !in game.registers.indices ||
            cubitIndex !in game.registers[playerIndex].indices
        ) return false

        val data = JSONObject()
            .put("player", playerIndex)
            .put("cubit", cubitIndex)
        val send = send("selectTarget", data)
        if (send) _state.update { it.copy(selectedTarget = BoardTarget(playerIndex, cubitIndex)) }
        return send
    }

    fun giveUp(): Boolean {
        return state.value.gameStarted && state.value.connection == ConnectionStatus.CONNECTED && send("giveUp", JSONObject.NULL)
    }

    fun returnToLobby() {
        _state.update {
            it.copy(
                gameStarted = false,
                game = null,
                selectedTarget = null,
                winner = null,
                opponentDisconnected = false
            )
        }
    }

    private fun driveConnection() {
        if (state.value.connection == ConnectionStatus.CONNECTED) {
            val currentNetwork = boardNetwork
            if (currentNetwork == null || connectivity.allNetworks.none { it == currentNetwork }) retry()
            return
        }

        if (state.value.connection == ConnectionStatus.ERROR) return

        if (SystemClock.elapsedRealtime() - attemptStartedAt >= CONNECT_TIMEOUT_MS) {
            disconnectSocket()
            _state.value = BoardSession(connection = ConnectionStatus.ERROR)
            return
        }

        if (socket != null) return
        val wifiNetwork = connectivity.allNetworks.firstOrNull { candidate ->
            connectivity.getNetworkCapabilities(candidate)?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
        }?: return

        openSocket(wifiNetwork)
    }

    private fun openSocket(network: AndroidNetwork) {
        disconnectSocket()

        boardNetwork = network
        val attempt = ++generation

        client = OkHttpClient.Builder()
            .socketFactory(network.socketFactory)
            .connectTimeout(3, TimeUnit.SECONDS)
            .pingInterval(15, TimeUnit.SECONDS)
            .build()

        socket = client!!.newWebSocket(Request.Builder().url(BOARD_URL).build(),
            object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) = Unit

                override fun onMessage(webSocket: WebSocket, text: String) {
                    scope.launch {
                        if (attempt != generation) return@launch

                        try {
                            handleMessage(JSONObject(text))
                        } catch (error: Exception) {
                            Log.w("BoardNetwork", "Некорректное сообщение от платы", error)
                        }
                    }
                }

                override fun onFailure(webSocket: WebSocket, error: Throwable, response: Response?) {
                    scope.launch {
                        if (attempt != generation) return@launch

                        Log.w("BoardNetwork", "Board socket упал с ошибкой", error)
                        val wasConnected = state.value.connection == ConnectionStatus.CONNECTED
                        disconnectSocket()
                        if (wasConnected) retry()
                    }
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    scope.launch {
                        if (attempt != generation) return@launch

                        val wasConnected = state.value.connection == ConnectionStatus.CONNECTED
                        disconnectSocket()
                        if (wasConnected) retry()
                    }
                }
            })
    }

    private fun handleMessage(message: JSONObject) {
        val data = message.opt("data")

        when (message.optString("event")) {
            "setId" -> if (data is Number) {
                _state.update { it.copy(selfId = data.toInt(), connection = ConnectionStatus.CONNECTED) }
            }
            "setOnlineUsers" -> if (data is JSONArray) {
                _state.update { it.copy(players = data.toPlayers()) }
            }
            "addOnlineUser" -> if (data is JSONObject) {
                val player = data.toPlayer() ?: return
                _state.update { current ->
                    current.copy(players = (current.players.filterNot { it.id == player.id } + player)
                        .sortedBy { it.id })
                }
            }
            "removeOnlineUser" -> if (data is Number) {
                val removed = data.toInt()
                _state.update { current ->
                    current.copy(
                        players = current.players.filterNot { it.id == removed },
                        opponentDisconnected = current.opponentDisconnected ||
                            (current.gameStarted && removed != current.selfId &&
                                current.game?.playerIds?.contains(removed) == true)
                    )
                }
            }
            "gameStarted" -> if (data is JSONObject) {
                val ids = data.optJSONArray("players")?.toIntList() ?: return
                if (state.value.selfId?.let(ids::contains) == true) {
                    _state.update { it.copy(gameStarted = true, game = null, winner = null,
                        selectedTarget = null, opponentDisconnected = false) }
                }
            }
            "setGameState" -> if (data is JSONObject) {
                val snapshot = data.toGameState() ?: return
                if (state.value.selfId?.let(snapshot.playerIds::contains) == true) {
                    _state.update { current ->
                        current.copy(gameStarted = true, game = snapshot,
                            selectedTarget = if (current.game?.currentPlayer != snapshot.currentPlayer ||
                                current.game?.playedCards != snapshot.playedCards
                            ) null else current.selectedTarget)
                    }
                }
            }
            "gameEnded" -> if (data is JSONObject) {
                val winnerId = data.optInt("winnerId", -1)
                if (winnerId >= 0) {
                    _state.update { it.copy(gameStarted = false, selectedTarget = null,
                        winner = BoardPlayer(winnerId, data.optString("winnerName", "player$winnerId"))) }
                }
            }
        }
    }

    private fun send(event: String, data: Any): Boolean {
        if (state.value.connection != ConnectionStatus.CONNECTED) return false
        return socket?.send(JSONObject().put("event", event).put("data", data).toString()) == true
    }

    private fun disconnectSocket() {
        generation++
        socket?.cancel()
        socket = null
        client?.dispatcher?.executorService?.shutdown()
        client?.connectionPool?.evictAll()
        client = null
    }
}
