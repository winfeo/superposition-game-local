package io.github.winfeo.superpositiongame.android.data.util

import io.github.winfeo.superpositiongame.android.data.dto.move.DoubleTapEffectDTO
import io.github.winfeo.superpositiongame.android.data.dto.move.MoveDTO
import io.github.winfeo.superpositiongame.android.data.dto.move.PlayCardDTO
import io.github.winfeo.superpositiongame.android.data.dto.move.ReshuffleCardDTO
import io.github.winfeo.superpositiongame.android.data.dto.move.RotateDiceDTO
import io.github.winfeo.superpositiongame.android.data.dto.move.SurrenderDTO
import io.github.winfeo.superpositiongame.android.data.dto.move.SwapDicesDTO
import io.github.winfeo.superpositiongame.model.game.Move
import io.github.winfeo.superpositiongame.model.game.Move.*

fun Move.toDto(): MoveDTO {
    return when (this) {
        is PlayCard -> PlayCardDTO(
            playerId = playerId,
            cardId = cardId,
            targetSlotIndex = targetSlotIndex,
            targetPlayerId = targetPlayerId
        )
        is RotateDice -> RotateDiceDTO(
            playerId = playerId,
            cardId = cardId,
            targetSlotIndex = targetSlotIndex,
            newState = newState.name,
            targetPlayerId = targetPlayerId
        )
        is SwapDices -> SwapDicesDTO(
            playerId = playerId,
            cardId = cardId,
            firstSlotIndex = firstSlotIndex,
            secondSlotIndex = secondSlotIndex,
            firstSlotOwner = firstSlotOwner,
            secondSlotOwner = secondSlotOwner
        )
        is DoubleTapEffect -> DoubleTapEffectDTO(
            playerId = playerId,
            cardId = cardId
        )

        is ReshuffleCard -> ReshuffleCardDTO(
            playerId = playerId,
            cardId = cardId,
            cardsToChange = cardsToChange
        )

        is Surrender -> SurrenderDTO(
            playerId = playerId
        )
    }
}

