package io.github.winfeo.superpositiongame.android.ui.screen.settings

data class SettingsState(
    val isAuthorized: Boolean = false,
    val isMusicEnabled: Boolean = true,
    val isInviteSoundEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
)
