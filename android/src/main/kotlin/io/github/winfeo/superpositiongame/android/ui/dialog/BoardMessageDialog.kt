package io.github.winfeo.superpositiongame.android.ui.dialog

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import io.github.winfeo.superpositiongame.R

@Composable
fun BoardMessageDialog(
    title: String,
    message: String,
    buttonText: String,
    onConfirm: () -> Unit,
    dismissable: Boolean = false
) {
    BoardDialogSurface(
        onDismissRequest = onConfirm,
        dismissable = dismissable
    ) {
        BoardMessageContent(title, message, buttonText, onConfirm)
    }
}

@Composable
internal fun ColumnScope.BoardMessageContent(
    title: String,
    message: String,
    buttonText: String,
    onConfirm: () -> Unit
) {
    Text(
        text = title,
        color = MaterialTheme.colors.onSurface,
        style = MaterialTheme.typography.h6
    )
    Text(
        text = message,
        color = colorResource(R.color.board_text_muted),
        style = MaterialTheme.typography.body2
    )
    BoardDialogButton(
        modifier = Modifier.fillMaxWidth(),
        text = buttonText,
        onClick = onConfirm
    )
}