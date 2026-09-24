package io.github.winfeo.superpositiongame.android.ui.screen.onboarding.components

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.android.ui.screen.onboarding.OnboardingTableFocus

private const val TABLE_IMAGE_ASPECT_RATIO = 1080f / 2424f
private const val TABLE_FOCUS_IMAGE_ASPECT_RATIO = 0.82f
private val TABLE_COUNTER_RESERVED_HEIGHT = 43.dp

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun OnboardingTableGuide(
    focus: OnboardingTableFocus,
    modifier: Modifier = Modifier
) {
    val image = tableImageFor(focus)
    val imageAlignment = when (focus) {
        OnboardingTableFocus.OVERVIEW -> Alignment.Center
        OnboardingTableFocus.PLAYERS_AND_TIMER,
        OnboardingTableFocus.OBJECTIVE -> Alignment.TopCenter
        OnboardingTableFocus.OPPONENT_SLOTS -> Alignment.Center
        OnboardingTableFocus.PLAYER_SLOTS,
        OnboardingTableFocus.HAND -> Alignment.BottomCenter
    }
    val imageAspectRatio = if (focus == OnboardingTableFocus.OVERVIEW) TABLE_IMAGE_ASPECT_RATIO else TABLE_FOCUS_IMAGE_ASPECT_RATIO

    BoxWithConstraints(
        modifier = modifier
            .widthIn(max = 430.dp)
            .fillMaxWidth()
    ) {
        val availableImageHeight = (maxHeight - TABLE_COUNTER_RESERVED_HEIGHT).coerceAtLeast(0.dp)
        val imageWidth = minOf(
            maxWidth,
            availableImageHeight * imageAspectRatio
        )
        val imageHeight = imageWidth / imageAspectRatio

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TableImage(
                image = image,
                contentScale = if (focus == OnboardingTableFocus.OVERVIEW) ContentScale.Fit else ContentScale.Crop,
                alignment = imageAlignment,
                modifier = Modifier.size(width = imageWidth, height = imageHeight)
            )
            TableStepCounter(focus = focus)
        }
    }
}

@Composable
private fun TableImage(
    @DrawableRes image: Int,
    contentScale: ContentScale,
    alignment: Alignment,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF080611))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.10f),
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = stringResource(R.string.onboarding_table_image_description),
            contentScale = contentScale,
            alignment = alignment,
            modifier = Modifier.matchParentSize()
        )
    }
}

@Composable
private fun TableStepCounter(focus: OnboardingTableFocus) {
    Text(
        text = stringResource(
            R.string.onboarding_table_substep_counter,
            focus.ordinal + 1,
            OnboardingTableFocus.entries.size
        ),
        color = OnboardingAccentLight,
        style = MaterialTheme.typography.caption,
        modifier = Modifier
            .padding(top = 10.dp)
            .clip(RoundedCornerShape(50))
            .background(Color(0xD911121F))
            .padding(horizontal = 12.dp, vertical = 7.dp)
    )
}

@DrawableRes
private fun tableImageFor(focus: OnboardingTableFocus): Int {
    return when (focus) {
        OnboardingTableFocus.OVERVIEW -> R.drawable.onboarding_game_table
        OnboardingTableFocus.PLAYERS_AND_TIMER -> R.drawable.onboarding_table_status
        OnboardingTableFocus.OBJECTIVE -> R.drawable.onboarding_table_objective
        OnboardingTableFocus.OPPONENT_SLOTS -> R.drawable.onboarding_table_opponent_slots
        OnboardingTableFocus.PLAYER_SLOTS -> R.drawable.onboarding_table_player_slots
        OnboardingTableFocus.HAND -> R.drawable.onboarding_table_hand
    }
}
