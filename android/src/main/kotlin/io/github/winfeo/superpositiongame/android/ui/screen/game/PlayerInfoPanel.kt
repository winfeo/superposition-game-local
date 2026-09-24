package io.github.winfeo.superpositiongame.android.ui.screen.game

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.card.CardRepository
import io.github.winfeo.superpositiongame.model.dice.Dice
import io.github.winfeo.superpositiongame.model.dice.DiceState
import io.github.winfeo.superpositiongame.model.game.GamePhase
import io.github.winfeo.superpositiongame.model.game.GameState
import io.github.winfeo.superpositiongame.model.game.PlayerState
import io.github.winfeo.superpositiongame.model.game.SlotOwner
import io.github.winfeo.superpositiongame.model.game.SlotState
import java.util.Locale

@Composable
fun PlayerInfoPanel(
    gameState: GameState,
    playerId: String,
    timerSeconds: Int,
    onPause: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
//            .fillMaxHeight(0.33f)
//            .background(Color(0xFF0C0813))
    ) {
//        BackgroundBlur()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 16.dp
                )
        ) {
            //Меню паузы
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                PauseButton(onClick = onPause)
            }

            val player = gameState.players[playerId]
            val playerName = player?.nickname?: playerId

            val opponentId = gameState.players.keys.first { it != playerId }
            val opponent = gameState.players[opponentId]
            val opponentName = opponent?.nickname?: opponentId

            val gameTask = buildTaskString(player)

            GameHud(
                playerName = playerName,
                opponentName = opponentName,
                isPlayerTurn = gameState.currentPlayerId == playerId,
                timerSeconds = timerSeconds,
                gameTask = gameTask
            )
        }
    }
}

private fun buildTaskString(playerState: PlayerState?): String {
    if (playerState == null) return ""

    return playerState.slots.joinToString("    ") { slot ->
        slot.dice.requiredState?.stateName?: "?"
    }
}

/* ---------------- Игровой худ ---------------- */
@Composable
fun GameHud(
    playerName: String,
    opponentName: String,
    isPlayerTurn: Boolean,
    timerSeconds: Int,
    gameTask: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PlayerCard(
                name = playerName.take(9),
                isCurrentPlayerCard = true,
                isCurrentTurn = isPlayerTurn,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Timer(
                timerSeconds = timerSeconds,
                modifier = Modifier.weight(0.8f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            PlayerCard(
                name = opponentName.take(9),
                isCurrentPlayerCard = false,
                isCurrentTurn = !isPlayerTurn,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TaskInfo(
            text = gameTask
        )
    }
}

/* ---------------- Карточка  игрока ---------------- */
@Composable
fun PlayerCard(
    name: String,
    isCurrentPlayerCard: Boolean,
    isCurrentTurn: Boolean,
    modifier: Modifier = Modifier
) {
    val glow = if (isCurrentTurn) Color(0xFF6C8CFF).copy(alpha = 0.25f)
    else Color.White.copy(alpha = 0.04f)

    val border = if (isCurrentTurn) Color(0xFF6C8CFF).copy(alpha = 0.35f)
    else Color.White.copy(alpha = 0.06f)

    val playerTag = if (isCurrentPlayerCard) "(${stringResource(R.string.panel_player_tag)})" else ""

    Box(
        modifier = modifier
            .aspectRatio(0.9f)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(22.dp))
                .blur(18.dp)
                .background(glow, RoundedCornerShape(22.dp))
        )

        Column(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(22.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.White.copy(alpha = 0.1f),
                            Color.White.copy(alpha = 0.04f)
                        )
                    )
                )
                .border(1.dp, border, RoundedCornerShape(22.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Avatar(
                isCurrentTurn = isCurrentTurn,
                modifier = Modifier.fillMaxWidth(0.4f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$name $playerTag",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            val text =
                if (isCurrentTurn) { stringResource(R.string.panel_turn_tag_active).uppercase() }
                else { stringResource(R.string.panel_turn_tag_wait).uppercase() }
            val color =
                if (isCurrentTurn) Color(0xFF9DB2FF)
                else Color.White.copy(alpha = 0.35f)

            Text(
                text = text,
                color = color,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp
            )
        }
    }
}

/* ---------------- Аватар ---------------- */
@Composable
fun Avatar(
    isCurrentTurn: Boolean,
    modifier: Modifier = Modifier
) {
    val glow = if (isCurrentTurn) Color(0xFF6C8CFF).copy(alpha = 0.35f)
    else Color.White.copy(alpha = 0.06f)

    Box(
        modifier = modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(CircleShape)
                .blur(16.dp)
                .background(glow, CircleShape)
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            Color.White.copy(alpha = 0.25f),
                            Color.White.copy(alpha = 0.05f)
                        )
                    )
                )
                .border(1.dp, Color.White.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_panda),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(0.5f),
                colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.9f))
            )
        }
    }
}

