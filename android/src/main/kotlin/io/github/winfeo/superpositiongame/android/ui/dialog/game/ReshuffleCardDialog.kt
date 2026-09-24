package io.github.winfeo.superpositiongame.android.ui.dialog.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.card.CardType
import io.github.winfeo.superpositiongame.model.card.description.AxisRotation
import io.github.winfeo.superpositiongame.model.card.description.CardDescription

@Composable
fun ReshuffleCardDialog(
    cards: List<Card>,
    maxSelectable: Int = 4,
    minSelectable: Int = 1,
    onCardsSelected: (List<Card>) -> Unit
) {
    var showDialog by remember { mutableStateOf(true) }
    var selectedCards by remember { mutableStateOf<Set<String>>(emptySet()) }

    if (showDialog) {
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
                    .wrapContentHeight()
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
                        color = Color.White.copy(0.08f),
                        shape = RoundedCornerShape(24.dp),
                        width = 1.dp
                    )
            ) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF6C8CFF).copy(alpha = 0.10f),
                                    Color.Transparent
                                ),
                                radius = 900f
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(24.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "${selectedCards.size} / $maxSelectable",
                        style = MaterialTheme.typography.body1,
                        color = Color(0xFF6C8CFF),
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 12.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(cards, key = { it.id }) { card ->
                            CardItem(
                                card = card,
                                isSelected = selectedCards.contains(card.id),
                                onCardClick = {
                                    selectedCards = if (selectedCards.contains(card.id)) {
                                        selectedCards - card.id
                                    } else {
                                        if (selectedCards.size < maxSelectable) {
                                            selectedCards + card.id
                                        } else {
                                            selectedCards
                                        }
                                    }
                                }
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .padding(top = 64.dp)
                            .fillMaxWidth()
                            .height(56.dp)
                            .then(
                                if (selectedCards.size in minSelectable..maxSelectable) {
                                    Modifier.background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(
                                                Color(0xFF4B5DFF),
                                                Color(0xFF6C8CFF)
                                            )
                                        ),
                                        shape = RoundedCornerShape(14.dp)
                                    )
                                } else {
                                    Modifier
                                        .background(
                                            color = Color.White.copy(alpha = 0.04f),
                                            shape = RoundedCornerShape(14.dp)
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = Color.White.copy(alpha = 0.08f),
                                            shape = RoundedCornerShape(14.dp)
                                        )
                                }
                            )
                            .clickable(
                                enabled = selectedCards.size in minSelectable..maxSelectable
                            ) {
                                val selected = cards.filter { it.id in selectedCards }
                                onCardsSelected(selected)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        val color =
                            if (selectedCards.size in minSelectable..maxSelectable) Color.White.copy(alpha = 0.92f)
                            else Color.White.copy(alpha = 0.35f)

                        Text(
                            text = stringResource(R.string.dialog_reshuffle_card_confirm),
                            color = color,
                            style = MaterialTheme.typography.body1
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = stringResource(R.string.dialog_reshuffle_card_info),
                            color = Color.White.copy(alpha = 0.38f),
                            style = MaterialTheme.typography.caption
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CardItem(
    card: Card,
    isSelected: Boolean,
    onCardClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = if (isSelected) {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF4B5DFF).copy(alpha = 0.22f),
                            Color(0xFF6C8CFF).copy(alpha = 0.12f)
                        )
                    )
                } else {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.08f),
                            Color.White.copy(alpha = 0.1f)
                        )
                    )
                },
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                color = if (isSelected) {
                    Color(0xFF6C8CFF).copy(alpha = 0.55f)
                } else {
                    Color.White.copy(alpha = 0.08f)
                },
                shape = RoundedCornerShape(8.dp),
                width = 1.dp
            )
            .clickable { onCardClick() }
            .padding(10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val cardImageId = getCardImageResource(card)
            Image(
                painter = painterResource(id = cardImageId),
                contentDescription = card.type.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.7f)
            )
        }
    }
}

//TODO переделать!
fun getCardImageResource(card: Card): Int {
    return when (card.type) {
        CardType.PAULI -> getPauliResource(card)
        CardType.ROTATE -> getRotateResource(card)
        CardType.PHASE -> getPhaseResource(card)
        CardType.HADAMARD -> getHadamardResource(card)
        CardType.SWAP -> R.drawable.swap
        CardType.QUANTUM_NOISE -> R.drawable.quantum_noise
        CardType.KRONECKER_MULTIPLICATION -> R.drawable.kronecker_multiplication
        CardType.MEASUREMENT -> R.drawable.measurement
        CardType.IDENTITY -> R.drawable.identity
        CardType.BARRIER -> R.drawable.barrier
        CardType.RESHUFFLE -> R.drawable.reshuffle
        CardType.QUANTUM_LUCKY -> R.drawable.quantum_lucky
    }
}

private fun getPauliResource(card: Card): Int {
    val axis = card.axis!!
    val radius = card.actionRadius

    return when (axis) {
        AxisRotation.X -> when (radius) {
            1 -> R.drawable.pauli_x
            else -> R.drawable.pauli_x3
        }
        AxisRotation.Y -> when (radius) {
            1 -> R.drawable.pauli_y
            else -> R.drawable.pauli_y3
        }
        AxisRotation.Z -> when (radius) {
            1 -> R.drawable.pauli_z
            else -> R.drawable.pauli_z3
        }
    }
}

private fun getRotateResource(card: Card): Int {
    val axis = card.axis!!

    return when (axis) {
        AxisRotation.X -> R.drawable.rotate_x
        AxisRotation.Y -> R.drawable.rotate_y
        AxisRotation.Z -> R.drawable.rotate_z
    }
}

private fun getPhaseResource(card: Card): Int {
    val isForward = card.isForwardRotation

    return when (isForward) {
        true -> R.drawable.phase_forward
        else -> R.drawable.phase_backward
    }
}

private fun getHadamardResource(card: Card): Int {
    val radius = card.actionRadius

    return when (radius) {
        1 -> R.drawable.hadamard
        else -> R.drawable.hadamard_3
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ReshuffleCardDialogPrev() {
    val testCards = listOf(
        Card(
            id = "1",
            textureId = null,
            description = object: CardDescription() {
                override val type = CardType.IDENTITY
                override val axis = null
                override val actionRadius = 0
                override val requiredSpecialSlot = false
                override val isForwardRotation = null
            }
        ),
        Card(
            id = "2",
            textureId = null,
            description = object : CardDescription() {
                override val type = CardType.SWAP
                override val axis = null
                override val actionRadius = 0
                override val requiredSpecialSlot = false
                override val isForwardRotation = null
            }
        ),
        Card(
            id = "3",
            textureId = null,
            description = object : CardDescription() {
                override val type = CardType.KRONECKER_MULTIPLICATION
                override val axis = null
                override val actionRadius = 0
                override val requiredSpecialSlot = false
                override val isForwardRotation = null
            }
        ),
        Card(
            id = "4",
            textureId = null,
            description = object : CardDescription() {
                override val type = CardType.KRONECKER_MULTIPLICATION
                override val axis = null
                override val actionRadius = 0
                override val requiredSpecialSlot = false
                override val isForwardRotation = null
            }
        )
    )

    MaterialTheme {
        ReshuffleCardDialog(
            cards = testCards,
            onCardsSelected = {}
        )
    }
}
