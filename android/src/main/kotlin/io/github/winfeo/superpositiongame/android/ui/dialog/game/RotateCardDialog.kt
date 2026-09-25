package io.github.winfeo.superpositiongame.android.ui.dialog.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.android.ui.dialog.BoardDialogSurface
import io.github.winfeo.superpositiongame.model.dice.DiceState

@Composable
fun RotateCardDialog(
    availableStates: List<DiceState>,
    onStateSelected: (DiceState) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val indication = LocalIndication.current
    val colors = MaterialTheme.colors
    val space8 = dimensionResource(R.dimen.space_8)
    val shape = RoundedCornerShape(space8)

    BoardDialogSurface(onDismissRequest = {}, dismissable = false) {
        Column(verticalArrangement = Arrangement.spacedBy(space8)) {
            availableStates.chunked(3).forEach { rowStates ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(space8)
                ) {
                    rowStates.forEach { state ->
                        Image(
                            painter = painterResource(stateDrawable(state)),
                            contentDescription = stringResource(R.string.dialog_dice_state, state.name),
                            modifier = Modifier.weight(1f).aspectRatio(1f)
                                .clip(shape)
                                .background(colors.onSurface.copy(alpha = 0.08f))
                                .border(Dp.Hairline, colors.onSurface.copy(alpha = 0.20f), shape)
                                .clickable(interactionSource = interactionSource, indication = indication) { onStateSelected(state) }
                                .padding(space8)
                        )
                    }
                    repeat(3 - rowStates.size) { Spacer(Modifier.weight(1f)) }
                }
            }
        }
        Text(
            text = stringResource(R.string.dialog_rotate_hint),
            color = colors.onSurface.copy(alpha = 0.5f),
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun stateDrawable(state: DiceState): Int = when (state) {
    DiceState.ZERO -> R.drawable.zero
    DiceState.ONE -> R.drawable.one
    DiceState.PLUS -> R.drawable.plus
    DiceState.MINUS -> R.drawable.minus
    DiceState.I -> R.drawable.i_plus
    DiceState.I_MINUS -> R.drawable.i_minus
}
