package io.github.winfeo.superpositiongame.ui.screen.elements

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Label
import io.github.winfeo.superpositiongame.config.GameConfig
import io.github.winfeo.superpositiongame.model.game.GamePhase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//Таймер хода игрока
class GameTimer(
    private val label: Label,
    private val scope: CoroutineScope
) {
    private val duration: Int = GameConfig.getTimerDuration()
//    private val progressBar = ProgressBar(0f, 30f,1f,false,skin)
    private var job: Job? = null

    fun start(
        state: StateFlow<GamePhase>,
        timeOut: () -> Unit
    ) {
        finish()
        val title = if (state.value == GamePhase.MOVE_START) "Your move" else "Opponents move"
        job = scope.launch {
            var leftTime = duration
            while (leftTime > 0) {
                Gdx.app.postRunnable {
                    label.setText("$title\n$leftTime  ")
                }
                delay(1000)
                leftTime--
            }
            timeOut()
        }
    }

    fun finish() {
        job?.cancel()
        job = null
    }
}
