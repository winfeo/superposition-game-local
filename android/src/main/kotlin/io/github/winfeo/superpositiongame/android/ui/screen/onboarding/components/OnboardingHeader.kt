package io.github.winfeo.superpositiongame.android.ui.screen.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.android.ui.screen.onboarding.OnboardingStep

@Composable
fun OnboardingHeader(
    step: OnboardingStep,
    onBack: () -> Unit,
    onSkip: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onBack,
                enabled = step != OnboardingStep.WELCOME,
                modifier = Modifier.width(88.dp)
            ) {
                Text(
                    text =
                        if (step == OnboardingStep.WELCOME) { "" }
                        else { stringResource(R.string.onboarding_back_with_arrow) },
                    color = OnboardingAccentLight,
                    style = MaterialTheme.typography.caption
                )
            }

            Text(
                text = stringResource(
                    R.string.onboarding_step_counter,
                    step.ordinal + 1,
                    OnboardingStep.total
                ),
                color = OnboardingMutedText,
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )

            TextButton(
                onClick = onSkip,
                modifier = Modifier.width(88.dp)
            ) {
                Text(
                    text = stringResource(R.string.onboarding_skip),
                    color = OnboardingMutedText,
                    style = MaterialTheme.typography.caption
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            OnboardingStep.entries.forEach { progressStep ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(
                            if (progressStep.ordinal <= step.ordinal) { OnboardingAccent }
                            else { Color.White.copy(alpha = 0.14f) }
                        )
                )
            }
        }
    }
}
