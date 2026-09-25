package io.github.winfeo.superpositiongame.android.ui.dialog

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import io.github.winfeo.superpositiongame.R

@Composable
fun BoardDialogButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    primary: Boolean = true,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val indication = LocalIndication.current
    val colors = MaterialTheme.colors
    val shape = RoundedCornerShape(dimensionResource(R.dimen.dialog_button_corner))
    val active = primary && enabled
    val background: Brush =
        if (active) Brush.horizontalGradient(listOf(colorResource(R.color.dialog_button_start), colors.primary))
        else SolidColor(colors.onSurface.copy(alpha = 0.04f))

    Box(
        modifier = modifier
            .height(dimensionResource(R.dimen.dialog_button_height))
            .clip(shape)
            .background(background)
            .border(Dp.Hairline, colors.onSurface.copy(alpha = 0.08f), shape)
            .clickable(
                interactionSource = interactionSource,
                indication = indication,
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = when {
                !enabled -> colors.onSurface.copy(alpha = 0.35f)
                active -> colors.onPrimary
                else -> colors.onSurface.copy(alpha = 0.85f)
            },
            style = MaterialTheme.typography.body2
        )
    }
}
