package io.github.winfeo.superpositiongame.android.ui.screen.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.winfeo.superpositiongame.R

@Composable
fun OnboardingTaskChip(text: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.58f)
            .height(54.dp)
            .background(Color.White.copy(alpha = 0.07f), RoundedCornerShape(50))
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(50))
            .padding(horizontal = 16.dp, vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.onboarding_task_label).uppercase(),
            color = OnboardingMutedText,
            style = MaterialTheme.typography.overline
        )
        Text(
            text = text,
            color = OnboardingText,
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Medium
        )
    }
}
