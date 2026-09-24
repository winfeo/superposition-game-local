package io.github.winfeo.superpositiongame.android.data.util

import io.github.winfeo.superpositiongame.android.data.dto.socket.ActiveGameDTO
import io.github.winfeo.superpositiongame.android.data.dto.socket.ActiveGameResponseDTO
import io.github.winfeo.superpositiongame.android.data.dto.socket.GameLifecycleEventDTO
import io.github.winfeo.superpositiongame.android.domain.game.model.ActiveGame
import io.github.winfeo.superpositiongame.android.domain.game.model.GameEndReason
import io.github.winfeo.superpositiongame.android.domain.game.model.GameLifecycleEvent
import io.github.winfeo.superpositiongame.android.domain.game.model.GameSessionStatus

fun ActiveGameResponseDTO.toDomain(): ActiveGame? {
    if (!hasActiveGame) return null
    return game?.toDomain()
}

fun ActiveGameDTO.toDomain(): ActiveGame {
    return ActiveGame(
        gameId = gameId,
        status = status.toGameSessionStatus(),
        opponentId = opponentId,
        opponentNickname = opponentNickname,
        currentPlayerDisconnected = currentPlayerDisconnected,
        reconnectDeadline = reconnectDeadline,
        serverTime = serverTime
    )
}

fun GameLifecycleEventDTO.toDomain(): GameLifecycleEvent {
    return GameLifecycleEvent(
        gameId = gameId,
        status = status.toGameSessionStatus(),
        disconnectedPlayerIds = disconnectedPlayerIds,
        reconnectDeadlines = reconnectDeadlines,
        serverTime = serverTime,
        winnerId = winnerId,
        endReason = endReason?.toGameEndReason()
    )
}

private fun String.toGameSessionStatus(): GameSessionStatus {
    return runCatching { GameSessionStatus.valueOf(this) }
        .getOrDefault(GameSessionStatus.UNKNOWN)
}

private fun String.toGameEndReason(): GameEndReason {
    return runCatching { GameEndReason.valueOf(this) }
        .getOrDefault(GameEndReason.UNKNOWN)
}
