package io.github.winfeo.superpositiongame.android.domain.game.usecase

import io.github.winfeo.superpositiongame.android.domain.game.GameRepository
import io.github.winfeo.superpositiongame.model.game.Move

class SendMoveUseCase(
    private val gameRepository: GameRepository
) {
    suspend operator fun invoke(gameId: String, move: Move) {
        gameRepository.sendMove(gameId, move)
    }
}
