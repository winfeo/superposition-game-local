package io.github.winfeo.superpositiongame.android.ui.screen.onboarding

enum class OnboardingStep {
    WELCOME,
    OBJECTIVE,
    PLAY_CARD,
    CARD_RESULT,
    CARD_PREVIEW,
    FULL_TABLE,
    FINISH;

    companion object {
        val total: Int = entries.size
    }
}
