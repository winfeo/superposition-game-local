package io.github.winfeo.superpositiongame.android.ui.screen.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.android.domain.ai.model.AiDifficulty
import io.github.winfeo.superpositiongame.android.domain.ai.model.AiGameError
import io.github.winfeo.superpositiongame.android.ui.theme.elements.BackgroundBlur

private val Accent = Color(0xFF6C8CFF)
private val AccentLight = Color(0xFF9DB2FF)

@Composable
fun AiGameScreen(viewModel: AiGameViewModel) {
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0C0813))
    ) {
        BackgroundBlur()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 48.dp, bottom = 124.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.ai_game_title),
                color = Color.White.copy(alpha = 0.94f),
                style = MaterialTheme.typography.h4,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.ai_game_subtitle),
                color = Color.White.copy(alpha = 0.5f),
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF1A1B2E), Color(0xFF10111D))
                        )
                    )
                    .border(
                        1.dp,
                        Color.White.copy(alpha = 0.08f),
                        RoundedCornerShape(24.dp)
                    )
                    .padding(18.dp)
            ) {
                Text(
                    text = stringResource(R.string.ai_game_difficulty_title),
                    color = Color.White.copy(alpha = 0.86f),
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(16.dp))

                DifficultyOption(
                    title = stringResource(R.string.ai_game_low_title),
                    description = stringResource(R.string.ai_game_low_description),
                    selected = state.selectedDifficulty == AiDifficulty.LOW,
                    enabled = !state.isLoading,
                    onClick = { viewModel.selectDifficulty(AiDifficulty.LOW) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                DifficultyOption(
                    title = stringResource(R.string.ai_game_mcts_title),
                    description = stringResource(R.string.ai_game_mcts_description),
                    selected = state.selectedDifficulty == AiDifficulty.MCTS,
                    enabled = !state.isLoading,
                    onClick = { viewModel.selectDifficulty(AiDifficulty.MCTS) }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.ai_game_unranked_info),
                color = Color.White.copy(alpha = 0.48f),
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(26.dp))

            val canStart = state.isConnected && !state.isLoading
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            if (canStart) listOf(Accent, AccentLight)
                            else listOf(Color(0xFF34384F), Color(0xFF41445A))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = viewModel::startGame,
                    enabled = canStart,
                    modifier = Modifier.fillMaxSize(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent,
                        disabledBackgroundColor = Color.Transparent
                    ),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp
                    )
                ) {
                    if (state.isLoading) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = Color.White
                            )
                            Text(
                                text = stringResource(R.string.ai_game_starting),
                                color = Color.White,
                                style = MaterialTheme.typography.button
                            )
                        }
                    } else {
                        Text(
                            text = stringResource(R.string.ai_game_start_button),
                            color = Color.White,
                            style = MaterialTheme.typography.button,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            val message = when (state.error) {
                AiGameError.NO_CONNECTION -> R.string.ai_game_error_no_connection
                AiGameError.SESSION_UNAVAILABLE -> R.string.ai_game_error_session_unavailable
                AiGameError.INVALID_SESSION -> R.string.ai_game_error_invalid_session
                AiGameError.ALREADY_IN_GAME -> R.string.ai_game_error_already_in_game
                AiGameError.SERVER_UNAVAILABLE -> R.string.ai_game_error_server_unavailable
                AiGameError.NETWORK_ERROR -> R.string.ai_game_error_network
                AiGameError.UNKNOWN -> R.string.ai_game_error_unknown
                null -> if (!state.isConnected) R.string.ai_game_waiting_connection else null
            }

            if (message != null) {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = stringResource(message),
                    color = if (state.error == null) {
                        Color.White.copy(alpha = 0.45f)
                    } else {
                        Color(0xFFFFA9B4)
                    },
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun DifficultyOption(
    title: String,
    description: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(
                if (selected) Accent.copy(alpha = 0.14f)
                else Color.White.copy(alpha = 0.035f)
            )
            .border(
                1.dp,
                if (selected) Accent.copy(alpha = 0.65f)
                else Color.White.copy(alpha = 0.07f),
                shape
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .border(
                    2.dp,
                    if (selected) AccentLight else Color.White.copy(alpha = 0.35f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(AccentLight, CircleShape)
                )
            }
        }

        Column {
            Text(
                text = title,
                color = Color.White.copy(alpha = 0.93f),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = description,
                color = Color.White.copy(alpha = 0.52f),
                style = MaterialTheme.typography.body2
            )
        }
    }
}
