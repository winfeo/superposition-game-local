package io.github.winfeo.superpositiongame.android.ui.screen.onboarding.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.winfeo.superpositiongame.R

@Composable
fun OnboardingSpeechBubble(
    text: String,
    showTapHint: Boolean,
    revealTextImmediately: Boolean,
    onTypingChanged: (Boolean) -> Unit
) {
    val mascotTransition = rememberInfiniteTransition(label = "onboarding-mascot-float")
    val mascotTravelPx = with(LocalDensity.current) { 3.dp.toPx() }
    val mascotOffset = mascotTransition.animateFloat(
        initialValue = -mascotTravelPx,
        targetValue = mascotTravelPx,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1_800,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "onboarding-mascot-offset"
    ).value

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(52.dp)
                    .graphicsLayer { translationY = mascotOffset }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = Color(0xFF24253A),
                        shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color(0xFF4E557A),
                        shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                OnboardingTypewriterText(
                    text = text,
                    revealImmediately = revealTextImmediately,
                    onTypingChanged = onTypingChanged,
                    color = OnboardingText,
                    style = MaterialTheme.typography.body2
                )
            }
        }

        OnboardingTapHint(visible = showTapHint)
    }
}

@Composable
private fun OnboardingTapHint(visible: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(26.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(durationMillis = 500)),
            exit = fadeOut(animationSpec = tween(durationMillis = 250))
        ) {
            val transition = rememberInfiniteTransition(label = "onboarding-tap-hint")
            val hintAlpha = transition.animateFloat(
                initialValue = 0.45f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1_100),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "onboarding-tap-hint-alpha"
            ).value

            Text(
                text = stringResource(R.string.onboarding_tap_to_continue),
                color = OnboardingAccentLight.copy(alpha = hintAlpha),
                style = MaterialTheme.typography.caption
            )
        }
    }
}
