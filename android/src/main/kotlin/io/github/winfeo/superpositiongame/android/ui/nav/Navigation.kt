package io.github.winfeo.superpositiongame.android.ui.nav

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.winfeo.superpositiongame.android.data.source.AppModule
import io.github.winfeo.superpositiongame.android.ui.nav.route.GameRoute
import io.github.winfeo.superpositiongame.android.ui.nav.route.LobbyRoute
import io.github.winfeo.superpositiongame.android.ui.screen.game.GameActivity
import io.github.winfeo.superpositiongame.android.ui.screen.game.GameViewModel
import io.github.winfeo.superpositiongame.android.ui.screen.lobby.LobbyScreen
import io.github.winfeo.superpositiongame.android.ui.screen.lobby.LobbyViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current


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
    val gameViewModel: GameViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return GameViewModel(AppModule.gameRepository) as T
            }
        }
    )

    val session by gameViewModel.session.collectAsState()
    val gameActivityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        if (navController.currentDestination?.route == GameRoute::class.qualifiedName) {
            navController.popBackStack()
        }
    }

    LaunchedEffect(
        session.gameStarted,
        session.winner,
        currentRoute
    ) {
        if (session.gameStarted && currentRoute == LobbyRoute::class.qualifiedName) {
            navController.navigate(GameRoute) { launchSingleTop = true }
        }
        else if (!session.gameStarted && session.winner == null && currentRoute == GameRoute::class.qualifiedName) {
            navController.popBackStack()
        }
    }

    NavHost(
        navController = navController,
        startDestination = LobbyRoute
    ) {
        composable<LobbyRoute> { LobbyScreen(lobbyViewModel) }
        composable<GameRoute> {
            LaunchedEffect(Unit) {
                gameActivityLauncher.launch(Intent(context, GameActivity::class.java))
            }
        }
    }
}
