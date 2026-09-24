package io.github.winfeo.superpositiongame.manager

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion

//Управляет ассетами карт
object CardsAtlasManager {
    private lateinit var cardsAtlas: TextureAtlas
    private val cardsId: MutableSet<String> = mutableSetOf()
    private const val ASSETS_CARDS_PATH = "cards/cards.atlas"

    fun loadAtlas(){
        cardsAtlas = TextureAtlas(Gdx.files.internal(ASSETS_CARDS_PATH))
        cardsAtlas.regions.forEach { region ->
            cardsId.add(region.name)
        }
    }

    fun getRandomCardId(): String = cardsId.random()

    fun getRegion(regionName: String): TextureRegion {
        return cardsAtlas.findRegion(regionName)?: throw IllegalStateException("Отладка. Текстура не найдена")
    }

    fun dispose() {
        cardsAtlas.dispose()
        cardsId.clear()
    }
}
