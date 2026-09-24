package io.github.winfeo.superpositiongame.ui.actor.dice

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import io.github.winfeo.superpositiongame.manager.GameAssetsManager
import io.github.winfeo.superpositiongame.model.dice.Dice

//Класс для отрисовки игрового кубита
class DiceActor(
    private val assetsManager: GameAssetsManager,
    sideSize: Float,
    var dice: Dice
): Image() {

    init {
        setSize(sideSize,sideSize)
        updateTexture()
    }

//    fun changeState(newState: DiceState) {
//        previousDice = dice
//        dice = dice.copy(state = newState)
//        val newTexture = DiceAtlasManager.getStateTexture(newState.textureId)
//        drawable = TextureRegionDrawable(newTexture)
//    }
//
//    fun enableTouchableEffect(){
//        touchable = Touchable.enabled
//    }
//
//    fun disableTouchableEffect() {
//        touchable = Touchable.disabled
//    }
//
//    fun setPreviousMoveDiceType() {
//        changeState(previousDice.state)
//    }

    fun render(newDice: Dice) {
        if (newDice.state == dice.state) return
        dice = newDice
        updateTexture()
    }

    private fun updateTexture() {
        val texture: TextureRegion = assetsManager.getDiceTexture(dice.state.textureId)
        drawable = TextureRegionDrawable(texture)
    }

//    override fun draw(batch: Batch, parentAlpha: Float) {
//        batch.draw(
//            texture,
//            x,
//            y,
//            width,
//            height
//        )
//    }

}
