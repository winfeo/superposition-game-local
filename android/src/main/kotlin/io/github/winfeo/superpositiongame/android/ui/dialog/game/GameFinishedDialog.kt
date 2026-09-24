package io.github.winfeo.superpositiongame.android.ui.dialog.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.winfeo.superpositiongame.R

@Composable
fun GameFinishedDialog(
    isWinner: Boolean,
    onReturnToLobby: () -> Unit
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp)
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
                    color = Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(24.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                if (isWinner)
                                    Color(0xFF35FF21).copy(alpha = 0.12f)
                                else
                                    Color(0xFFFF6B6B).copy(alpha = 0.1f),
                                Color.Transparent
                            ),
                            radius = 900f
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                val textType =
                    if (isWinner) stringResource(R.string.dialog_game_finished_victory)
                    else stringResource(R.string.dialog_game_finished_defeat)

                Text(
                    text = textType,
                    color = Color.White,
                    style = MaterialTheme.typography.h5
                )

                val textInfo =
                    if (isWinner) stringResource(R.string.dialog_game_finished_victory_info)
                    else stringResource(R.string.dialog_game_finished_defeat_info)

                Text(
                    text = textInfo,
                    color = Color.White.copy(alpha = 0.72f),
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Center
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF4B5DFF),
                                    Color(0xFF6C8CFF)
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            onReturnToLobby()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.dialog_game_finished_return_to_lobby),
                        color = Color.White,
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun GameFinishedDialogPrev() {
    GameFinishedDialog(
        isWinner = true,
        onReturnToLobby = {}
    )
}
