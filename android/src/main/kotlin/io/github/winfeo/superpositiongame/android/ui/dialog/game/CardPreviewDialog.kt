package io.github.winfeo.superpositiongame.android.ui.dialog.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.android.data.util.cardImageResource
import io.github.winfeo.superpositiongame.android.ui.dialog.BoardDialogSurface
import io.github.winfeo.superpositiongame.model.card.Card

@Composable
fun CardPreviewDialog(card: Card, onDismiss: () -> Unit) {
    BoardDialogSurface(onDismissRequest = onDismiss) {
        Image(
            painter = painterResource(cardImageResource(card)),
            contentDescription = stringResource(R.string.dialog_card_image, card.type.name),
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .width(dimensionResource(R.dimen.dialog_preview_width))
                .height(dimensionResource(R.dimen.dialog_preview_height))
                .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_16)))
        )
        Text(
            text = stringResource(R.string.dialog_card_preview_hint),
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
