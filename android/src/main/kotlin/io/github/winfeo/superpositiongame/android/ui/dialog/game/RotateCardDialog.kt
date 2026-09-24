package io.github.winfeo.superpositiongame.android.ui.dialog.game

import io.github.winfeo.superpositiongame.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.winfeo.superpositiongame.model.dice.DiceState

@Composable
fun RotateCardDialog(
    availableStates: List<DiceState>,
    onStateSelected: (DiceState) -> Unit
) {
    var showDialog by remember { mutableStateOf(true) }

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
                        color = Color.White.copy(alpha = 0.08f),
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
                                    Color(0xFF6C8CFF).copy(alpha = 0.1f),
                                    Color.Transparent
                                ),
                                radius = 900f
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    DiceStatesGrid(
                        availableStates = availableStates,
                        onStateClick = { selectedState ->
                            onStateSelected(selectedState)
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = stringResource(R.string.dialog_rotate_card),
                            color = Color.White.copy(alpha = 0.4f),
                            style = MaterialTheme.typography.caption,
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DiceStatesGrid(
    availableStates: List<DiceState>,
    onStateClick: (DiceState) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        availableStates.chunked(3).forEach { rowStates ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowStates.forEach { state ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clickable { onStateClick(state) }
                            .border(
                                color = Color.White.copy(0.2f),
                                shape = RoundedCornerShape(8.dp),
                                width = 1.dp
                            )
                            .background(
                                color = Color.White.copy(0.1f),
                                shape = RoundedCornerShape(8.dp),
                            )
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val stateId = when (state) {
                            DiceState.ZERO -> R.drawable.zero
                            DiceState.ONE -> R.drawable.one
                            DiceState.PLUS -> R.drawable.plus
                            DiceState.MINUS -> R.drawable.minus
                            DiceState.I -> R.drawable.i_plus
                            DiceState.I_MINUS -> R.drawable.i_minus
                        }

                        Image(
                            painter = painterResource(id = stateId),
                            contentDescription = state.name,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
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
fun RotateCardDialogPrev() {
    RotateCardDialog(
        availableStates = listOf(DiceState.MINUS, DiceState.ZERO, DiceState.PLUS),
        onStateSelected = {}
    )
}
