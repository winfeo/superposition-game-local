package io.github.winfeo.superpositiongame.android.ui.screen.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
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
import io.github.winfeo.superpositiongame.android.domain.game.model.BoardSession
import io.github.winfeo.superpositiongame.android.domain.game.model.ConnectionStatus
import java.util.Locale

@Composable
fun PlayerInfoPanel(
    session: BoardSession,
    timerSeconds: Int? = null,
    onMenuClick: () -> Unit
) {
    val game = session.game ?: return
    val selfId = session.selfId ?: return
    val opponentId = game.playerIds.firstOrNull { it != selfId } ?: return
    val selfName = session.players.firstOrNull { it.id == selfId }?.name
        ?.takeIf(String::isNotBlank)
        ?: stringResource(R.string.game_player_fallback, selfId)
    val opponentName = session.players.firstOrNull { it.id == opponentId }?.name
        ?.takeIf(String::isNotBlank)
        ?: stringResource(R.string.game_player_fallback, opponentId)
    val spacing = dimensionResource(R.dimen.space_16)

    Column(
        modifier = Modifier.fillMaxWidth().padding(spacing),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(bottom = spacing),
            contentAlignment = Alignment.TopEnd
        ) {
            GameMenuButton(
                enabled = session.gameStarted && session.connection == ConnectionStatus.CONNECTED,
                onClick = onMenuClick
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PlayerCard(
                name = selfName,
                isSelf = true,
                isCurrentTurn = game.currentPlayerId == selfId,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.size(spacing))
            GameTimer(timerSeconds, modifier = Modifier.weight(0.8f))
            Spacer(Modifier.size(spacing))
            PlayerCard(
                name = opponentName,
                isSelf = false,
                isCurrentTurn = game.currentPlayerId == opponentId,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(spacing))
        TaskInfo(game.targetRegister)
    }
}

@Composable
private fun PlayerCard(
    name: String,
    isSelf: Boolean,
    isCurrentTurn: Boolean,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colors
    val shape = RoundedCornerShape(dimensionResource(R.dimen.hud_card_corner))
    val glow = if (isCurrentTurn) colors.primary.copy(alpha = 0.25f)
        else colors.onSurface.copy(alpha = 0.04f)
    val border = if (isCurrentTurn) colors.primary.copy(alpha = 0.35f)
        else colors.onSurface.copy(alpha = 0.06f)

    Box(
        modifier = modifier
            .aspectRatio(0.9f)
            .padding(4.dp)
    ) {
        Box(
            Modifier
                .matchParentSize()
                .clip(shape)
                .blur(dimensionResource(R.dimen.hud_glow_radius))
                .background(glow, shape)
        )
        Column(
            modifier = Modifier
                .matchParentSize()
                .clip(shape)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            colors.onSurface.copy(alpha = 0.10f),
                            colors.onSurface.copy(alpha = 0.04f)
                        )
                    )
                )
                .border(Dp.Hairline, border, shape)
                .padding(dimensionResource(R.dimen.space_8)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PlayerAvatar(isCurrentTurn)
            Spacer(Modifier.height(dimensionResource(R.dimen.space_8)))
            Text(
                text = if (isSelf) stringResource(R.string.game_player_self, name) else name,
                color = colors.onSurface.copy(alpha = 0.9f),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.space_8)))
            Text(
                text = stringResource(
                    if (isCurrentTurn) R.string.panel_turn_tag_active
                    else R.string.panel_turn_tag_wait
                ).uppercase(),
                color = if (isCurrentTurn) colors.primary else colors.onSurface.copy(alpha = 0.35f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun PlayerAvatar(isCurrentTurn: Boolean) {
    val colors = MaterialTheme.colors
    val glow = if (isCurrentTurn) colors.primary.copy(alpha = 0.35f)
        else colors.onSurface.copy(alpha = 0.06f)

    Box(
        modifier = Modifier.size(dimensionResource(R.dimen.hud_avatar_size)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .matchParentSize()
                .clip(CircleShape)
                .blur(dimensionResource(R.dimen.hud_glow_radius))
                .background(glow, CircleShape)
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            colors.onSurface.copy(alpha = 0.25f),
                            colors.onSurface.copy(alpha = 0.05f)
                        )
                    )
                )
                .border(Dp.Hairline, colors.onSurface.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_panda),
                contentDescription = null,
                modifier = Modifier.size(dimensionResource(R.dimen.space_24)),
                colorFilter = ColorFilter.tint(colors.onSurface.copy(alpha = 0.9f))
            )
        }
    }
}

@Composable
private fun GameTimer(timerSeconds: Int?, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colors
    val time = timerSeconds?.let {
        String.format(Locale.US, "%02d:%02d", it / 60, it % 60)
    } ?: stringResource(R.string.game_timer_unavailable)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = time,
            color = if (timerSeconds == null) colors.onSurface.copy(alpha = 0.55f)
                else colors.onSurface,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.space_8)))
        Box(
            Modifier
                .fillMaxWidth(0.75f)
                .height(dimensionResource(R.dimen.hud_timer_track_height))
                .clip(CircleShape)
                .background(colors.onSurface.copy(alpha = 0.1f))
        ) {
            if (timerSeconds != null) {
                Box(
                    Modifier
                        .fillMaxWidth((timerSeconds / 45f).coerceIn(0f, 1f))
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(colors.primary)
                )
            }
        }
    }
}

@Composable
private fun TaskInfo(faces: List<String>) {
    val colors = MaterialTheme.colors
    val shape = CircleShape
    Box(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .clip(shape)
            .background(colors.onSurface.copy(alpha = 0.06f))
            .border(Dp.Hairline, colors.onSurface.copy(alpha = 0.08f), shape)
            .padding(horizontal = dimensionResource(R.dimen.space_16),
                vertical = dimensionResource(R.dimen.space_8)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = faces.take(4).joinToString("    ") { it.uppercase() },
            color = colors.onSurface.copy(alpha = 0.8f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1
        )
    }
}

@Composable
private fun GameMenuButton(enabled: Boolean, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val indication = LocalIndication.current
    val colors = MaterialTheme.colors
    Box(
        modifier = Modifier
            .size(dimensionResource(R.dimen.hud_menu_size))
            .clip(CircleShape)
            .background(colors.onSurface.copy(alpha = 0.06f))
            .border(Dp.Hairline, colors.onSurface.copy(alpha = 0.08f), CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = indication,
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = stringResource(R.string.game_menu_title),
            tint = colors.onSurface.copy(alpha = if (enabled) 0.85f else 0.35f),
            modifier = Modifier.size(dimensionResource(R.dimen.space_24))
        )
    }
}