/* ---------------- Таймер ---------------- */
@Composable
fun Timer(
    timerSeconds: Int,
    modifier: Modifier = Modifier
) {
    val minutes = timerSeconds / 60
    val seconds = timerSeconds % 60
    val time = String.format(Locale.US, "%02d:%02d", minutes, seconds)

    val totalSeconds = 45
    val progress = (timerSeconds.toFloat() / totalSeconds).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = 500,
            easing = LinearEasing
        )
    )

    val barColor = when {
        timerSeconds <= 10 -> Color(0xFFFF6B6B)
//        timerSeconds <= 10 -> Color(0xFFFFD93D)
        else -> Color(0xFF6C8CFF)
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = time,
            color = if (timerSeconds <= 10) Color(0xFFFF6B6B) else Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color.White.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress)
                    .clip(RoundedCornerShape(2.dp))
                    .background(barColor)  // сплошной цвет
            )
        }

//        Box(
//            modifier = Modifier
//                .padding(top = 4.dp)
//                .fillMaxWidth(0.6f)
//                .height(3.dp)
//                .background(
//                    color = if (timerSeconds <= 10) Color(0xFFFF6B6B) else Color(0xFF6C8CFF),
//                    shape = RoundedCornerShape(50.dp)
//                )
//        )
    }
}

