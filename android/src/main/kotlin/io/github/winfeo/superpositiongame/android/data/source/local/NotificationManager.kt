package io.github.winfeo.superpositiongame.android.data.source.local

import android.content.Context
import android.media.SoundPool
import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.android.data.repository.InvitationRepositoryImpl
import io.github.winfeo.superpositiongame.android.domain.invitations.model.InvitationEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlin.math.max

object NotificationManager {
    private val _badgeCount = MutableStateFlow(0)
    val badgeCount: StateFlow<Int> = _badgeCount

    private var soundPool: SoundPool? = null
    private var soundId: Int = 0
    private var job: Job? = null

    private var settingsManager: SettingsManager? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    fun init(
        context: Context,
        repository: InvitationRepositoryImpl,
        settingsManager: SettingsManager
    ) {
        this.settingsManager = settingsManager
        soundPool = SoundPool.Builder().setMaxStreams(1).build()
        soundId = soundPool?.load(context, R.raw.notification_sound, 1)?: 0

        job?.cancel()
        job = CoroutineScope(Dispatchers.IO).launch {
            launch {
                UserSession.currentUserId
                    .filterNotNull()
                    .flatMapLatest { userId -> repository.observeInvitations(userId) }
                    .collect { invites -> _badgeCount.value = invites.size }
            }

            launch {
                UserSession.currentUserId
                    .filterNotNull()
                    .flatMapLatest { repository.invitationEvents }
                    .filterIsInstance<InvitationEvent.New>()
                    .collect { playSound() }
            }
        }
    }

    private fun playSound() {
        if (settingsManager?.isInviteSoundEnabled?.value == true) {
            soundPool?.play(soundId, 1f, 1f, 1, 0, 1f)
        }
    }

    fun clear() {
        job?.cancel()
        soundPool?.release()
    }
}
