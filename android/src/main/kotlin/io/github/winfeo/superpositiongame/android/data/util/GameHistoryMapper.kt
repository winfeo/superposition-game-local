package io.github.winfeo.superpositiongame.android.data.util

import io.github.winfeo.superpositiongame.android.data.dto.rest.GameHistoryDTO
import io.github.winfeo.superpositiongame.android.domain.history.GameHistoryItem

fun GameHistoryDTO.toDomain(): GameHistoryItem {
    return GameHistoryItem(
        isWinner = isWinner,
        opponentNickname = opponentNickname,
        totalMoves = totalMoves,
        ratingChange = ratingChange,
        playedAt = playedAt
    )
}
