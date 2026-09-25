package io.github.winfeo.superpositiongame.android.ui.dialog.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.android.data.util.cardImageResource
import io.github.winfeo.superpositiongame.model.card.Card

@Composable
fun SelectableCardItem(
    card: Card,
    selected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val indication = LocalIndication.current
    val colors = MaterialTheme.colors
    val shape = RoundedCornerShape(dimensionResource(R.dimen.space_8))
    val borderColor = if (selected) colors.primary else colors.onSurface.copy(alpha = 0.08f)
    val backgroundColor = if (selected) colors.primary.copy(alpha = 0.22f) else colors.onSurface.copy(alpha = 0.08f)

    Image(
        painter = painterResource(cardImageResource(card)),
        contentDescription = stringResource(R.string.dialog_card_image, card.type.name),
        modifier = Modifier.fillMaxWidth().aspectRatio(0.7f)
            .clip(shape)
            .background(backgroundColor)
            .border(Dp.Hairline, borderColor, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = indication,
                onClick = onClick
            )
            .padding(dimensionResource(R.dimen.space_8))
    )
}
