package io.github.winfeo.superpositiongame.android.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.winfeo.superpositiongame.R

@Composable
fun DeleteAccountDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1A1B2E),
                            Color(0xFF11121F)
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(24.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFFF6B6B).copy(alpha = 0.12f),
                                Color.Transparent
                            ),
                            radius = 700f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFFF6B6B),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(R.string.dialog_settings_delete_account),
                        color = Color(0xFFFF6B6B),
                        style = MaterialTheme.typography.h6
                    )
                }

                Text(
                    text = stringResource(R.string.dialog_settings_delete_info),
                    color = Color.White.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.body2
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SettingsDialogButton(
                        modifier = Modifier.weight(1f),
                        text = stringResource(R.string.dialog_settings_cancel),
                        isPrimary = false,
                        onClick = onDismiss
                    )

                    SettingsDialogButton(
                        modifier = Modifier.weight(1f),
                        text = stringResource(R.string.dialog_settings_delete_confirm),
                        isPrimary = true,
                        onClick = onConfirm,
                        buttonColor = Color(0xFFFF6B6B)
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsDialogButton(
    modifier: Modifier = Modifier,
    text: String,
    isPrimary: Boolean,
    onClick: () -> Unit,
    buttonColor: Color = Color(0xFF6C8CFF)
) {
    val shape = RoundedCornerShape(14.dp)

    Box(
        modifier = modifier
            .height(52.dp)
            .then(
                if (isPrimary) {
                    Modifier.background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(buttonColor, buttonColor.copy(alpha = 0.8f))
                        ),
                        shape = shape
                    )
                } else {
                    Modifier
                        .background(
                            color = Color.White.copy(alpha = 0.04f),
                            shape = shape
                        )
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.08f),
                            shape = shape
                        )
                }
            )
            .clip(shape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        val color =
            if (isPrimary) Color.White
            else Color.White.copy(alpha = 0.85f)

        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.body2
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun DeleteAccountDialogPrev() {
    DeleteAccountDialog(
        onConfirm = {},
        onDismiss = {}
    )
}
