package io.github.winfeo.superpositiongame.ui.screen.elements

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import io.github.winfeo.superpositiongame.graphics.GameSkinFactory

class TurnLabel(
    private val playerId: String
): Table() {
    private val turnLabel: Label

    init {
        val labelStyle = GameSkinFactory.createTimerLabelSkin()
        turnLabel = Label("", labelStyle)
        turnLabel.setColor(Color.WHITE)

        add(turnLabel).pad(30f)
    }

    fun render(currentPlayerId: String) {
        val text = if (currentPlayerId == playerId) {
            "YOUR TURN"
        } else {
            "OPPONENT'S TURN"
        }
        turnLabel.setText(text)
    }
}
