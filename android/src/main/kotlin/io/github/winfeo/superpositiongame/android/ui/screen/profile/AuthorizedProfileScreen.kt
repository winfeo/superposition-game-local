package io.github.winfeo.superpositiongame.android.ui.screen.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.android.data.repository.ProfileRepositoryImpl
import io.github.winfeo.superpositiongame.android.data.source.rest.GameHistoryApi
import io.github.winfeo.superpositiongame.android.data.source.rest.UserApi
import io.github.winfeo.superpositiongame.android.ui.dialog.EditNicknameDialog
import io.github.winfeo.superpositiongame.android.ui.screen.profile.components.ProfileSettingsButton
import io.github.winfeo.superpositiongame.android.ui.theme.elements.BackgroundBlur
import io.ktor.client.HttpClient

data class MatchHistoryItem(
    val enemyName: String,
    val result: String,
    val turns: Int,
    val ratingChange: Int
)

@Composable
fun AuthorizedProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateToGameHistory: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val user = state.user

    val isEditDialogVisible by viewModel.isEditNicknameDialogVisible.collectAsState()
    val editError by viewModel.editNicknameError.collectAsState()

    LaunchedEffect(Unit) {
        user?.let {
            viewModel.loadGameHistory(it.id)
            viewModel.loadUserStats(it.id)
        }
    }

    if (user == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0B0812)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF6C8CFF))
        }
        return
    }

    val recentGameHistory = state.gameHistory.take(5)

    val matchHistoryItems = recentGameHistory.map { game ->
        val result =
            if (game.isWinner) stringResource(R.string.authorized_profile_tag_victory)
            else stringResource(R.string.authorized_profile_tag_defeat)
        MatchHistoryItem(
            enemyName = game.opponentNickname,
            result = result,
            turns = game.totalMoves,
            ratingChange = game.ratingChange
        )
    }

    val currentRating = user.ratingPoints
    val nextRankRating = 3000
    val progress = currentRating.toFloat() / nextRankRating.toFloat()


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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(22.dp))

                //Настройки
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProfileSettingsButton(onClick = onNavigateToSettings)
                }

                //Ава
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(190.dp)
                            .blur(48.dp, BlurredEdgeTreatment.Unbounded)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFF6C8CFF).copy(alpha = 0.12f),
                                        Color.Transparent
                                    ),
                                    radius = 420f
                                ),
                                shape = CircleShape
                            )
                    )

                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .blur(80.dp, BlurredEdgeTreatment.Unbounded)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFF9DB2FF).copy(alpha = 0.22f),
                                        Color.Transparent
                                    ),
                                    radius = 240f
                                ),
                                shape = CircleShape
                            )
                    )

                    Box(
                        modifier = Modifier
                            .size(132.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF1A1B2E),
                                        Color(0xFF10111D)
                                    )
                                )
                            )
                            .border(
                                width = 1.dp,
                                color = Color.White.copy(alpha = 0.08f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.White.copy(alpha = 0.08f),
                                            Color.Transparent
                                        )
                                    ),
                                    shape = CircleShape
                                )
                        )

                        Image(
                            painter = painterResource(R.drawable.ic_panda),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        )
                    }
                }

                //Ник
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { viewModel.showEditNicknameDialog() }
                ) {
                    Text(
                        text = user.nickname,
                        color = Color.White,
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.05f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.65f),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "#${user.id}",
                    color = Color.White.copy(alpha = 0.36f),
                    style = MaterialTheme.typography.body2
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = user.league,
                    color = Color(0xFF9DB2FF),
                    style = MaterialTheme.typography.body1
                )

                Spacer(modifier = Modifier.height(34.dp))

                //Статистика
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    //Очки рейтинга
                    ProfileStatCard(
                        modifier = Modifier.weight(1f),
                        title = stringResource(R.string.authorized_profile_rating),
                        value = "${user.ratingPoints}",
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFF9DB2FF)
                            )
                        }
                    )

                    //Количество побед
                    ProfileStatCard(
                        modifier = Modifier.weight(1f),
                        title = stringResource(R.string.authorized_profile_victory_amount),
                        value = "${user.winsAmount}",
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color(0xFFFFD166)
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                //Ранговый прогресс
                RankedSeasonCard(
                    currentRating = currentRating,
                    nextRankRating = nextRankRating,
                    progress = progress,
                    league = user.league
                )

                Spacer(modifier = Modifier.height(28.dp))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.authorized_profile_recent_games),
                    color = Color.White,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.weight(1f)
                )

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF2B36A6).copy(alpha = 0.4f),
                                    Color(0xFF1A1B2E).copy(alpha = 0.3f)
                                )
                            )
                        )
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { onNavigateToGameHistory()  }
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.72f),
                            modifier = Modifier.size(16.dp)
                        )

                        Text(
                            text = stringResource(R.string.authorized_profile_history),
                            color = Color.White.copy(alpha = 0.85f),
                            style = MaterialTheme.typography.body2,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //Посление матчи
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) {
                items(matchHistoryItems) { match ->
                    MatchCard(match)
                }
            }

            Spacer(modifier = Modifier.height(160.dp))
        }

        if (isEditDialogVisible) {
            EditNicknameDialog(
                currentNickname = user.nickname,
                errorMessage = editError,
                onConfirm = { newNickname ->
                    viewModel.updateNickname(newNickname)
                },
                onDismiss = { viewModel.hideEditNicknameDialog() }
            )
        }
    }
}

