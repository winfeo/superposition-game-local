package io.github.winfeo.superpositiongame.android.data.source

object NetworkConfig {
//    private const val SERVER_ADDRESS = "10.0.2.2:8080"
    private const val SERVER_ADDRESS = "91.237.249.20:8080"

    const val REST_BASE_URL = "http://$SERVER_ADDRESS"
    const val STOMP_URL = "ws://$SERVER_ADDRESS/ws-android"
}
