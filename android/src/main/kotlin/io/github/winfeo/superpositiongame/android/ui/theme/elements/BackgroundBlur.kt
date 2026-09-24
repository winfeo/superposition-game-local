package io.github.winfeo.superpositiongame.android.ui.theme.elements

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp

@Composable
fun BackgroundBlur() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .blur(200.dp)
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {

            val topArc = Path().apply {
                moveTo(size.width * -0.25f, size.height * 0.18f)

                cubicTo(
                    size.width * 0.10f, size.height * -0.05f,
                    size.width * 0.35f, size.height * -0.05f,
                    size.width * 0.55f, size.height * 0.18f
                )

                cubicTo(
                    size.width * 0.70f, size.height * 0.42f,
                    size.width * 0.90f, size.height * 0.42f,
                    size.width * 1.15f, size.height * 0.18f
                )
            }

            drawPath(
                path = topArc,
                color = Color(0xFF3D4AEB).copy(alpha = 0.1f)
            )

            drawPath(
                path = topArc,
                color = Color(0xFF3D4AEB).copy(alpha = 0.1f)
            )

            drawPath(
                path = topArc,
                color = Color(0xFF6C8CFF).copy(alpha = 0.08f)
            )

            val topBlob2 = Path().apply {
                moveTo(size.width * -0.20f, size.height * 0.20f)

                cubicTo(
                    size.width * 0.15f, size.height * 0.00f,
                    size.width * 0.40f, size.height * 0.10f,
                    size.width * 0.60f, size.height * 0.25f
                )

                cubicTo(
                    size.width * 0.80f, size.height * 0.45f,
                    size.width * 0.45f, size.height * 0.50f,
                    size.width * 0.05f, size.height * 0.30f
                )

                close()
            }

            drawPath(
                path = topBlob2,
                color = Color(0xFF3D4AEB).copy(alpha = 0.06f)
            )

            val bottomBlob = Path().apply {
                moveTo(size.width * 0.55f, size.height * 0.75f)

                cubicTo(
                    size.width * 1.2f, size.height * 0.55f,
                    size.width * 1.1f, size.height * 1.1f,
                    size.width * 0.7f, size.height * 1.0f
                )

                cubicTo(
                    size.width * 0.2f, size.height * 1.1f,
                    size.width * 0.1f, size.height * 0.7f,
                    size.width * 0.4f, size.height * 0.6f
                )

                cubicTo(
                    size.width * 0.6f, size.height * 0.55f,
                    size.width * 0.6f, size.height * 0.65f,
                    size.width * 0.55f, size.height * 0.75f
                )

                close()
            }

            drawPath(
                path = bottomBlob,
                color = Color(0xFF3D4AEB).copy(alpha = 0.08f)
            )

            val secondBlob = Path().apply {
                moveTo(size.width * 0.65f, size.height * 0.80f)

                cubicTo(
                    size.width * 1.25f, size.height * 0.60f,
                    size.width * 1.15f, size.height * 1.15f,
                    size.width * 0.75f, size.height * 1.05f
                )

                cubicTo(
                    size.width * 0.25f, size.height * 1.15f,
                    size.width * 0.15f, size.height * 0.75f,
                    size.width * 0.45f, size.height * 0.65f
                )

                cubicTo(
                    size.width * 0.65f, size.height * 0.60f,
                    size.width * 0.65f, size.height * 0.70f,
                    size.width * 0.65f, size.height * 0.80f
                )

                close()
            }

            drawPath(
                path = secondBlob,
                color = Color(0xFF3D4AEB).copy(alpha = 0.08f)
            )
        }
    }
}

