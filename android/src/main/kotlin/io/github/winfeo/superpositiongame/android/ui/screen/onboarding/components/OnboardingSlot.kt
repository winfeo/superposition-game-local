package io.github.winfeo.superpositiongame.android.ui.screen.onboarding.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.android.ui.screen.onboarding.OnboardingDiceState

@Composable
fun OnboardingSlot(
    diceState: OnboardingDiceState,
    modifier: Modifier = Modifier,
    highlighted: Boolean = false,
    appliedCard: Boolean = false,
    scale: Float = 1f,
    onSlotBoundsChanged: ((Rect) -> Unit)? = null
) {
    val transition = rememberInfiniteTransition(label = "onboarding-slot-pulse")
    val pulseAlpha = transition.animateFloat(
        initialValue = 0.55f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 520),
            repeatMode = RepeatMode.Reverse
        ),
        label = "onboarding-slot-border-alpha"
    ).value

    val borderColor = when {
        appliedCard -> Color(0xFF5BD9A4)
        highlighted -> Color(0xFF64D8EB).copy(alpha = pulseAlpha)
        else -> Color.White.copy(alpha = 0.46f)
    }

    Box(
        modifier = modifier
            .padding(top = 30.dp * scale)
            .size(width = 110.dp * scale, height = 169.dp * scale)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .then(
                    if (onSlotBoundsChanged == null) {
                        Modifier
                    } else {
                        Modifier.onGloballyPositioned { coordinates ->
                            onSlotBoundsChanged(coordinates.boundsInRoot())
                        }
                    }
                )
                .clip(RoundedCornerShape(12.dp * scale))
                .background(Color.Black.copy(alpha = 0.3f))
                .border(
                    width = (if (highlighted) 3.dp else 2.dp) * scale,
                    color = borderColor,
                    shape = RoundedCornerShape(12.dp * scale)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (appliedCard) {
                Image(
                    painter = painterResource(R.drawable.hadamard),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.matchParentSize()
                )
            } else {
                Text(
                    text = stringResource(R.string.onboarding_slot_label).uppercase(),
                    color = OnboardingMutedText,
                    style = MaterialTheme.typography.caption.copy(
                        fontSize = MaterialTheme.typography.caption.fontSize * scale
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Image(
            painter = painterResource(diceResource(diceState)),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(61.dp * scale)
                .align(Alignment.TopStart)
                .offset(x = (-30).dp * scale, y = (-30).dp * scale)
        )
    }
}

private fun diceResource(state: OnboardingDiceState): Int {
    return when (state) {
        OnboardingDiceState.ZERO -> R.drawable.zero
        OnboardingDiceState.PLUS -> R.drawable.plus
    }
}
