package io.github.winfeo.superpositiongame.manager

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import io.github.winfeo.superpositiongame.model.dice.DiceState

//Управляет ассетами кубика
object DiceAtlasManager {
    private lateinit var diceAtlas: TextureAtlas
    private const val ASSETS_DICE_PATH = "dice/dice.atlas"

    fun loadAtlas() {
        diceAtlas = TextureAtlas(Gdx.files.internal(ASSETS_DICE_PATH))
    }

    fun getRandomDiceState(): DiceState = DiceState.entries.random()

    fun getStateTexture(stateId: String): TextureRegion {
        return diceAtlas.findRegion(stateId)?: throw IllegalStateException("Отладка. Текстура не найдена")
    }

    fun dispose() {
        diceAtlas.dispose()
    }
}
