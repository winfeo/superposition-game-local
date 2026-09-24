package io.github.winfeo.superpositiongame.android.ui.screen.onboarding.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import kotlinx.coroutines.delay

private const val CHARACTER_DELAY_MILLIS = 24L

@Composable
fun OnboardingTypewriterText(
    text: String,
    revealImmediately: Boolean,
    onTypingChanged: (Boolean) -> Unit,
    color: Color,
    style: TextStyle,
    modifier: Modifier = Modifier
) {
    var visibleCharacters by remember(text) { mutableIntStateOf(0) }
    val currentOnTypingChanged by rememberUpdatedState(onTypingChanged)

    LaunchedEffect(text, revealImmediately) {
        if (revealImmediately) {
            visibleCharacters = text.length
            currentOnTypingChanged(false)
            return@LaunchedEffect
        }

        visibleCharacters = 0
        currentOnTypingChanged(true)

        text.forEachIndexed { index, character ->
            delay(CHARACTER_DELAY_MILLIS)
            visibleCharacters = index + 1

            val punctuationPause = punctuationPauseMillis(character)
            if (punctuationPause > 0L) {
                delay(punctuationPause)
            }
        }

        currentOnTypingChanged(false)
    }

    val displayedText = buildAnnotatedString {
        append(text)
        if (visibleCharacters < text.length) {
            addStyle(
                style = SpanStyle(color = Color.Transparent),
                start = visibleCharacters,
                end = text.length
            )
        }
    }

    Text(
        text = displayedText,
        color = color,
        style = style,
        modifier = modifier
    )
}

private fun punctuationPauseMillis(character: Char): Long {
    return when (character) {
        '.', '!', '?' -> 90L
        ',', ':', ';', '—' -> 40L
        else -> 0L
    }
}
