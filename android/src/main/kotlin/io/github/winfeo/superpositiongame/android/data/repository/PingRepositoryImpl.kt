package io.github.winfeo.superpositiongame.android.data.repository

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import io.github.winfeo.superpositiongame.android.data.source.socket.Network
import io.github.winfeo.superpositiongame.android.domain.game.PingRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.json.Json
import kotlin.coroutines.resume

class PingRepositoryImpl: PingRepository {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun measureRTT(): Long {
        Log.d("PING", "isConnected = ${Network.connectionState.value}")
        return suspendCancellableCoroutine { cont ->
            var sendTime = 0L
            val topic = "/user/queue/pong"

            Network.subscribeToTopic(topic) {
                val receiveTime = SystemClock.elapsedRealtime()
                val rtt = receiveTime - sendTime
                Log.d("PING", "RTT измерен: ${rtt}ms")

                Network.unsubscribeToTopic(topic)
                if (cont.isActive) cont.resume(rtt)
            }

            ///TODO пределать потом может быть?
            Handler(Looper.getMainLooper()).postDelayed({
                sendTime = SystemClock.elapsedRealtime()
                Network.sendMessage(
                    destination = "/app/ping",
                    message = ""
                )
            }, 300)

            cont.invokeOnCancellation {
                Network.unsubscribeToTopic(topic)
            }
        }
    }
}
