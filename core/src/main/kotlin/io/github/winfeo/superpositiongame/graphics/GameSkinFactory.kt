package io.github.winfeo.superpositiongame.graphics

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import io.github.winfeo.superpositiongame.config.GameConfig

object GameSkinFactory {
    //TODO Лениво создавать? Чтобы не тратить ресурсы пересоздавая каждый раз
    fun createTimerLabelSkin(): Label.LabelStyle {
        val fontScale = GameConfig.screenHeight * 0.002f
        val fontColor = Color.WHITE
        val font = BitmapFont()
        font.data.setScale(fontScale)
        return Label.LabelStyle(font, fontColor)
    }

    fun createSelectionDialogSkin(stage: Stage): Skin {
        val skin = Skin()

        val font = BitmapFont()
        skin.add("default-font", font)

        fun createColorDrawable(color: Color): TextureRegionDrawable {
            val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
            pixmap.setColor(color)
            pixmap.fill()
            val texture = Texture(pixmap)
            pixmap.dispose()
            return TextureRegionDrawable(texture)
        }

        val windowStyle = Window.WindowStyle().apply {
            this.titleFont = skin.getFont("default-font")
            this.titleFontColor = Color.WHITE
            this.background = createColorDrawable(Color(0.1f, 0.1f, 0.1f, 0.95f))
        }
        skin.add("default", windowStyle)

        val labelStyle = Label.LabelStyle().apply {
            this.font = skin.getFont("default-font")
            this.fontColor = Color.WHITE
        }
        skin.add("default", labelStyle)

        return skin
    }

    fun createVictoryDialogSkin(stage: Stage): Skin {
        val skin = Skin()

        //val font = BitmapFont()
        val fontScale = GameConfig.screenHeight * 0.002f
        val font = BitmapFont()
        font.data.setScale(fontScale)
        skin.add("default-font", font)

        fun createColorDrawable(color: Color): TextureRegionDrawable {
            val pixmap = Pixmap(stage.width.toInt() / 2, stage.width.toInt() / 2, Pixmap.Format.RGBA8888)
            pixmap.setColor(color)
            pixmap.fill()
            val texture = Texture(pixmap)
            pixmap.dispose()
            return TextureRegionDrawable(texture)
        }

        val windowStyle = Window.WindowStyle().apply {
            this.titleFont = skin.getFont("default-font")
            this.titleFontColor = Color.WHITE
            this.background = createColorDrawable(Color(0.1f, 0.1f, 0.1f, 0.95f))
        }
        skin.add("default", windowStyle)

        val labelStyle = Label.LabelStyle().apply {
            this.font = skin.getFont("default-font")
            this.fontColor = Color.WHITE
        }
        skin.add("default", labelStyle)

        val textButtonStyle = com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle().apply {
            this.up = createColorDrawable(Color.DARK_GRAY)
            this.down = createColorDrawable(Color.GRAY)
            this.checked = createColorDrawable(Color.LIGHT_GRAY)
            this.font = skin.getFont("default-font")
            this.fontColor = Color.WHITE
        }
        skin.add("default", textButtonStyle)

        return skin
    }
}
