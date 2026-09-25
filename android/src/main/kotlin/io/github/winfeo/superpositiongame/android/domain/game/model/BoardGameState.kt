package io.github.winfeo.superpositiongame.android.domain.game.model

data class BoardGameState(
    val playerIds: List<Int>,
    val registers: List<List<String>>,
    val targetRegister: List<String>,
    val currentPlayer: Int,
    val playedCards: Int,
    val cardsOnField: List<BoardFieldCard>
) {
    val currentPlayerId: Int? get() = playerIds.getOrNull(currentPlayer)
}
