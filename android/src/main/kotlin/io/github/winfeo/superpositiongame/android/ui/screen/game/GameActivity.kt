package io.github.winfeo.superpositiongame.android.ui.screen.game

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import io.github.winfeo.superpositiongame.Main
import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.android.data.source.AppModule
import io.github.winfeo.superpositiongame.android.data.util.BoardGameStateMapper
import io.github.winfeo.superpositiongame.android.domain.game.model.ConnectionStatus
import io.github.winfeo.superpositiongame.android.ui.dialog.BoardConfirmationDialog
import io.github.winfeo.superpositiongame.android.ui.dialog.BoardMessageDialog
import io.github.winfeo.superpositiongame.android.ui.dialog.game.CardPreviewDialog
import io.github.winfeo.superpositiongame.android.ui.dialog.game.GameDialogState
import io.github.winfeo.superpositiongame.android.ui.dialog.game.GameDialogs
import io.github.winfeo.superpositiongame.android.ui.dialog.game.ReshuffleCardDialog
import io.github.winfeo.superpositiongame.android.ui.dialog.game.RotateCardDialog
import io.github.winfeo.superpositiongame.android.ui.theme.SuperpositionGameTheme
import io.github.winfeo.superpositiongame.android.ui.theme.elements.BackgroundBlur
import io.github.winfeo.superpositiongame.model.game.SlotOwner

