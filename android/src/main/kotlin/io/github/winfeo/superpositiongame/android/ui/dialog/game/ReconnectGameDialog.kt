package io.github.winfeo.superpositiongame.android.ui.dialog.game

import android.os.SystemClock
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import kotlinx.coroutines.delay
import kotlin.math.ceil

@Composable
fun ReconnectGameDialog(
    opponentNickname: String?,
    reconnectDeadline: Long?,
    serverTime: Long,
    isReconnecting: Boolean,
    onReconnect: () -> Unit,
    onDecline: () -> Unit,
    onExpired: () -> Unit
) {
    val remainingSeconds = rememberReconnectSeconds(
        deadline = reconnectDeadline,
        serverTime = serverTime
    )
    val canReconnect = !isReconnecting && (remainingSeconds == null || remainingSeconds > 0)

    LaunchedEffect(remainingSeconds) {
        if (remainingSeconds == 0) {
            delay(1_500L)
            onExpired()
        }
    }

    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        ConnectionDialogContainer {
            Text(
                text = stringResource(R.string.dialog_reconnect_title),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = stringResource(
                    R.string.dialog_reconnect_info,
                    opponentNickname?.take(16)?: stringResource(R.string.dialog_reconnect_opponent_fallback)
                ),
                color = Color.White.copy(alpha = 0.68f),
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            remainingSeconds?.let {
                ReconnectCountdown(seconds = it)
                Spacer(modifier = Modifier.height(20.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ReconnectDialogButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.dialog_reconnect_decline),
                    accent = Color(0xFFFF6B6B),
                    enabled = !isReconnecting,
                    onClick = onDecline
                )

                ReconnectDialogButton(
                    modifier = Modifier.weight(1f),
                    text =
                        if (isReconnecting) stringResource(R.string.dialog_reconnect_connecting)
                        else stringResource(R.string.dialog_reconnect_confirm),
                    accent = Color(0xFF6C8CFF),
                    enabled = canReconnect,
                    onClick = onReconnect
                )
            }
        }
    }
}

@Composable
internal fun ConnectionDialogContainer(
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF1A1526),
                        Color(0xFF120E1C)
                    )
                )
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
                        listOf(
                            Color(0xFF6C8CFF).copy(alpha = 0.14f),
                            Color.Transparent
                        ),
                        radius = 750f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content
        )
    }
}

@Composable
internal fun ReconnectCountdown(seconds: Int) {
    val minutes = seconds / 60
    val secondsPart = seconds % 60

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .border(
                1.dp,
                Color(0xFF6C8CFF).copy(alpha = 0.22f),
                RoundedCornerShape(18.dp)
            )
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.dialog_reconnect_time_left),
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = "%02d:%02d".format(minutes, secondsPart),
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
    }
}

@Composable
private fun ReconnectDialogButton(
    modifier: Modifier,
    text: String,
    accent: Color,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)

    Box(
        modifier = modifier
            .height(54.dp)
            .clip(shape)
            .background(
                Brush.horizontalGradient(
                    listOf(
                        accent.copy(alpha = if (enabled) 0.65f else 0.2f),
                        accent.copy(alpha = if (enabled) 0.9f else 0.3f)
                    )
                )
            )
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White.copy(alpha = if (enabled) 1f else 0.45f),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
internal fun rememberReconnectSeconds(
    deadline: Long?,
    serverTime: Long
): Int? {
    if (deadline == null) return null

    val initialServerTime = remember(deadline, serverTime) {
        serverTime.takeIf { it > 0L } ?: System.currentTimeMillis()
    }
    val initialRemainingMs = remember(deadline, initialServerTime) {
        (deadline - initialServerTime).coerceAtLeast(0L)
    }
    val startedAt = remember(deadline, initialServerTime) {
        SystemClock.elapsedRealtime()
    }
    var seconds by remember(deadline, initialServerTime) {
        mutableStateOf(ceil(initialRemainingMs / 1_000.0).toInt())
    }

    LaunchedEffect(deadline, initialServerTime) {
        while (seconds > 0) {
            val elapsed = SystemClock.elapsedRealtime() - startedAt
            seconds = ceil(
                (initialRemainingMs - elapsed).coerceAtLeast(0L) / 1_000.0
            ).toInt()
            delay(250L)
        }
    }

    return seconds
}

@Preview(showBackground = true)
@Composable
private fun ReconnectGameDialogPreview() {
    ReconnectGameDialog(
        opponentNickname = "winfeo",
        reconnectDeadline = 61_000L,
        serverTime = 1_000L,
        isReconnecting = false,
        onReconnect = {},
        onDecline = {},
        onExpired = {}
    )
}
