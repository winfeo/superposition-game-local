package io.github.winfeo.superpositiongame

import com.badlogic.gdx.Gdx
import io.github.winfeo.superpositiongame.graphics.Dialogs
import io.github.winfeo.superpositiongame.manager.GameAssetsManager
import io.github.winfeo.superpositiongame.model.game.GameState
import io.github.winfeo.superpositiongame.model.game.Move
import io.github.winfeo.superpositiongame.ui.screen.GameScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync

class Main(
    private val playerId: String,
    private val dialogs: Dialogs,
    private val onMove: (Move) -> Unit,
    private val getGameState: () -> GameState
): KtxGame<KtxScreen>() {
    @Volatile
    private var pendingState: GameState? = null

    private var gameScreen: GameScreen? = null

    lateinit var assets: GameAssetsManager
        private set

    override fun create() {
        KtxAsync.initiate()

        assets = GameAssetsManager()
        assets.load()

        val screen = GameScreen(
            assetsManager = assets,
            playerId = playerId,
            dialogs = dialogs,
            onMove = onMove,
            getGameState = getGameState,
            applyPendingState = { updateState() }
        )
        gameScreen = screen
        addScreen(screen)
        setScreen<GameScreen>()
    }

    fun applyNewState(state: GameState) {
        Gdx.app.log("GAME_STATE_APPLY", "Применение обновлённого состояния\n" +
            "Фаза: ${state.phase}\n" +
            "Ход: ${state.turnNumber}\n" +
            "Карты в руке игрока: ${state.players[playerId]?.hand?.joinToString(", ")?: "пусто"}")
        pendingState = state
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
    }

//    fun updateState(state: GameState) {
//        val gameScreen = getScreen<GameScreen>()
//        gameScreen.renderState(state)
//    }

    fun updateState() {
        pendingState?.let { state ->
            gameScreen?.renderState(state)
            pendingState = null
        }
    }

    override fun dispose() {
        super.dispose()
        assets.dispose()
    }

}
