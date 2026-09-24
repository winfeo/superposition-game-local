package io.github.winfeo.superpositiongame.android.ui.screen.history

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.android.data.repository.GameHistoryRepositoryImpl
import io.github.winfeo.superpositiongame.android.data.source.rest.GameHistoryApi
import io.github.winfeo.superpositiongame.android.data.source.local.UserSession
import io.github.winfeo.superpositiongame.android.domain.history.GameHistoryItem
import io.github.winfeo.superpositiongame.android.ui.theme.elements.BackgroundBlur
import io.github.winfeo.superpositiongame.android.util.TimeFormatter
import io.ktor.client.HttpClient

@Composable
fun GameHistoryScreen(
    viewModel: GameHistoryViewModel,
    onReturnToProfile: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val userId by UserSession.currentUserId.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadHistory(userId!!.toLong())
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0812))
    ) {
        BackgroundBlur()

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.White.copy(alpha = 0.015f))
        )

        Column(modifier = Modifier.fillMaxSize()) {
            HistoryHeader(onBack = onReturnToProfile)

            Spacer(modifier = Modifier.height(4.dp))

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            color = Color(0xFF6C8CFF)
                        )
                    }
                }
                state.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${stringResource(R.string.app_error)}: ${state.error}",
                            color = Color.White.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
                state.history.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.history_empty_list),
                            color = Color.White.copy(alpha = 0.45f),
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.history) { game ->
                            HistoryMatchCard(game = game)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryHeader(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2B36A6).copy(alpha = 0.45f),
                        Color(0xFF15162A).copy(alpha = 0.35f)
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF6C8CFF).copy(alpha = 0.15f),
                            Color.Transparent
                        ),
                        radius = 900f
                    )
                )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onBack() },
                modifier = Modifier.size(42.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.9f)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = stringResource(R.string.history_title),
                color = Color.White.copy(alpha = 0.92f),
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.White.copy(alpha = 0.15f),
                        Color.Transparent
                    )
                )
            )
    )
}

@Composable
fun HistoryMatchCard(game: GameHistoryItem) {
    val isVictory = game.isWinner
    val ratingColor = if (game.ratingChange > 0) Color(0xFF6CFF9D) else Color(0xFFFF6B6B)
    val formattedTime = TimeFormatter.formatDateTime(game.playedAt)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.06f),
                        Color.White.copy(alpha = 0.03f)
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.08f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = game.opponentNickname,
                    color = Color.White.copy(alpha = 0.92f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                if (isVictory) Color(0xFF4CFF93) else Color(0xFFFF6B6B)
                            )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    val text =
                        if (isVictory) stringResource(R.string.history_tag_victory)
                        else stringResource(R.string.history_tag_defeat)

                    Text(
                        text = text,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }
                Text(
                    text = formattedTime,
                    color = Color.White.copy(alpha = 0.45f),
                    fontSize = 12.sp
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${stringResource(R.string.history_moves_made)}: ${game.totalMoves}",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                val ratingChange =
                    if (game.ratingChange > 0) "+${game.ratingChange}"
                    else "${game.ratingChange}"

                Text(
                    text = ratingChange,
                    color = ratingColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun GameHistoryScreenPrev() {
    GameHistoryScreen(
        viewModel = GameHistoryViewModel(GameHistoryRepositoryImpl(GameHistoryApi(HttpClient()))),
        onReturnToProfile = {}
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true
)

@Composable
fun HistoryMatchCardPrev() {
    HistoryMatchCard(
        game = GameHistoryItem(
            isWinner = false,
            opponentNickname = "Гость",
            totalMoves = 23,
            ratingChange = -17,
            playedAt = "2026-06-03"
        )
    )
}