/* ---------------- Задание ---------------- */
@Composable
fun TaskInfo(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .background(
                Color.White.copy(alpha = 0.06f),
                RoundedCornerShape(50.dp)
            )
            .border(
                1.dp,
                Color.White.copy(alpha = 0.08f),
                RoundedCornerShape(50.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

/* ---------------- Кнопка паузы ---------------- */
@Composable
fun PauseButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.06f))
            .border(1.dp, Color.White.copy(alpha = 0.08f), CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = Icons.Default.Menu,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.85f)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GameScreenPreview() {
    val gameState = GameState(
        phase = GamePhase.MOVE_START,
        currentPlayerId = "player_2",
        players = mapOf(
            "player_1" to PlayerState(
                id = "123",
                nickname = null,
                hand = listOf(
                    Card(
                        id = "card_1",
                        textureId = "pauli_x",
                        description = CardRepository.getDescription("pauli_x")!!
                    ),
                    Card(
                        id = "card_2",
                        textureId = "rotate_z",
                        description = CardRepository.getDescription("rotate_z")!!
                    ),
                    Card(
                        id = "card_3",
                        textureId = "hadamard_h",
                        description = CardRepository.getDescription("hadamard_h")!!
                    ),
                    Card(
                        id = "card_4",
                        textureId = "measurement",
                        description = CardRepository.getDescription("measurement")!!
                    ),
                    Card(
                        id = "card_5",
                        textureId = "swap",
                        description = CardRepository.getDescription("swap")!!
                    ),
                    Card(
                        id = "card_6",
                        textureId = "reshuffle",
                        description = CardRepository.getDescription("reshuffle")!!
                    )
                ),
                slots = listOf(
                    SlotState(
                        index = 0,
                        slotOwner = SlotOwner.PLAYER,
                        initialDice = Dice(
                            id = "dice_1",
                            state = DiceState.MINUS,
                            requiredState = DiceState.PLUS
                        ),
                        dice = Dice(
                            id = "dice_1",
                            state = DiceState.MINUS,
                            requiredState = DiceState.PLUS
                        )
                    ),
                    SlotState(
                        index = 1,
                        slotOwner = SlotOwner.PLAYER,
                        initialDice = Dice(
                            id = "dice_2",
                            state = DiceState.ONE,
                            requiredState = DiceState.PLUS
                        ),
                        dice = Dice(
                            id = "dice_2",
                            state = DiceState.PLUS,
                            requiredState = DiceState.PLUS
                        ),
                        appliedCards = listOf(
                            Card(
                                id = "applied_1",
                                textureId = "pauli_x",
                                description = CardRepository.getDescription("pauli_x")!!
                            )
                        )
                    ),
                    SlotState(
                        index = 2,
                        slotOwner = SlotOwner.OPPONENT,
                        initialDice = Dice(
                            id = "dice_3",
                            state = DiceState.PLUS,
                            requiredState = DiceState.PLUS
                        ),
                        dice = Dice(
                            id = "dice_3",
                            state = DiceState.PLUS,
                            requiredState = DiceState.PLUS
                        )
                    ),
                    SlotState(
                        index = 3,
                        slotOwner = SlotOwner.OPPONENT,
                        initialDice = Dice(
                            id = "dice_4",
                            state = DiceState.ZERO,
                            requiredState = DiceState.PLUS
                        ),
                        dice = Dice(
                            id = "dice_4",
                            state = DiceState.ZERO,
                            requiredState = DiceState.PLUS
                        )
                    )
                ),
                skipNextTurn = false,
                remainingMoves = 1
            ),
            "player_2" to PlayerState(
                id = "player_2",
                nickname = "Winfeo",
                hand = listOf(
                    Card(
                        id = "card_7",
                        textureId = "identity",
                        description = CardRepository.getDescription("identity")!!
                    )
                ),
                slots = listOf(
                    SlotState(
                        index = 0,
                        slotOwner = SlotOwner.PLAYER,
                        initialDice = Dice(
                            id = "dice_5",
                            state = DiceState.I,
                            requiredState = DiceState.PLUS
                        ),
                        dice = Dice(
                            id = "dice_5",
                            state = DiceState.I,
                            requiredState = DiceState.PLUS
                        )
                    ),
                    SlotState(
                        index = 1,
                        slotOwner = SlotOwner.PLAYER,
                        initialDice = Dice(
                            id = "dice_6",
                            state = DiceState.MINUS,
                            requiredState = DiceState.PLUS
                        ),
                        dice = Dice(
                            id = "dice_6",
                            state = DiceState.PLUS,
                            requiredState = DiceState.PLUS
                        ),
                        appliedCards = listOf(
                            Card(
                                id = "applied_2",
                                textureId = "rotate_y",
                                description = CardRepository.getDescription("rotate_y")!!
                            ),
                            Card(
                                id = "applied_3",
                                textureId = "hadamard_h",
                                description = CardRepository.getDescription("hadamard_h")!!
                            )
                        )
                    ),
                    SlotState(
                        index = 2,
                        slotOwner = SlotOwner.OPPONENT,
                        initialDice = Dice(
                            id = "dice_7",
                            state = DiceState.ONE,
                            requiredState = DiceState.PLUS
                        ),
                        dice = Dice(
                            id = "dice_7",
                            state = DiceState.ONE,
                            requiredState = DiceState.PLUS
                        )
                    ),
                    SlotState(
                        index = 3,
                        slotOwner = SlotOwner.OPPONENT,
                        initialDice = Dice(
                            id = "dice_8",
                            state = DiceState.I_MINUS,
                            requiredState = DiceState.PLUS
                        ),
                        dice = Dice(
                            id = "dice_8",
                            state = DiceState.I_MINUS,
                            requiredState = DiceState.PLUS
                        )
                    )
                ),
                skipNextTurn = false,
                remainingMoves = 2
            )
        ),
        turnNumber = 3,
        activeSlotsRow = SlotOwner.PLAYER,
        winnerId = null,
        serverTime = 0L,
        turnEndsAt = 0L
    )

    PlayerInfoPanel(
        gameState = gameState,
        playerId = "player_1",
        timerSeconds = 45,
        onPause = {}
    )
}
