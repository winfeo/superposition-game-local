package io.github.winfeo.superpositiongame.android.data.dto.move

import kotlinx.serialization.Serializable

@Serializable
sealed class MoveDTO{
    abstract val playerId: String
}
