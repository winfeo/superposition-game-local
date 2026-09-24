package io.github.winfeo.superpositiongame.android.data.source.local

import android.content.Context
import androidx.core.content.edit
import io.github.winfeo.superpositiongame.android.domain.ai.model.AiDifficulty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsManager(context: Context) {
    companion object {
        private const val KEY_MUSIC = "music_enabled"
        private const val KEY_INVITE_SOUND = "invite_sound_enabled"
        private const val KEY_AI_DIFFICULTY = "ai_difficulty"
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    }

    private val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    private val _isMusicEnabled = MutableStateFlow(prefs.getBoolean(KEY_MUSIC, true))
    val isMusicEnabled: StateFlow<Boolean> = _isMusicEnabled

    private val _isInviteSoundEnabled = MutableStateFlow(prefs.getBoolean(KEY_INVITE_SOUND, true))
    val isInviteSoundEnabled: StateFlow<Boolean> = _isInviteSoundEnabled

    private val savedAiDifficulty = prefs.getString(KEY_AI_DIFFICULTY, null)
    private val _aiDifficulty = MutableStateFlow(
        AiDifficulty.values().firstOrNull { it.name == savedAiDifficulty } ?: AiDifficulty.LOW
    )
    val aiDifficulty: StateFlow<AiDifficulty> = _aiDifficulty

    private val _isOnboardingCompleted = MutableStateFlow(
        prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    )
    val isOnboardingCompleted: StateFlow<Boolean> = _isOnboardingCompleted

    fun setMusicEnabled(enabled: Boolean) {
        prefs.edit { putBoolean(KEY_MUSIC, enabled) }
        _isMusicEnabled.value = enabled
    }

    fun setInviteSoundEnabled(enabled: Boolean) {
        prefs.edit { putBoolean(KEY_INVITE_SOUND, enabled) }
        _isInviteSoundEnabled.value = enabled
    }

    fun setAiDifficulty(difficulty: AiDifficulty) {
        prefs.edit { putString(KEY_AI_DIFFICULTY, difficulty.name) }
        _aiDifficulty.value = difficulty
    }

    fun setOnboardingCompleted(completed: Boolean) {
        prefs.edit { putBoolean(KEY_ONBOARDING_COMPLETED, completed) }
        _isOnboardingCompleted.value = completed
    }
}
