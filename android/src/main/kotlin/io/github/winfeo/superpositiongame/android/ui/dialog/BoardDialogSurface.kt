package io.github.winfeo.superpositiongame.android.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.winfeo.superpositiongame.R

@Composable
fun BoardDialogSurface(
    onDismissRequest: () -> Unit,
    dismissable: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = dismissable,
            dismissOnClickOutside = dismissable,
            usePlatformDefaultWidth = false
        )
    ) {
        BoardDialogCard(content = content)
    }
}

@Composable
internal fun BoardDialogCard(content: @Composable ColumnScope.() -> Unit) {
    val colors = MaterialTheme.colors
    val shape = RoundedCornerShape(dimensionResource(R.dimen.dialog_corner))
    val space16 = dimensionResource(R.dimen.space_16)
    val space24 = dimensionResource(R.dimen.space_24)
    val top = colorResource(R.color.dialog_surface_top)
    val bottom = colorResource(R.color.dialog_surface_bottom)

    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(space16)
            .clip(shape)
            .background(Brush.verticalGradient(listOf(top, bottom)))
            .background(
                Brush.radialGradient(
                    colors = listOf(colors.primary.copy(alpha = 0.10f), Color.Transparent),
                    radius = 700f
                )
            )
            .border(1.dp, colors.onSurface.copy(alpha = 0.08f), shape)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(space24),
            verticalArrangement = Arrangement.spacedBy(space16),
            content = content
        )
    }
}