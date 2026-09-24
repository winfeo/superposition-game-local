package io.github.winfeo.superpositiongame.android.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
fun InviteDialog(
    playerName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
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
                                Color(0xFF6C8CFF).copy(alpha = 0.12f),
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
                Text(
                    text = stringResource(R.string.dialog_invitation_title),
                    color = Color.White.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.h6
                )

                Column {
                    Text(
                        text = stringResource(R.string.dialog_invitation_send_info),
                        color = Color.White.copy(alpha = 0.65f),
                        style = MaterialTheme.typography.body2
                    )

                    Text(
                        text = playerName.take(9),
                        color = Color.White.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.body1
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DialogButton(
                        modifier = Modifier.weight(1f),
                        text = stringResource(R.string.dialog_invitation_cancel),
                        isPrimary = false,
                        onClick = onDismiss
                    )

                    DialogButton(
                        modifier = Modifier.weight(1f),
                        text = stringResource(R.string.dialog_invitation_send),
                        isPrimary = true,
                        onClick = onConfirm
                    )
                }
            }
        }
    }
}

@Composable
fun DialogButton(
    modifier: Modifier = Modifier,
    text: String,
    isPrimary: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(14.dp)

    Box(
        modifier = modifier
            .height(52.dp)
            .then(
                if (isPrimary) {
                    Modifier.background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF4B5DFF),
                                Color(0xFF6C8CFF)
                            )
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
        Text(
            text = text,
            color = if (isPrimary) {
                Color.White
            } else {
                Color.White.copy(alpha = 0.85f)
            },
            style = MaterialTheme.typography.body2
        )
    }
}



@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun InviteDialogPrev() {
    InviteDialog(
        playerName = "12345",
        onConfirm = {},
        onDismiss = {}
    )
}
