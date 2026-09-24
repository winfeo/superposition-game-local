package io.github.winfeo.superpositiongame.android.ui.screen.onboarding

enum class OnboardingTableFocus {
    OVERVIEW,
    PLAYERS_AND_TIMER,
    OBJECTIVE,
    OPPONENT_SLOTS,
    PLAYER_SLOTS,
    HAND;

    val isLast: Boolean
        get() = this == entries.last()
}
