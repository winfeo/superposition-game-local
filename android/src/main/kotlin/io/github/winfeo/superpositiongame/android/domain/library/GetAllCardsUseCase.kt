package io.github.winfeo.superpositiongame.android.domain.library

import kotlinx.coroutines.flow.Flow

class GetAllCardsUseCase(
    private val repository: CardsRepository
) {
    operator fun invoke(): Flow<List<Card>> {
        return repository.getAllCards()
    }
}
