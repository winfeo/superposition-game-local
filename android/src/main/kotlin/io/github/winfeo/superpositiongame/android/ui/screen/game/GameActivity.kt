package io.github.winfeo.superpositiongame.android.ui.screen.game

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import io.github.winfeo.superpositiongame.Main
import io.github.winfeo.superpositiongame.android.data.source.AppModule
import io.github.winfeo.superpositiongame.android.ui.dialog.game.GameDialogState
import io.github.winfeo.superpositiongame.android.ui.dialog.game.GameDialogs
import io.github.winfeo.superpositiongame.android.ui.dialog.game.CardPreviewDialog
import io.github.winfeo.superpositiongame.android.ui.dialog.game.GameFinishedDialog
import io.github.winfeo.superpositiongame.android.ui.dialog.game.GameMenuDialog
import io.github.winfeo.superpositiongame.android.ui.dialog.game.OpponentDisconnectedDialog
import io.github.winfeo.superpositiongame.android.ui.dialog.game.ReshuffleCardDialog
import io.github.winfeo.superpositiongame.android.ui.dialog.game.RotateCardDialog
import io.github.winfeo.superpositiongame.android.ui.dialog.game.RulesDialog
import io.github.winfeo.superpositiongame.android.ui.theme.SuperpositionGameTheme
import io.github.winfeo.superpositiongame.android.ui.theme.elements.BackgroundBlur
import io.github.winfeo.superpositiongame.android.util.GameMusicPlayer
import io.github.winfeo.superpositiongame.model.game.GamePhase
import kotlinx.coroutines.launch

class GameActivity: AppCompatActivity(), AndroidFragmentApplication.Callbacks {
    private lateinit var viewModel: GameViewModel

    private val gameMusicPlayer by lazy {
        GameMusicPlayer(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() = Unit
            }
        )

        processMusic()

        val gameId = intent.getStringExtra("GAME_ID")
            ?: throw Resources.NotFoundException("Отладка. Игра не передана")
        val playerId = intent.getStringExtra("USER_ID")
            ?: throw Resources.NotFoundException("Отладка. Не передан id игрока")

        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return GameViewModel(
                    gameRepository = AppModule.gameRepository,
                    pingRepository = AppModule.pingRepository,
                    playerId = playerId,
                    gameId = gameId
                ) as T
            }
        }

        viewModel = ViewModelProvider(this, viewModelFactory)[GameViewModel::class.java]

        val dialogs = GameDialogs(viewModel)
        val game = Main(
            playerId = playerId,
            dialogs = dialogs,
            onMove = { viewModel.sendMove(it) },
            getGameState = { viewModel.gameState.value!! }
        )

        setContent {
            SuperpositionGameTheme {
                val gameState by viewModel.gameState.collectAsState()
                val dialogState by viewModel.dialogState.collectAsState()
                val timerSeconds by viewModel.timerSeconds.collectAsState()

                LaunchedEffect(gameState) {
                    Log.d("GAME", "LaunchedEffect triggered ${gameState.hashCode()}")
//                    val state = gameState?: return@LaunchedEffect
//
////                    Gdx.app.postRunnable {
////                        game.updateState(state)
////                    }
//                    game.applyNewState(state)

                    gameState?.let { state ->
                        game.applyNewState(state)
//                        viewModel.startTimer()
                    }
                }

                LaunchedEffect(gameState?.phase, gameState?.winnerId) {
                    val state = gameState ?: return@LaunchedEffect
                    if (state.phase == GamePhase.GAME_FINISHED) {
                        viewModel.showGameFinishedDialog(
                            isWinner = state.winnerId == playerId,
                            onReturnToLobby = { exit() }
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF0C0813))
//                        .background(Color.Red)
                ) {
                    BackgroundBlur()

                    Column(modifier = Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            if (gameState != null) {
                                PlayerInfoPanel(
                                    gameState = gameState!!,
                                    playerId = playerId,
                                    timerSeconds = timerSeconds,
                                    onPause = { viewModel.showGameMenuDialog(
                                        onResume = { viewModel.dismissDialog() },
                                        onRules = { viewModel.showRulesDialog() },
                                        onSettings = {},
                                        onSurrender = { viewModel.surrender() },
                                        onDismiss = { viewModel.dismissDialog() }
                                    ) }
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(2f)
                        ) {
                            AndroidView(
                                modifier = Modifier.fillMaxSize(),
                                factory = { context ->
                                    val fragmentContainer = FragmentContainerView(context).apply {
                                        id = View.generateViewId()
                                    }

                                    val fragment = GameFragment().apply {
                                        this.game = game
                                    }

                                    (context as AppCompatActivity).supportFragmentManager
                                        .beginTransaction()
                                        .replace(fragmentContainer.id, fragment)
                                        .commit()

                                    fragmentContainer
                                }
                            )
                        }
                    }

                    dialogState?.let { dialog ->
                        when (dialog) {
                            is GameDialogState.RotateDialog -> {
                                RotateCardDialog(
                                    availableStates = dialog.availableStates,
                                    onStateSelected = { selected ->
                                        dialog.onStateSelected(selected)
                                        viewModel.dismissDialog()
                                    }
                                )
                            }
                            is GameDialogState.ReshuffleDialog -> {
                                ReshuffleCardDialog(
                                    cards = dialog.cards,
                                    maxSelectable = dialog.maxSelectable,
                                    minSelectable = dialog.minSelectable,
                                    onCardsSelected = { selectedCards ->
                                        dialog.onCardsSelected(selectedCards)
                                        viewModel.dismissDialog()
                                    }
                                )
                            }
                            is GameDialogState.CardPreviewDialog -> {
                                CardPreviewDialog(
                                    card = dialog.card,
                                    onDismiss = { viewModel.dismissDialog() }
                                )
                            }
                            is GameDialogState.GameFinishedDialog -> {
                                GameFinishedDialog(
                                    isWinner = dialog.isWinner,
                                    onReturnToLobby = dialog.onReturnToLobby
                                )
                            }

                            is GameDialogState.GameMenuDialog -> {
                                GameMenuDialog(
                                    onResume = dialog.onResume,
                                    onRules = dialog.onRules,
                                    onSettings = dialog.onSettings,
                                    onSurrender = dialog.onSurrender,
                                    onDismiss = dialog.onDismiss
                                )
                            }

                            is GameDialogState.RulesDialog -> {
                                RulesDialog(
                                    onDismiss = { viewModel.dismissDialog() }
                                )
                            }

                            is GameDialogState.OpponentDisconnectedDialog -> {
                                OpponentDisconnectedDialog(
                                    opponentNickname = dialog.opponentNickname,
                                    reconnectDeadline = dialog.reconnectDeadline,
                                    serverTime = dialog.serverTime
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun processMusic() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                AppModule.settingsManager.isMusicEnabled.collect { isEnabled ->
                    if (isEnabled) gameMusicPlayer.play()
                    else gameMusicPlayer.pause()
                }
            }
        }
    }

    override fun exit() {
        finish()
    }

    override fun onStart() {
        super.onStart()
        if (::viewModel.isInitialized) {
            viewModel.onGameVisible()
        }
    }

    override fun onStop() {
        if (::viewModel.isInitialized) {
            viewModel.onGameHidden()
        }
        gameMusicPlayer.pause()
        super.onStop()
    }

    override fun onDestroy() {
        gameMusicPlayer.release()
        super.onDestroy()
    }
}
