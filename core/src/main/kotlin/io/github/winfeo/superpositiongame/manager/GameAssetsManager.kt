package io.github.winfeo.superpositiongame.manager

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion

class GameAssetsManager {
    private val ASSETS_CARDS_PATH = "cards/cards.atlas"
    private val ASSETS_DICE_PATH = "dice/dice.atlas"

    lateinit var cardsAtlas: TextureAtlas
        private set
    lateinit var diceAtlas: TextureAtlas
        private set
    private val cardsIds = mutableListOf<String>()

    fun load() {
        loadCards()
        loadDice()
    }

    private fun loadCards() {
        cardsAtlas = TextureAtlas(Gdx.files.internal(ASSETS_CARDS_PATH))
        cardsIds.clear()
        cardsAtlas.regions.forEach { region ->
            cardsIds.add(region.name)
        }
    }
    private fun loadDice() {
        diceAtlas = TextureAtlas(Gdx.files.internal(ASSETS_DICE_PATH))
    }

    fun getCardTexture(name: String): TextureRegion {
        return cardsAtlas.findRegion(name)?: throw IllegalStateException("Отладка. Текстура не найдена")
    }
    fun getDiceTexture(name: String): TextureRegion {
        return diceAtlas.findRegion(name)?: throw IllegalStateException("Отладка. Текстура не найдена")
    }

    fun dispose() {
        if (::cardsAtlas.isInitialized) {
            cardsAtlas.dispose()
        }
        if (::diceAtlas.isInitialized) {
            diceAtlas.dispose()
        }
        cardsIds.clear()
    }

}
