package io.github.winfeo.superpositiongame.android.ui.nav

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.winfeo.superpositiongame.android.data.source.AppModule
import io.github.winfeo.superpositiongame.android.data.source.local.UserSession
import io.github.winfeo.superpositiongame.android.ui.nav.route.AiRoute
import io.github.winfeo.superpositiongame.android.ui.nav.route.AuthRoute
import io.github.winfeo.superpositiongame.android.ui.nav.route.GameHistory
import io.github.winfeo.superpositiongame.android.ui.nav.route.InvitesRoute
import io.github.winfeo.superpositiongame.android.ui.nav.route.LibraryRoute
import io.github.winfeo.superpositiongame.android.ui.nav.route.LobbyRoute
import io.github.winfeo.superpositiongame.android.ui.nav.route.OnboardingRoute
import io.github.winfeo.superpositiongame.android.ui.nav.route.ProfileRoute
import io.github.winfeo.superpositiongame.android.ui.nav.route.SettingsRoute
import io.github.winfeo.superpositiongame.android.ui.dialog.game.ReconnectGameDialog
import io.github.winfeo.superpositiongame.android.ui.screen.ai.AiGameScreen
import io.github.winfeo.superpositiongame.android.ui.screen.ai.AiGameViewModel
import io.github.winfeo.superpositiongame.android.ui.screen.game.GameActivity
import io.github.winfeo.superpositiongame.android.ui.screen.invites.InvitesScreen
import io.github.winfeo.superpositiongame.android.ui.screen.invites.InvitationViewModel
import io.github.winfeo.superpositiongame.android.ui.screen.library.LibraryScreen
import io.github.winfeo.superpositiongame.android.ui.screen.library.LibraryViewModel
import io.github.winfeo.superpositiongame.android.ui.screen.lobby.LobbyScreen
import io.github.winfeo.superpositiongame.android.ui.screen.lobby.LobbyViewModel
import io.github.winfeo.superpositiongame.android.ui.screen.onboarding.OnboardingScreen
import io.github.winfeo.superpositiongame.android.ui.screen.onboarding.OnboardingViewModel
import io.github.winfeo.superpositiongame.android.ui.screen.auth.AuthScreen
import io.github.winfeo.superpositiongame.android.ui.screen.auth.AuthViewModel
import io.github.winfeo.superpositiongame.android.ui.screen.game.GameLauncher
import io.github.winfeo.superpositiongame.android.ui.screen.history.GameHistoryScreen
import io.github.winfeo.superpositiongame.android.ui.screen.history.GameHistoryViewModel
import io.github.winfeo.superpositiongame.android.ui.screen.profile.ProfileScreen
import io.github.winfeo.superpositiongame.android.ui.screen.profile.ProfileViewModel
import io.github.winfeo.superpositiongame.android.ui.screen.settings.SettingsScreen
import io.github.winfeo.superpositiongame.android.ui.screen.settings.SettingsViewModel
import kotlinx.coroutines.flow.collect

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val currentUserId by UserSession.currentUserId.collectAsState()


    /* --------------- ViewModel-и --------------- */
    ///TODO временно потом DI
    val lobbyViewModel: LobbyViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LobbyViewModel(AppModule.lobbyRepository) as T
            }
        }
    )

    ///TODO временно потом DI
    val invitationViewModel: InvitationViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return InvitationViewModel(AppModule.invitationRepository) as T
            }
        }
    )

    ///TODO временно потом DI
    val libraryViewModel: LibraryViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LibraryViewModel(AppModule.cardsRepository) as T
            }
        }
    )

    val aiGameViewModel: AiGameViewModel = viewModel(
        key = "ai-game-$currentUserId",
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AiGameViewModel(
                    aiGameRepository = AppModule.aiGameRepository,
                    gameRepository = AppModule.gameRepository,
                    settingsManager = AppModule.settingsManager
                ) as T
            }
        }
    )

    ///TODO временно потом DI
    val authViewModel: AuthViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(AppModule.authRepository) as T
            }
        }
    )

    ///TODO временно потом DI
    val profileViewModel: ProfileViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProfileViewModel(AppModule.profileRepository) as T
            }
        }
    )

    ///TODO временно потом DI
    val gameHistoryViewModel: GameHistoryViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return GameHistoryViewModel(AppModule.gameHistoryRepository) as T
            }
        }
    )

    val settingsViewModel: SettingsViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SettingsViewModel(AppModule.settingsManager, AppModule.accountRepository) as T
            }
        }
    )


    /* --------------- Запуск игры --------------- */
    val context = LocalContext.current
    val viewModel: GameLauncher = viewModel(
        key = currentUserId,
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return GameLauncher(
                    playerId = requireNotNull(currentUserId),
                    repository = AppModule.gameRepository
                ) as T
            }
        }
    )

    val gameId by viewModel.gameFlow.collectAsState()
    val activeGame by viewModel.activeGame.collectAsState()
    val isReconnectInProgress by viewModel.isReconnectInProgress.collectAsState()
    LaunchedEffect(aiGameViewModel, viewModel) {
        aiGameViewModel.gameCreated.collect { createdGameId ->
            viewModel.onAiGameCreated(createdGameId)
        }
    }

    val gameActivityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        viewModel.onGameActivityClosed()
    }

    LaunchedEffect(gameId) {
        if (gameId != null && currentUserId != null) {
            gameActivityLauncher.launch(
                Intent(context, GameActivity::class.java)
                    .putExtra("GAME_ID", gameId)
                    .putExtra("USER_ID", currentUserId)
            )
            viewModel.resetGameId()
        }
    }


    /* --------------- Навигация --------------- */
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = LobbyRoute,
            modifier = Modifier.fillMaxSize()
        ) {
            composable<LobbyRoute> {
                LobbyScreen(
                    viewModel = lobbyViewModel,
                    onInvitesClick = {
                        navController.navigate(InvitesRoute)
                    }
                )
            }

            composable<AiRoute> {
                AiGameScreen(viewModel = aiGameViewModel)
            }

            composable<InvitesRoute> {
                InvitesScreen(
                    viewModel = invitationViewModel,
                    onReturnToLobby = {
                        navController.popBackStack()
                    }
                )
            }

            composable<LibraryRoute> {
                LibraryScreen(
                    viewModel = libraryViewModel
                )
            }

            composable<ProfileRoute> {
                ProfileScreen(
                    viewModel = profileViewModel,
                    onNavigateToAuth = {
                        navController.navigate(AuthRoute)
                    },
                    onNavigateToGameHistory = {
                        navController.navigate(GameHistory)
                    },
                    onNavigateToSettings = {
                        navController.navigate(SettingsRoute)
                    }
                )
            }

            composable<AuthRoute> {
                LaunchedEffect(Unit) {
                    authViewModel.resetForm()
                }

                AuthScreen(
                    viewModel = authViewModel,
                    onSuccess = {
                        navController.popBackStack()
                    }
                )
            }

            composable<GameHistory> {
                GameHistoryScreen(
                    viewModel = gameHistoryViewModel,
                    onReturnToProfile = {
                        navController.popBackStack()
                    }
                )
            }

            composable<SettingsRoute> {
                SettingsScreen(
                    viewModel = settingsViewModel,
                    onBack = { navController.popBackStack() },
                    onReplayOnboarding = {
                        navController.navigate(OnboardingRoute)
                    }
                )
            }

            composable<OnboardingRoute> {
                val onboardingViewModel: OnboardingViewModel = viewModel()

                OnboardingScreen(
                    viewModel = onboardingViewModel,
                    onFinished = { navController.popBackStack() },
                    onSkipped = { navController.popBackStack() }
                )
            }
        }

        val showBottomBar = currentRoute in listOf(
            LobbyRoute::class.qualifiedName,
            AiRoute::class.qualifiedName,
            LibraryRoute::class.qualifiedName,
            ProfileRoute::class.qualifiedName
        )

        if (showBottomBar) {
            BottomNavBar(
                navController = navController,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        activeGame?.let { game ->
            ReconnectGameDialog(
                opponentNickname = game.opponentNickname,
                reconnectDeadline = game.reconnectDeadline,
                serverTime = game.serverTime,
                isReconnecting = isReconnectInProgress,
                onReconnect = viewModel::reconnectToGame,
                onDecline = viewModel::declineReconnect,
                onExpired = viewModel::onReconnectExpired
            )
        }
    }
}
