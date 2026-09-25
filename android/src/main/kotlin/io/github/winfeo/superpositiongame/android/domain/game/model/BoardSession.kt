package io.github.winfeo.superpositiongame.android.domain.game.model

data class BoardSession(
    val connection: ConnectionStatus = ConnectionStatus.CONNECTING,
    val selfId: Int? = null,
    val players: List<BoardPlayer> = emptyList(),
    val gameStarted: Boolean = false,
    val game: BoardGameState? = null,
    val selectedTarget: BoardTarget? = null,
    val winner: BoardPlayer? = null,
    val opponentDisconnected: Boolean = false
)
