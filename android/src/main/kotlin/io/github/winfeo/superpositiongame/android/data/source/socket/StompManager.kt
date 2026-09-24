package io.github.winfeo.superpositiongame.android.data.source.socket

import android.util.Log
import io.github.winfeo.superpositiongame.android.data.source.socket.StompSubscription
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

object StompManager {
    private val desiredSubscriptions = ConcurrentHashMap<String, (String) -> Unit>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            StompConnection.isConnected.collect { connected ->
                if (connected) {
                    StompSubscription.clear()
                    desiredSubscriptions.forEach { (topic, callback) ->
                        subscribeToConnectedClient(topic, callback)
                    }
                } else {
                    StompSubscription.clear()
                }
            }
        }
    }
    fun connect(userId: String) {
        StompConnection.connect(userId)
    }

    fun subscribe(
        topic: String,
        onMessage: (String) -> Unit
    ) {
        val previousCallback = desiredSubscriptions.put(topic, onMessage)

        if (StompConnection.isConnected.value) {
            if (previousCallback != null) {
                StompSubscription.unsubscribe(topic)
            }
            subscribeToConnectedClient(topic, onMessage)
        }
    }

    fun unsubscribe(topic: String) {
        desiredSubscriptions.remove(topic)
        StompSubscription.unsubscribe(topic)
    }

    fun send(
        destination: String,
        message: String
    ) {
        if (!StompConnection.isConnected.value) return

        StompConnection.client.send(destination, message)
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    Log.d("STOMP", "Сообщение отправлено")
                },
                { error ->
                    Log.d("STOMP", "Ошибка отправки на $destination: ${error.message}")
                }
            )
    }

    fun disconnect() {
        desiredSubscriptions.clear()
        StompConnection.disconnect()
        StompSubscription.clear()
    }

    private fun subscribeToConnectedClient(
        topic: String,
        onMessage: (String) -> Unit
    ) {
        StompSubscription.subscribe(
            topic = topic,
            client = StompConnection.client,
            onMessage = onMessage
        )
    }
}
