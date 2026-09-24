package io.github.winfeo.superpositiongame.android

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.winfeo.superpositiongame.android.data.source.socket.Network
import io.github.winfeo.superpositiongame.android.data.source.AppModule
import io.github.winfeo.superpositiongame.android.data.source.local.NotificationManager
import io.github.winfeo.superpositiongame.android.data.source.local.UserSession
import io.github.winfeo.superpositiongame.android.data.util.toDomain
import io.github.winfeo.superpositiongame.android.ui.nav.Navigation
import io.github.winfeo.superpositiongame.android.ui.screen.onboarding.OnboardingScreen
import io.github.winfeo.superpositiongame.android.ui.screen.onboarding.OnboardingViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("RestrictedApi")
class AndroidLauncher : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppModule.init(applicationContext)
        NotificationManager.init(
            context = applicationContext,
            repository = AppModule.invitationRepository,
            settingsManager = AppModule.settingsManager
        )

        val savedToken = AppModule.tokenManager.getToken()
        if (savedToken != null) {
            UserSession.restoreToken(savedToken)
            lifecycleScope.launch {
                val result = AppModule.userRepository.getCurrentUser()
                result.fold(
                    onSuccess = { userDto ->
                        val user = userDto.toDomain()
                        UserSession.restoreSession(user, savedToken)
                        UserSession.setUserId(user.id.toString())
                        Network.connect(userId = user.id.toString())
                    },
                    onFailure = {
                        UserSession.logout()
                        startGuestMode()
                    }
                )
            }
        } else {
            startGuestMode()
        }

        setContent {
            val userIdState by UserSession.currentUserId.collectAsState()
            val isOnboardingCompleted by AppModule.settingsManager.isOnboardingCompleted.collectAsState()
            val currentUserId = userIdState

            if (!isOnboardingCompleted) {
                val onboardingViewModel: OnboardingViewModel = viewModel(key = "first-launch-onboarding")

                OnboardingScreen(
                    viewModel = onboardingViewModel,
                    onFinished = ::completeFirstLaunchOnboarding,
                    onSkipped = ::completeFirstLaunchOnboarding
                )
            } else if (currentUserId == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        color = Color(0xFF6C8CFF)
                    )
                }
            } else {
                Navigation()
            }
        }
    }

    private fun startGuestMode() {
        lifecycleScope.launch {
            val savedGuestId = AppModule.guestSessionManager.getGuestId()
            if (savedGuestId != null) {
                connectAsGuest(savedGuestId)
                Log.i("AndroidLauncher", "Гостевая сессия восстановлена: $savedGuestId")
                return@launch
            }

            while (true) {
                val guestIdResult = AppModule.guestRepository.createGuest()
                if (guestIdResult.isSuccess) {
                    val guestId = guestIdResult.getOrNull()!!
                    connectAsGuest(guestId)
                    Log.i("AndroidLauncher", "Гостевой ID получен: $guestId")
                    break
                } else {
                    Log.e("AndroidLauncher", "Не удалось получить гостевой ID")
                    delay(3000)
                }
            }
        }
    }

    private fun connectAsGuest(guestId: String) {
        UserSession.setUserId(guestId)
        Network.connect(userId = guestId)
    }

    private fun completeFirstLaunchOnboarding() {
        AppModule.settingsManager.setOnboardingCompleted(true)
        UserSession.currentUserId.value?.let { userId ->
            Network.connect(userId = userId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Network.disconnect()
    }
}
