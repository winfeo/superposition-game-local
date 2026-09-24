package io.github.winfeo.superpositiongame.android.data.source.socket

import io.github.winfeo.superpositiongame.android.data.source.socket.StompConnection
import io.github.winfeo.superpositiongame.android.data.source.socket.StompManager
import kotlinx.coroutines.flow.StateFlow

object Network {
    val connectionState: StateFlow<Boolean>
        get() = StompConnection.isConnected

    fun connect(userId: String) {
        StompManager.connect(userId = userId)
    }

    fun subscribeToTopic(
        topic: String,
        onMessage: (String) -> Unit
    ) {
        StompManager.subscribe(
            topic = topic,
            onMessage = onMessage
        )
    }

    fun unsubscribeToTopic(topic: String) {
        StompManager.unsubscribe(topic = topic)
    }

    fun sendMessage(
        destination: String,
        message: String
    ) {
        StompManager.send(
            destination = destination,
            message = message
        )
    }

    fun disconnect() {
        StompManager.disconnect()
    }
}
