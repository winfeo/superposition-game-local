package io.github.winfeo.superpositiongame.android.ui.dialog

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import io.github.winfeo.superpositiongame.R

@Composable
fun BoardMessageOverlay(
    title: String,
    message: String,
    buttonText: String,
    onConfirm: () -> Unit
) {
    BackHandler { }
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.dialog_scrim))
            .clickable(interactionSource = interactionSource, indication = null) {},
        contentAlignment = Alignment.Center
    ) {
        BoardDialogCard {
            BoardMessageContent(title, message, buttonText, onConfirm)
        }
    }
}