package io.github.winfeo.superpositiongame.ui.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ScreenViewport
import io.github.winfeo.superpositiongame.model.game.GameState
import io.github.winfeo.superpositiongame.model.game.Move
import io.github.winfeo.superpositiongame.config.GameConfig
import io.github.winfeo.superpositiongame.PlayerActionController
import io.github.winfeo.superpositiongame.graphics.BorderTexture
import io.github.winfeo.superpositiongame.graphics.Dialogs
import io.github.winfeo.superpositiongame.manager.CardsDoubleTapManager
import io.github.winfeo.superpositiongame.manager.CardsDragAndDropManager
import io.github.winfeo.superpositiongame.manager.CardsLongPressManager
import io.github.winfeo.superpositiongame.manager.GameAssetsManager
import io.github.winfeo.superpositiongame.manager.SwapSelectionManager
import io.github.winfeo.superpositiongame.ui.actor.card.CardActorBuilder
import io.github.winfeo.superpositiongame.ui.actor.dice.DiceActorBuilder
import io.github.winfeo.superpositiongame.ui.screen.elements.CardsFan
import io.github.winfeo.superpositiongame.ui.screen.elements.GameTable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import ktx.app.KtxScreen

//Игровой экран, MVI паттерн
class GameScreen(
    private val assetsManager: GameAssetsManager,
    private val playerId: String,
    private val dialogs: Dialogs,
    private val onMove: (Move) -> Unit,
    private val getGameState: () -> GameState,
    private val applyPendingState: () -> Unit
) : KtxScreen {
    private val cardActorBuilder = CardActorBuilder(assetsManager = assetsManager)
    private val diceActorBuilder = DiceActorBuilder(assetsManager = assetsManager)
    private val stage = Stage(ScreenViewport())
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var lastRenderedTurnNumber: Int? = null
    private var lastRenderedCurrentPlayerId: String? = null

    private val swapManager: SwapSelectionManager by lazy {
        SwapSelectionManager(
            allSlotsProvider = {
                gameTable.getAllSlots()
            }
        )
    }

    private val doubleTapManager by lazy {
        CardsDoubleTapManager(
            controller = playerActionController,
            onCardConsumed = { card ->
                val gameState = getGameState()
                if (gameState.currentPlayerId == playerId) {
                    cardsFan.consumeCard(card)
                }
            }
        )
    }

    private val playerActionController = PlayerActionController(
        playerId = playerId,
        dialogs = dialogs,
        onMove = onMove,
        getGameState = getGameState,
        swapManager = swapManager
    )
    private val dragManager = CardsDragAndDropManager(playerActionController)
//    private val doubleTapManager = CardsDoubleTapManager(playerActionController)
    private val longPressManager = CardsLongPressManager(dialogs)

    private val cardsFan: CardsFan by lazy {
        CardsFan(
            playerId = playerId,
            stage = stage,
            dragManager = dragManager,
            doubleTapManager = doubleTapManager,
            longPressManager = longPressManager,
            cardActorBuilder = cardActorBuilder
        )
    }

    private val gameTable: GameTable by lazy {
        GameTable(
            playerId = playerId,
            dragManager = dragManager,
            cardActorBuilder = cardActorBuilder,
            diceActorBuilder = diceActorBuilder
        )
    }

    fun renderState(newState: GameState) {
        val turnContextChanged = lastRenderedTurnNumber != null && (newState.turnNumber != lastRenderedTurnNumber || newState.currentPlayerId != lastRenderedCurrentPlayerId)

        if (turnContextChanged || newState.currentPlayerId != playerId || newState.turnEndsAt <= 0L) {
            swapManager.reset()
        }

        gameTable.render(newState)
        cardsFan.render(newState)
        lastRenderedTurnNumber = newState.turnNumber
        lastRenderedCurrentPlayerId = newState.currentPlayerId
    }

    override fun show() {
        super.show()
        GameConfig.init(stage = stage)
        Gdx.input.inputProcessor = stage

        stage.clear()
        stage.addActor(gameTable)

//        stage.isDebugAll = true

    }

    override fun render(delta: Float) {
//        super.render(delta)
        applyPendingState()
//        Gdx.gl.glClearColor(0f,0f,0f,0f)
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        ScreenUtils.clear(0f,0f,0f,0f);

        stage.act(delta)
        stage.draw()
//        Gdx.app.log("CARD", "акторов на сцене = ${stage.actors.size}")
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)

        stage.viewport.update(width, height, true)
    }

    override fun pause() {
        swapManager.reset()
        super.pause()
    }

    override fun dispose() {
        super.dispose()

        BorderTexture.clear()
        stage.dispose()
        dragManager.clear()
        doubleTapManager.clear()
        longPressManager.clear()
        scope.cancel()
    }
}
