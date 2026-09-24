package io.github.winfeo.superpositiongame.android.ui.screen.onboarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.github.winfeo.superpositiongame.R

@Composable
fun OnboardingCard(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    scale: Float = 1f
) {
    Image(
        painter = painterResource(R.drawable.hadamard),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = modifier
            .size(width = 82.dp * scale, height = 128.dp * scale)
            .clip(RoundedCornerShape(9.dp * scale))
            .then(
                if (selected) {
                    Modifier.border(
                        3.dp * scale,
                        OnboardingAccentLight,
                        RoundedCornerShape(9.dp * scale)
                    )
                } else { Modifier }
            )
    )
}
