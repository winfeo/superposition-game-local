package io.github.winfeo.superpositiongame.android.ui.screen.onboarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.winfeo.superpositiongame.R

@Composable
fun OnboardingAppSections() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AppSection(
            icon = R.drawable.ic_gamepad,
            label = stringResource(R.string.onboarding_section_lobby),
            modifier = Modifier.weight(1f)
        )
        AppSection(
            icon = R.drawable.ic_bot,
            label = stringResource(R.string.onboarding_section_ai),
            modifier = Modifier.weight(1f)
        )
        AppSection(
            icon = R.drawable.ic_graduation_cap,
            label = stringResource(R.string.onboarding_section_library),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun AppSection(
    icon: Int,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color.White.copy(alpha = 0.055f), RoundedCornerShape(16.dp))
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = label,
            color = OnboardingText,
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.Center
        )
    }
}
