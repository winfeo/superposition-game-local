package io.github.winfeo.superpositiongame.android.domain.library

import kotlinx.coroutines.flow.Flow

interface CardsRepository {
    fun getAllCards(): Flow<List<Card>>
}
