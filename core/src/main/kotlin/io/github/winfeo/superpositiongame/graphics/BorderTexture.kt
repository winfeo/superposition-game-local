package io.github.winfeo.superpositiongame.graphics

import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import io.github.winfeo.superpositiongame.config.GameConfig
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

//объект для отрисовки текстуры рамки ячейки карты
object BorderTexture {
    private const val TEXTURE_SCALE = 2
    //private val textures = mutableMapOf<String, Texture>() //все виды рамок
    //одна текстура, размер рамки статический так как?
    private var texture: Texture? = null

//    private val bordersThickness = GameConfig.getCardBorderThickness().toInt()
//    private val cornerRadius = GameConfig.getCardBorderRadius().toInt()
//    private val cardWidth = GameConfig.cardWidth.toInt()
//    private val cardHeight = GameConfig.cardHeight.toInt()


//    fun clear() {
//        textures.values.forEach { it.dispose() }
//        textures.clear()
//    }

    private fun makeKey(
        width: Int,
        height: Int,
        radius: Int,
        thickness: Int
    ): String {
        return "${width}_${height}_${radius}_${thickness}"
    }

    fun getBorderTexture(): Texture {
        if (texture == null) {
            texture = createTexture()
        }
        return texture!!
//        texture = Texture("cards/backfround_transp.png")
//        texture?.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
//        return texture!!
    }

    private fun createTexture(): Texture {
        val cardWidth = GameConfig.cardWidth.toInt()
        val cardHeight = GameConfig.cardHeight.toInt()
        val bordersThickness = GameConfig.getCardBorderThickness().toInt()
        val cornerRadius = GameConfig.getCardBorderRadius().toInt()

        val pixmap = Pixmap(cardWidth, cardHeight, Pixmap.Format.RGBA8888)
        pixmap.setColor(0f, 0f, 0f, 0f)
        pixmap.fill()

        for (y in 0..<cardHeight) {
            for (x in 0..<cardWidth) {
                val alpha = calculatePixelAlpha(
                    x, y,
                    cardWidth,
                    cardHeight,
                    cornerRadius,
                    bordersThickness)
                if (alpha > 0f) {
                    pixmap.setColor(1f, 1f, 1f, alpha)
                    pixmap.drawPixel(x, y)
                }
            }
        }

        val texture = Texture(pixmap)
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        pixmap.dispose()

        return texture
    }

    //мат. расчёты отрисовки
    private fun calculatePixelAlpha(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        radius: Int,
        thickness: Int
    ): Float {
        val px = x + 0.5f
        val py = y + 0.5f
        val r = radius.toFloat()
        val t = thickness.toFloat()

        val innerLeft = r
        val innerRight = width - r
        val innerBottom = r
        val innerTop = height - r

        val nearestX = px.coerceIn(innerLeft, innerRight)
        val nearestY = py.coerceIn(innerBottom, innerTop)

        val dx = px - nearestX
        val dy = py - nearestY
        val dist = sqrt(dx * dx + dy * dy)

        val outer = r
        val inner = max(0f, r - t)

        val sdf = when {
            dist > outer -> dist - outer
            dist < inner -> inner - dist
            else -> -1 * min(dist - inner, outer - dist)
        }

        val aa = 1f
        return (0.5f - sdf / aa).coerceIn(0f, 1f)
    }

    fun clear() {
        texture?.dispose()
        texture = null
    }
}
