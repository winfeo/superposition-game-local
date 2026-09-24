package io.github.winfeo.superpositiongame.android.ui.screen.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateToAuth: () -> Unit = {},
    onNavigateToGameHistory: () -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

    when {
        state.isLoadingHistory && state.user == null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF6C8CFF))
            }
        }
        state.isAuthorized && state.user != null -> {
            AuthorizedProfileScreen(
                viewModel = viewModel,
                onNavigateToGameHistory = onNavigateToGameHistory,
                onNavigateToSettings = onNavigateToSettings
            )
        }
        else -> {
            UnauthorizedProfileScreen(
                onLoginClick = onNavigateToAuth,
                onNavigateToSettings = onNavigateToSettings
            )
        }
    }
}
