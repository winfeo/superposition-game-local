package io.github.winfeo.superpositiongame.android.ui.screen.profile

import io.github.winfeo.superpositiongame.android.domain.auth.model.AuthorizedUser
import io.github.winfeo.superpositiongame.android.domain.history.GameHistoryItem

data class ProfileState(
    val isAuthorized: Boolean = false,
    val user: AuthorizedUser? = null,
    val isLoadingHistory: Boolean = false,
    val gameHistory: List<GameHistoryItem> = emptyList(),
    val historyError: String? = null
)
