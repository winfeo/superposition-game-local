package io.github.winfeo.superpositiongame.android.ui.screen.lobby

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.android.data.source.local.NotificationManager
import io.github.winfeo.superpositiongame.android.data.source.socket.Network
import io.github.winfeo.superpositiongame.android.domain.lobby.model.Player
import io.github.winfeo.superpositiongame.android.ui.dialog.InviteDialog
import io.github.winfeo.superpositiongame.android.ui.theme.elements.BackgroundBlur

private val LobbyBackgroundColor = Color(0xFF0C0813)
private val LobbyCardColor = Color(0xFF181725)
private val LobbyAccent = Color(0xFF6C8CFF)
private val LobbyGreen = Color(0xFF4ED6A2)
private val LobbyMuted = Color(0xFFA9A8BA)
private val LobbyAvatarColors = listOf(
    Color(0xFF8D85E6),
    Color(0xFFE6A77B),
    Color(0xFF74A9D3)
)

@Composable
fun LobbyScreen(
    viewModel: LobbyViewModel,
    onInvitesClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val selectedPlayer by viewModel.selectedPlayer.collectAsState()
    val notificationCount by NotificationManager.badgeCount.collectAsState()
    val isConnected by Network.connectionState.collectAsState()
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val successMessage = stringResource(R.string.dialog_invitation_confirm_success)
    val errorMessage = stringResource(R.string.dialog_invitation_confirm_error)

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { message ->
            scaffoldState.snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
            viewModel.clearSnackbarMessage()
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = LobbyBackgroundColor,
        snackbarHost = { SnackbarHost(scaffoldState.snackbarHostState) { Snackbar(it) } }
    ) { paddingValues ->
        LobbyContent(
            state = state,
            isConnected = isConnected,
            notificationCount = notificationCount,
            onInvitesClick = onInvitesClick,
            onPlayerClick = viewModel::showInviteDialog,
            modifier = Modifier.padding(paddingValues)
        )
    }

    selectedPlayer?.let { player ->
        InviteDialog(
            playerName = player.nickname ?: player.id.take(9),
            onConfirm = { viewModel.sendInvite(successMessage, errorMessage) },
            onDismiss = viewModel::hideInviteDialog
        )
    }
}

@Composable
private fun LobbyContent(
    state: LobbyState,
    isConnected: Boolean,
    notificationCount: Int,
    onInvitesClick: () -> Unit,
    onPlayerClick: (Player) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
            .background(LobbyBackgroundColor)
    ) {
        BackgroundBlur()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 22.dp, end = 22.dp, top = 30.dp)
        ) {
            LobbyTopBar(isConnected, notificationCount, onInvitesClick)
            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Онлайн-лобби",
                color = Color.White,
                fontSize = 29.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5f).sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Выберите соперника и пригласите его в игру",
                color = LobbyMuted,
                fontSize = 14.sp,
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.height(34.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Игроки в сети",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                PlayerCountBadge(
                    count = if (isConnected && !state.isLoading && state.error == null) {
                        state.players.size
                    } else null
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                when {
                    !isConnected -> LobbyMessage(
                        title = "Нет подключения",
                        description = "Подключаемся к серверу. Список игроков появится, когда соединение восстановится"
                    )
                    state.isLoading -> Column(
                        modifier = Modifier.fillMaxSize().padding(bottom = 124.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(28.dp),
                            color = LobbyAccent,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.height(18.dp))
                        Text("Получаем список игроков…", color = LobbyMuted, fontSize = 14.sp)
                    }
                    state.error != null -> LobbyMessage(
                        title = "Не удалось загрузить игроков",
                        description = state.error ?: "Попробуйте проверить подключение."
                    )
                    state.players.isEmpty() -> LobbyMessage(
                        title = stringResource(R.string.lobby_empty_list),
                        description = "Когда кто-то появится в лобби, вы сможете пригласить его в игру"
                    )
                    else -> UsersList(state.players, onPlayerClick)
                }
            }
        }
    }
}

