package io.github.winfeo.superpositiongame.android.ui.dialog.game

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.winfeo.superpositiongame.R

@Composable
fun OpponentDisconnectedDialog(
    opponentNickname: String?,
    reconnectDeadline: Long?,
    serverTime: Long
) {
    val remainingSeconds = rememberReconnectSeconds(
        deadline = reconnectDeadline,
        serverTime = serverTime
    )

    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        ConnectionDialogContainer {
            CircularProgressIndicator(
                color = Color(0xFF6C8CFF),
                strokeWidth = 2.dp
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = stringResource(R.string.dialog_opponent_disconnected_title),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(
                    R.string.dialog_opponent_disconnected_info,
                    opponentNickname?.take(16)
                        ?: stringResource(R.string.dialog_reconnect_opponent_fallback)
                ),
                color = Color.White.copy(alpha = 0.68f),
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center
            )

            remainingSeconds?.let {
                Spacer(modifier = Modifier.height(20.dp))
                ReconnectCountdown(seconds = it)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OpponentDisconnectedDialogPreview() {
    OpponentDisconnectedDialog(
        opponentNickname = "winfeo",
        reconnectDeadline = 61_000L,
        serverTime = 1_000L
    )
}
