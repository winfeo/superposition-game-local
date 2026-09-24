package io.github.winfeo.superpositiongame.android.ui.theme.elements

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class DiagonalCutShape(
    private val cornerRadius: Float = 16f,
    private val cutFraction: Float = 0.67f,
    private val topFraction: Float = 0.75f,
    private val cutWidth: Float = 3f
): Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val radius = with(density) { cornerRadius.dp.toPx() }
        val gap = with(density) { cutWidth.dp.toPx() }

        val bottomX = size.width * cutFraction
        val topX = size.width * topFraction

        val path = Path().apply {
            //левый верхний угол
            arcTo(
                rect = Rect(0f, 0f, radius * 2, radius * 2),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = true
            )

            //верхняя сторона (до разреза)
            lineTo(topX - gap, 0f)

            //разрез
            lineTo(bottomX - gap, size.height)

            //нижняя сторона (до разреза)
            lineTo(radius, size.height)

            //нижний левый угол
            arcTo(
                rect = Rect(0f, size.height - radius * 2, radius * 2, size.height),
                startAngleDegrees = 90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            close()
        }

        path.addPath(
            Path().apply {
                moveTo(topX + gap, 0f)

                //верхняя сторона (после разреза)
                lineTo(size.width - radius, 0f)

                //верхний правый угол
                arcTo(
                    rect = Rect(size.width - radius * 2, 0f, size.width, radius * 2),
                    startAngleDegrees = 270f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                //правая сторона
                lineTo(size.width, size.height - radius)

                //нижний правый угол
                arcTo(
                    rect = Rect(
                        size.width - radius * 2,
                        size.height - radius * 2,
                        size.width,
                        size.height
                    ),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                //нижняя сторона
                lineTo(bottomX + gap, size.height)

                //разрез
                lineTo(topX + gap, 0f)

                close()
            }
        )

        return Outline.Generic(path)
    }
}