@Composable
private fun LobbyTopBar(
    isConnected: Boolean,
    notificationCount: Int,
    onInvitesClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val statusColor = if (isConnected) LobbyGreen else LobbyMuted
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(statusColor.copy(alpha = 0.12f))
                .border(1.dp, statusColor.copy(alpha = 0.2f), RoundedCornerShape(50))
                .padding(horizontal = 13.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(statusColor)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isConnected) "Вы в сети" else "Подключаемся",
                color = statusColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        NotificationButton(notificationCount, onInvitesClick)
    }
}

@Composable
private fun NotificationButton(count: Int, onClick: () -> Unit) {
    Box {
        Button(
            onClick = onClick,
            modifier = Modifier.size(42.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = LobbyCardColor),
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_bell),
                contentDescription = "Приглашения",
                tint = Color.White,
                modifier = Modifier.size(21.dp)
            )
        }
        if (count > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(LobbyGreen)
                    .border(2.dp, LobbyBackgroundColor, CircleShape)
            )
        }
    }
}

@Composable
private fun PlayerCountBadge(count: Int?) {
    Text(
        text = count?.let(::playerCountText) ?: "—",
        color = LobbyAccent,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(LobbyAccent.copy(alpha = 0.12f))
            .padding(horizontal = 11.dp, vertical = 6.dp)
    )
}

private fun playerCountText(count: Int): String {
    val suffix = when {
        count % 100 in 11..14 -> "игроков"
        count % 10 == 1 -> "игрок"
        count % 10 in 2..4 -> "игрока"
        else -> "игроков"
    }
    return "$count $suffix"
}

@Composable
private fun UsersList(players: List<Player>, onPlayerClick: (Player) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 124.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(players, key = { player -> player.id }) { player ->
            UserCard(player, onPlayerClick)
        }
        item {
            Row(
                modifier = Modifier.padding(start = 2.dp, top = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    tint = LobbyMuted.copy(alpha = 0.75f),
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    text = "Список обновляется автоматически",
                    color = LobbyMuted.copy(alpha = 0.75f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun UserCard(player: Player, onPlayerClick: (Player) -> Unit) {
    val name = player.nickname?.takeIf { it.isNotBlank() } ?: player.id.take(9)
    val avatarColor = LobbyAvatarColors[
        (player.id.hashCode() and Int.MAX_VALUE) % LobbyAvatarColors.size
    ]

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 76.dp)
            .clip(RoundedCornerShape(17.dp))
            .background(LobbyCardColor)
            .border(1.dp, Color.White.copy(alpha = 0.07f), RoundedCornerShape(17.dp))
            .padding(horizontal = 13.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(43.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(avatarColor.copy(alpha = 0.20f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_panda),
                contentDescription = null,
                tint = avatarColor,
                modifier = Modifier.size(25.dp)
            )
        }
        Spacer(modifier = Modifier.width(11.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(6.dp).clip(CircleShape).background(LobbyGreen)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("В сети", color = LobbyMuted, fontSize = 11.sp)
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = { onPlayerClick(player) },
            modifier = Modifier.height(38.dp),
            shape = RoundedCornerShape(11.dp),
            border = BorderStroke(1.dp, LobbyAccent.copy(alpha = 0.45f)),
            colors = ButtonDefaults.buttonColors(backgroundColor = LobbyAccent.copy(alpha = 0.12f)),
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
            contentPadding = PaddingValues(horizontal = 10.dp)
        ) {
            Text(
                text = "Пригласить",
                color = Color(0xFFB6C4FF),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun LobbyMessage(title: String, description: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(bottom = 124.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(9.dp))
        Text(
            text = description,
            color = LobbyMuted,
            fontSize = 13.sp,
            lineHeight = 19.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 14.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LobbyScreenPrev() {
    MaterialTheme {
        LobbyContent(
            state = LobbyState(
                players = listOf(
                    Player("1", "Алексей"),
                    Player("2", "Мария"),
                    Player("3", "winfeo")
                ),
                isLoading = false
            ),
            isConnected = true,
            notificationCount = 2,
            onInvitesClick = {},
            onPlayerClick = {}
        )
    }
}
