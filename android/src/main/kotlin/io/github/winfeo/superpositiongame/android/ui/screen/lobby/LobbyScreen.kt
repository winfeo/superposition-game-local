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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.android.domain.game.model.BoardPlayer
import io.github.winfeo.superpositiongame.android.domain.game.model.BoardSession
import io.github.winfeo.superpositiongame.android.domain.game.model.ConnectionStatus
import io.github.winfeo.superpositiongame.android.ui.dialog.BoardConfirmationDialog
import io.github.winfeo.superpositiongame.android.ui.dialog.BoardMessageOverlay
import io.github.winfeo.superpositiongame.android.ui.theme.elements.BackgroundBlur

@Composable
fun LobbyScreen(viewModel: LobbyViewModel) {
    val session by viewModel.session.collectAsState()
    var selectedOpponent by remember { mutableStateOf<BoardPlayer?>(null) }

    LaunchedEffect(session.connection) {
        if (session.connection != ConnectionStatus.CONNECTED) selectedOpponent = null
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        BackgroundBlur()
        LobbyContent(session, onPlayerClick = { selectedOpponent = it })

        if (session.connection == ConnectionStatus.ERROR) {
            BoardMessageOverlay(
                title = stringResource(R.string.network_error_title),
                message = stringResource(R.string.lobby_connection_timeout),
                buttonText = stringResource(R.string.action_retry),
                onConfirm = viewModel::retryConnection
            )
        }
    }

    if (session.connection == ConnectionStatus.CONNECTED) {
        selectedOpponent?.let { opponent ->
            BoardConfirmationDialog(
                title = stringResource(R.string.lobby_start_game_title),
                message = stringResource(R.string.lobby_start_game_message, opponent.name),
                confirmText = stringResource(R.string.action_start),
                cancelText = stringResource(R.string.action_cancel),
                onConfirm = {
                    viewModel.startGame(opponent.id)
                    selectedOpponent = null
                },
                onDismiss = { selectedOpponent = null }
            )
        }
    }
}

@Composable
private fun LobbyContent(
    session: BoardSession,
    onPlayerClick: (BoardPlayer) -> Unit
) {
    val colors = MaterialTheme.colors
    val muted = colorResource(R.color.board_text_muted)
    val spacing = dimensionResource(R.dimen.space_16)
    val largeSpacing = dimensionResource(R.dimen.space_32)
    val opponents = session.players.filter { it.id != session.selfId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = dimensionResource(R.dimen.space_16),
                end = dimensionResource(R.dimen.space_16), top = largeSpacing)
    ) {
        LobbyTopBar(session.connection)
        Spacer(Modifier.height(dimensionResource(R.dimen.space_24)))

        Text(
            text = stringResource(R.string.lobby_title),
            color = colors.onBackground,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.space_8)))
        Text(
            text = stringResource(R.string.lobby_subtitle),
            color = muted,
            fontSize = 14.sp
        )
        session.selfId?.let { id ->
            Spacer(Modifier.height(dimensionResource(R.dimen.space_16)))
            SelfPlayerIdentity(
                name = session.players.firstOrNull { it.id == id }?.name
                    ?: stringResource(R.string.game_player_fallback, id)
            )
        }

        Spacer(Modifier.height(largeSpacing))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.lobby_players_online),
                color = colors.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            PlayerCountBadge(
                count = if (session.connection == ConnectionStatus.CONNECTED) opponents.size else null
            )
        }
        Spacer(Modifier.height(spacing))

        Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
            when {
                session.connection == ConnectionStatus.CONNECTING -> LobbyMessage(
                    title = stringResource(R.string.board_connecting),
                    description = stringResource(R.string.lobby_waiting_connection)
                )
                session.connection == ConnectionStatus.ERROR -> LobbyMessage(
                    title = stringResource(R.string.board_connection_failed),
                    description = stringResource(R.string.lobby_connection_timeout)
                )
                opponents.isEmpty() -> LobbyMessage(
                    title = stringResource(R.string.lobby_no_other_player),
                    description = stringResource(R.string.lobby_connect_second_phone)
                )
                else -> LobbyPlayers(opponents, onPlayerClick)
            }
        }
    }
}