class GameActivity: AppCompatActivity(), AndroidFragmentApplication.Callbacks {
    private lateinit var viewModel: GameViewModel
    private val mapper = BoardGameStateMapper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppModule.init(applicationContext)
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() = Unit
            }
        )

        viewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return GameViewModel(AppModule.gameRepository) as T
                }
            }
        )[GameViewModel::class.java]

        val selfId = viewModel.session.value.selfId
        if (selfId == null) {
            finish()
            return
        }

        val game = Main(
            playerId = selfId.toString(),
            dialogs = GameDialogs(viewModel),
            onMove = {
                runOnUiThread {
                    Toast.makeText(this, R.string.game_nfc_only, Toast.LENGTH_LONG).show()
                }
            },
            getGameState = {
                checkNotNull(mapper.map(viewModel.session.value)) {
                    getString(R.string.game_state_unavailable)
                }
            },
            onSlotSelected = { owner, cubit ->
                val snapshot = viewModel.session.value.game
                val player = snapshot?.playerIds?.indexOfFirst { id ->
                    if (owner == SlotOwner.PLAYER) id == selfId else id != selfId
                }?: -1

                if (player >= 0) viewModel.selectTarget(player, cubit)
            }
        )

        setContent {
            SuperpositionGameTheme(darkTheme = true) {
                val session by viewModel.session.collectAsState()
                val dialogState by viewModel.dialogState.collectAsState()
                val mapped = remember(session) { mapper.map(session) }
                var confirmGiveUp by remember { mutableStateOf(false) }
                var showGameMenu by remember { mutableStateOf(false) }
                val colors = MaterialTheme.colors
                val muted = colorResource(R.color.board_text_muted)
                val space16 = dimensionResource(R.dimen.space_16)

                LaunchedEffect(mapped) {
                    mapped?.let(game::applyNewState)
                }

                Box(Modifier.fillMaxSize().background(colors.background)) {
                    BackgroundBlur()
                    Column(Modifier.fillMaxSize()) {
                        val currentGame = session.game
                        if (currentGame == null) {
                            Text(
                                text = stringResource(R.string.game_waiting_board_state),
                                color = colors.onBackground,
                                modifier = Modifier.padding(horizontal = space16)
                            )
                        } else {
                            PlayerInfoPanel(
                                session = session,
                                onMenuClick = { showGameMenu = true }
                            )
                        }
                        val selectedTarget = session.selectedTarget
                        Box(
                            modifier = Modifier.fillMaxWidth()
                                .height(dimensionResource(R.dimen.game_instruction_height)),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = when {
                                    currentGame == null -> stringResource(R.string.game_waiting_start)
                                    session.game?.currentPlayerId != selfId -> stringResource(R.string.game_wait_your_turn)
                                    selectedTarget != null -> stringResource(
                                        R.string.game_target_selected,
                                        selectedTarget.cubit + 1
                                    )
                                    else -> stringResource(R.string.game_select_target)
                                },
                                color = muted,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(horizontal = space16)
                            )
                        }

                        if (session.game != null && mapped == null) {
                            Text(
                                text = stringResource(R.string.game_state_display_error),
                                color = colors.error,
                                modifier = Modifier.padding(space16)
                            )
                        }

                        Box(Modifier.fillMaxWidth().weight(1f)) {
                            AndroidView(
                                modifier = Modifier.fillMaxSize(),
                                factory = { context ->
                                    FragmentContainerView(context).apply {
                                        id = View.generateViewId()
                                        val fragment = GameFragment().also { it.game = game }
                                        supportFragmentManager.beginTransaction()
                                            .replace(id, fragment)
                                            .commit()
                                    }
                                }
                            )
                        }
                    }
                }

                when {
                    session.connection == ConnectionStatus.ERROR -> {
                        BoardMessageDialog(
                            title = stringResource(R.string.network_error_title),
                            message = stringResource(R.string.game_connection_lost),
                            buttonText = stringResource(R.string.action_back_to_lobby),
                            onConfirm = {
                                viewModel.returnToLobby()
                                finish()
                            }
                        )
                    }
                    session.winner != null -> {
                        val winner = checkNotNull(session.winner)
                        BoardMessageDialog(
                            title = if (winner.id == selfId) {
                                stringResource(R.string.game_victory_title)
                            } else {
                                stringResource(R.string.game_ended_title)
                            },
                            message = stringResource(R.string.game_winner, winner.name),
                            buttonText = stringResource(R.string.action_back_to_lobby),
                            onConfirm = {
                                viewModel.returnToLobby()
                                finish()
                            }
                        )
                    }
                    showGameMenu -> {
                        BoardConfirmationDialog(
                            title = stringResource(R.string.game_menu_title),
                            message = stringResource(R.string.game_menu_message),
                            confirmText = stringResource(R.string.action_give_up),
                            cancelText = stringResource(R.string.action_continue),
                            onConfirm = {
                                showGameMenu = false
                                confirmGiveUp = true
                            },
                            onDismiss = { showGameMenu = false }
                        )
                    }
                    confirmGiveUp -> {
                        BoardConfirmationDialog(
                            title = stringResource(R.string.game_give_up_title),
                            message = stringResource(R.string.game_give_up_message),
                            confirmText = stringResource(R.string.action_give_up),
                            cancelText = stringResource(R.string.action_continue),
                            onConfirm = {
                                viewModel.giveUp()
                                confirmGiveUp = false
                            },
                            onDismiss = { confirmGiveUp = false }
                        )
                    }
                    else -> when (val dialog = dialogState) {
                        is GameDialogState.Rotate -> RotateCardDialog(
                            availableStates = dialog.availableStates,
                            onStateSelected = { selected ->
                                dialog.onStateSelected(selected)
                                viewModel.dismissDialog()
                            }
                        )
                        is GameDialogState.Reshuffle -> ReshuffleCardDialog(
                            cards = dialog.cards,
                            minSelectable = dialog.minSelectable,
                            maxSelectable = dialog.maxSelectable,
                            onCardsSelected = { selected ->
                                dialog.onCardsSelected(selected)
                                viewModel.dismissDialog()
                            }
                        )
                        is GameDialogState.CardPreview -> CardPreviewDialog(
                            card = dialog.card,
                            onDismiss = viewModel::dismissDialog
                        )
                        null -> Unit
                    }
                }
            }
        }
    }

    override fun exit() = finish()
}
