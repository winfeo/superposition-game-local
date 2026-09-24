package io.github.winfeo.superpositiongame.android.data.util

import io.github.winfeo.superpositiongame.android.data.dto.rest.AuthResponseDTO
import io.github.winfeo.superpositiongame.android.data.dto.rest.AuthorizedUserDTO
import io.github.winfeo.superpositiongame.android.domain.auth.model.AuthToken
import io.github.winfeo.superpositiongame.android.domain.auth.model.AuthorizedUser


fun AuthResponseDTO.toDomain(): Pair<AuthorizedUser, AuthToken> {
    val user = user.toDomain()
    val token = AuthToken(
        accessToken = token,
    )
    return user to token
}

fun AuthorizedUserDTO.toDomain(): AuthorizedUser {
    return AuthorizedUser(
        id = id,
        email = email,
        league = league,
        nickname = nickname,
        ratingPoints = ratingPoints,
        winsAmount = winsAmount,
        gamesPlayed = gamesPlayed,
        createdAt = createdAt
    )
}
