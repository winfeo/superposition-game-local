package io.github.winfeo.superpositiongame.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.github.winfeo.superpositiongame.R

@Composable
fun SuperpositionGameTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val primary = colorResource(R.color.board_primary)
    val primaryVariant = colorResource(R.color.board_primary_variant)
    val secondary = colorResource(R.color.board_secondary)
    val onDark = colorResource(R.color.board_on_dark)
    val onLight = colorResource(R.color.board_on_light)
    val error = colorResource(R.color.board_error)
    val colors = if (darkTheme) {
        darkColors(
            primary = primary,
            primaryVariant = primaryVariant,
            secondary = secondary,
            background = colorResource(R.color.board_background),
            surface = colorResource(R.color.board_surface),
            error = error,
            onPrimary = onDark,
            onSecondary = onLight,
            onBackground = onDark,
            onSurface = onDark,
            onError = onDark
        )
    } else {
        lightColors(
            primary = primary,
            primaryVariant = primaryVariant,
            secondary = secondary,
            background = colorResource(R.color.board_light_background),
            surface = colorResource(R.color.board_light_background),
            error = error,
            onPrimary = onDark,
            onSecondary = onLight,
            onBackground = onLight,
            onSurface = onLight,
            onError = onDark
        )
    }
    val typography = Typography(
        h4 = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold),
        h5 = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
        body1 = TextStyle(fontSize = 16.sp),
        subtitle1 = TextStyle(fontSize = 16.sp),
        button = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    )

    MaterialTheme(
        colors = colors,
        typography = typography,
        content = content
    )
}
