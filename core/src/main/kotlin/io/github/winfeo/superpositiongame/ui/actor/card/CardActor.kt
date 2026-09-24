package io.github.winfeo.superpositiongame.ui.actor.card

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import io.github.winfeo.superpositiongame.manager.GameAssetsManager
import io.github.winfeo.superpositiongame.model.card.Card

//Отрисовка игровой карты
class CardActor(
    private val assetsManager: GameAssetsManager,
    cardWidth: Float,
    cardHeight: Float,
    var card: Card,
    var canDrag: Boolean = false,
//    private var touchable: Touchable = Touchable.enabled,
): Image() {

    init {
        setSize(cardWidth,cardHeight)
        updateTexture()
    }

    fun render(newCard: Card) {
        card = newCard
        updateTexture()
    }

    private fun updateTexture() {
        val texture: TextureRegion = assetsManager.getCardTexture(card.textureId!!)
        drawable = TextureRegionDrawable(texture)
    }

}
