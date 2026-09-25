package io.github.winfeo.superpositiongame.android.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import io.github.winfeo.superpositiongame.R

@Composable
fun BoardConfirmationDialog(
    title: String,
    message: String,
    confirmText: String,
    cancelText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    BoardDialogSurface(
        onDismissRequest = onDismiss
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_16))
        ) {
            BoardDialogButton(
                modifier = Modifier.weight(1f),
                text = cancelText,
                onClick = onDismiss,
                primary = false
            )

            BoardDialogButton(
                modifier = Modifier.weight(1f),
                text = confirmText,
                onClick = onConfirm
            )
        }
    }
}
