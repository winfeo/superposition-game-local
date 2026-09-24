package io.github.winfeo.superpositiongame.android.domain.ai.model

class AiGameException(
    val error: AiGameError,
    cause: Throwable? = null
) : RuntimeException(error.name, cause)