@Composable
private fun SelfPlayerIdentity(name: String) {
    val accent = MaterialTheme.colors.primary
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(dimensionResource(R.dimen.space_32))
                .clip(CircleShape)
                .background(accent)
        )
        Spacer(Modifier.width(dimensionResource(R.dimen.space_8)))
        Column {
            Text(
                text = stringResource(R.string.lobby_self_label),
                color = colorResource(R.color.board_text_muted),
                fontSize = 12.sp
            )
            Text(
                text = name,
                color = MaterialTheme.colors.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun LobbyTopBar(connection: ConnectionStatus) {
    val colors = MaterialTheme.colors
    val statusColor = when (connection) {
        ConnectionStatus.CONNECTED -> colors.secondary
        ConnectionStatus.CONNECTING -> colorResource(R.color.board_text_muted)
        ConnectionStatus.ERROR -> colors.error
    }
    val label = when (connection) {
        ConnectionStatus.CONNECTED -> R.string.lobby_status_connected
        ConnectionStatus.CONNECTING -> R.string.lobby_status_connecting
        ConnectionStatus.ERROR -> R.string.lobby_status_error
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .clip(CircleShape)
                .background(statusColor.copy(alpha = 0.12f))
                .border(Dp.Hairline, statusColor.copy(alpha = 0.25f), CircleShape)
                .padding(horizontal = dimensionResource(R.dimen.space_16),
                    vertical = dimensionResource(R.dimen.space_8)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(dimensionResource(R.dimen.space_8))
                    .clip(CircleShape)
                    .background(statusColor)
            )
            Spacer(Modifier.width(dimensionResource(R.dimen.space_8)))
            Text(
                text = stringResource(label),
                color = statusColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

    }
}

@Composable
private fun PlayerCountBadge(count: Int?) {
    val accent = MaterialTheme.colors.primary
    Text(
        text = if (count == null) stringResource(R.string.lobby_count_unknown)
            else stringResource(R.string.lobby_players_count, count),
        color = accent,
        fontSize = 12.sp,
        modifier = Modifier
            .clip(CircleShape)
            .background(accent.copy(alpha = 0.12f))
            .padding(horizontal = dimensionResource(R.dimen.space_16),
                vertical = dimensionResource(R.dimen.space_8))
    )
}

@Composable
private fun LobbyPlayers(
    players: List<BoardPlayer>,
    onPlayerClick: (BoardPlayer) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = dimensionResource(R.dimen.space_32)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_8))
    ) {
        items(players, key = { it.id }) { player ->
            LobbyPlayerCard(player, onPlayerClick)
        }
        item {
            Row(
                modifier = Modifier.padding(top = dimensionResource(R.dimen.space_16)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    tint = colorResource(R.color.board_text_muted),
                    modifier = Modifier.size(dimensionResource(R.dimen.space_16))
                )
                Spacer(Modifier.width(dimensionResource(R.dimen.space_8)))
                Text(
                    text = stringResource(R.string.lobby_auto_refresh),
                    color = colorResource(R.color.board_text_muted),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun LobbyPlayerCard(
    player: BoardPlayer,
    onPlayerClick: (BoardPlayer) -> Unit
) {
    val colors = MaterialTheme.colors
    val muted = colorResource(R.color.board_text_muted)
    val avatarColor = colorResource(
        when ((player.id and Int.MAX_VALUE) % 3) {
            0 -> R.color.lobby_avatar_violet
            1 -> R.color.lobby_avatar_orange
            else -> R.color.lobby_avatar_blue
        }
    )
    val shape = RoundedCornerShape(dimensionResource(R.dimen.corner_16))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = dimensionResource(R.dimen.lobby_player_card_min_height))
            .clip(shape)
            .background(colors.surface)
            .border(Dp.Hairline, colors.onSurface.copy(alpha = 0.07f), shape)
            .padding(horizontal = dimensionResource(R.dimen.space_16),
                vertical = dimensionResource(R.dimen.space_8)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(dimensionResource(R.dimen.lobby_avatar_size))
                .clip(shape)
                .background(avatarColor.copy(alpha = 0.20f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_panda),
                contentDescription = null,
                tint = avatarColor,
                modifier = Modifier.size(dimensionResource(R.dimen.lobby_avatar_icon_size))
            )
        }
        Spacer(Modifier.width(dimensionResource(R.dimen.space_8)))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = player.name,
                color = colors.onSurface,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.space_8)))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(dimensionResource(R.dimen.space_8))
                        .clip(CircleShape)
                        .background(colors.secondary)
                )
                Spacer(Modifier.width(dimensionResource(R.dimen.space_8)))
                Text(stringResource(R.string.lobby_player_online), color = muted, fontSize = 11.sp)
            }
        }
        Spacer(Modifier.width(dimensionResource(R.dimen.space_8)))
        Button(
            onClick = { onPlayerClick(player) },
            modifier = Modifier.height(dimensionResource(R.dimen.lobby_play_button_height))
                .widthIn(min = dimensionResource(R.dimen.lobby_play_button_min_width)),
            shape = shape,
            border = BorderStroke(Dp.Hairline, colors.primary.copy(alpha = 0.45f)),
            colors = ButtonDefaults.buttonColors(backgroundColor = colors.primary.copy(alpha = 0.12f)),
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp,
                pressedElevation = 0.dp),
            contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.space_8))
        ) {
            Text(
                text = stringResource(R.string.action_play),
                color = colors.primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun LobbyMessage(title: String, description: String) {
    val colors = MaterialTheme.colors
    Column(
        modifier = Modifier.fillMaxSize().padding(bottom = dimensionResource(R.dimen.space_32)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            color = colors.onBackground,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.space_8)))
        Text(
            text = description,
            color = colorResource(R.color.board_text_muted),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.space_16))
        )
    }
}