@Composable
fun ProfileStatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .height(120.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A1B2E),
                        Color(0xFF10111D)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.08f),
                shape = RoundedCornerShape(28.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.07f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            icon()

            Column {
                Text(
                    text = value,
                    color = Color.White,
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = title,
                    color = Color.White.copy(alpha = 0.45f),
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Composable
fun RankedSeasonCard(
    currentRating: Int,
    nextRankRating: Int,
    progress: Float,
    league: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A1B2E),
                        Color(0xFF10111D)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.08f),
                shape = RoundedCornerShape(30.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF6C8CFF).copy(alpha = 0.16f),
                            Color.Transparent
                        ),
                        radius = 900f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = null,
                    tint = Color(0xFF9DB2FF)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = stringResource(R.string.authorized_profile_rang_progress).uppercase(),
                    color = Color.White.copy(alpha = 0.55f),
                    style = MaterialTheme.typography.body2
                )
            }

            Column {
                Text(
                    text = league,
                    color = Color.White,
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "$currentRating MMR",
                    color = Color(0xFF9DB2FF),
                    style = MaterialTheme.typography.body1
                )

                Spacer(modifier = Modifier.height(18.dp))

                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(999.dp)),
                    color = Color(0xFF6C8CFF),
                    backgroundColor = Color.White.copy(alpha = 0.08f)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$currentRating",
                        color = Color.White.copy(alpha = 0.55f),
                        style = MaterialTheme.typography.caption
                    )

                    Text(
                        text = "$nextRankRating",
                        color = Color.White.copy(alpha = 0.55f),
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }
    }
}

@Composable
fun MatchCard(
    match: MatchHistoryItem
) {
    val isVictory = match.result == stringResource(R.string.authorized_profile_tag_victory)

    Box(
        modifier = Modifier
            .widthIn(min = 190.dp, max = 220.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF171827),
                        Color(0xFF10111D)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.08f),
                shape = RoundedCornerShape(28.dp)
            )
            .padding(18.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(
                            if (isVictory) Color(0xFF4CFF93)
                            else Color(0xFFFF6B6B)
                        )
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = match.result,
                    color = Color.White,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = match.enemyName,
                color = Color.White.copy(alpha = 0.92f),
                style = MaterialTheme.typography.h6,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${stringResource(R.string.authorized_profile_moves_made)}: ${match.turns}",
                color = Color.White.copy(alpha = 0.45f),
                style = MaterialTheme.typography.body2
            )

            Spacer(modifier = Modifier.height(6.dp))

            val text =
                if (match.ratingChange > 0) "+${match.ratingChange} ${stringResource(R.string.authorized_profile_rating_change)}"
                else "${match.ratingChange} ${stringResource(R.string.authorized_profile_rating_change)}"
            val color =
                if (match.ratingChange > 0) Color(0xFF6CFF9D)
                else Color(0xFFFF6B6B)

            Text(
                text = text,
                color = color,
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun AuthorizedProfileScreenPreview() {
    AuthorizedProfileScreen(
        viewModel = ProfileViewModel(
            repository = ProfileRepositoryImpl(UserApi(HttpClient()), GameHistoryApi(HttpClient()))
        ),
        onNavigateToGameHistory = {},
        onNavigateToSettings = {}
    )
}
