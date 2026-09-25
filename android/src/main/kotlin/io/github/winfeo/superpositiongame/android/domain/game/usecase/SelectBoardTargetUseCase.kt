package io.github.winfeo.superpositiongame.android.domain.game.usecase

import io.github.winfeo.superpositiongame.android.domain.game.GameRepository

class SelectBoardTargetUseCase(
    private val repository: GameRepository
) {
    operator fun invoke(playerIndex: Int, cubitIndex: Int): Boolean = repository.selectTarget(playerIndex, cubitIndex)
}
