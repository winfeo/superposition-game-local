package io.github.winfeo.superpositiongame.android.ui.screen.onboarding

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OnboardingViewModel : ViewModel() {
    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    fun nextStep() {
        val current = _state.value
        if (current.currentStep == OnboardingStep.PLAY_CARD) return
        if (current.currentStep == OnboardingStep.CARD_PREVIEW && !current.wasCardPreviewOpened) return

        if (current.currentStep == OnboardingStep.FULL_TABLE && !current.tableFocus.isLast) {
            _state.value = current.copy(tableFocus = OnboardingTableFocus.entries[current.tableFocus.ordinal + 1])
            return
        }

        val nextIndex = (current.currentStep.ordinal + 1).coerceAtMost(OnboardingStep.entries.lastIndex)
        _state.value = current.copy(currentStep = OnboardingStep.entries[nextIndex])
    }

    fun previousStep() {
        val current = _state.value

        if (current.currentStep == OnboardingStep.FULL_TABLE && current.tableFocus.ordinal > 0) {
            _state.value = current.copy(tableFocus = OnboardingTableFocus.entries[current.tableFocus.ordinal - 1])
            return
        }

        val previousIndex = (current.currentStep.ordinal - 1).coerceAtLeast(0)
        _state.value = current.copy(
            currentStep = OnboardingStep.entries[previousIndex],
            isCardPreviewVisible = false
        )
    }

    fun applyHadamard() {
        _state.value = _state.value.copy(
            currentStep = OnboardingStep.CARD_RESULT
        )
    }

    fun showCardPreview() {
        _state.value = _state.value.copy(
            isCardPreviewVisible = true,
            wasCardPreviewOpened = true
        )
    }

    fun dismissCardPreview() {
        _state.value = _state.value.copy(isCardPreviewVisible = false)
    }

    fun showSkipDialog() {
        _state.value = _state.value.copy(isSkipDialogVisible = true)
    }

    fun dismissSkipDialog() {
        _state.value = _state.value.copy(isSkipDialogVisible = false)
    }
}
