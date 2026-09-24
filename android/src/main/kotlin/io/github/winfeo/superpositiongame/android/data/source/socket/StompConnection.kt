package io.github.winfeo.superpositiongame.android.data.source.socket

import android.util.Log
import io.github.winfeo.superpositiongame.android.data.source.NetworkConfig
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent

object StompConnection {
    private const val STOMP_URL = NetworkConfig.STOMP_URL

    private const val RECONNECT_DELAY_MS = 2_000L

    lateinit var client: StompClient
        private set

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    private var lifecycleDisposable: Disposable? = null
    private val connectionScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var reconnectJob: Job? = null
    @Volatile
    private var currentUserId: String? = null
    @Volatile
    private var manuallyDisconnected = false

    @Synchronized
    fun connect(
        userId: String
    ) {
        manuallyDisconnected = false
        currentUserId = userId
        reconnectJob?.cancel()
        reconnectJob = null
        openConnection(userId)
    }

    @Synchronized
    private fun openConnection(userId: String) {
        lifecycleDisposable?.dispose()

        if (::client.isInitialized) {
            runCatching { client.disconnect() }
        }

        val url = "$STOMP_URL?userId=$userId"
        Log.d("STOMP", "Подключение к: $url")
        client = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)

        lifecycleDisposable = client.lifecycle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { event ->
                when (event.type) {
                    LifecycleEvent.Type.OPENED -> {
                        _isConnected.value = true
                        Log.d("STOMP", "STOMP соединение открыто")
                    }
                    LifecycleEvent.Type.CLOSED -> {
                        _isConnected.value = false
                        Log.d("STOMP", "STOMP соединение закрыто")
                        scheduleReconnect()
                    }
                    LifecycleEvent.Type.ERROR -> {
                        _isConnected.value = false
                        Log.d("STOMP", "STOMP ошибка соединения: ${event.exception}")
                        scheduleReconnect()
                    }
                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> {
                        _isConnected.value = false
                        Log.d("STOMP", "Серверный heartbeat не получен")
                        scheduleReconnect()
                    }
                }
            }

//        val headers = listOf(StompHeader("userId", userId)) ///TODO не работают кастомные заголовки?
//        client.connect(headers)
        client.connect()
    }

    @Synchronized
    fun disconnect() {
        manuallyDisconnected = true
        currentUserId = null
        reconnectJob?.cancel()
        reconnectJob = null
        _isConnected.value = false
        lifecycleDisposable?.dispose()
        lifecycleDisposable = null

        if (::client.isInitialized) {
            runCatching { client.disconnect() }
        }
    }

    @Synchronized
    private fun scheduleReconnect() {
        val userId = currentUserId?: return
        if (manuallyDisconnected || reconnectJob?.isActive == true) return

        reconnectJob = connectionScope.launch {
            delay(RECONNECT_DELAY_MS)

            if (manuallyDisconnected || currentUserId != userId || _isConnected.value) return@launch
            withContext(Dispatchers.Main.immediate) { performReconnect(userId) }
        }
    }

    @Synchronized
    private fun performReconnect(userId: String) {
        reconnectJob = null
        if (!manuallyDisconnected && currentUserId == userId) openConnection(userId)
    }
}
