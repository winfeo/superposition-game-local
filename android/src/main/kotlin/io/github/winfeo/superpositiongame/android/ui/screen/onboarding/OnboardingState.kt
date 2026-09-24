package io.github.winfeo.superpositiongame.android.ui.screen.onboarding

data class OnboardingState(
    val currentStep: OnboardingStep = OnboardingStep.WELCOME,
    val isCardPreviewVisible: Boolean = false,
    val wasCardPreviewOpened: Boolean = false,
    val tableFocus: OnboardingTableFocus = OnboardingTableFocus.OVERVIEW,
    val isSkipDialogVisible: Boolean = false
)
