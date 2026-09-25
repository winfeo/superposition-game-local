package io.github.winfeo.superpositiongame.android.ui.dialog.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import io.github.winfeo.superpositiongame.R
import io.github.winfeo.superpositiongame.android.ui.dialog.BoardDialogButton
import io.github.winfeo.superpositiongame.android.ui.dialog.BoardDialogSurface
import io.github.winfeo.superpositiongame.model.card.Card

@Composable
fun ReshuffleCardDialog(
    cards: List<Card>,
    minSelectable: Int,
    maxSelectable: Int,
    onCardsSelected: (List<Card>) -> Unit
) {
    var selectedIds by remember(cards) { mutableStateOf<Set<String>>(emptySet()) }
    val space8 = dimensionResource(R.dimen.space_8)
    val validSelection = selectedIds.size in minSelectable..maxSelectable

    BoardDialogSurface(onDismissRequest = {}, dismissable = false) {
        Text(
            text = stringResource(R.string.dialog_reshuffle_count, selectedIds.size, maxSelectable),
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.body1
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth()
                .heightIn(max = dimensionResource(R.dimen.dialog_grid_max_height)),
            contentPadding = PaddingValues(space8),
            horizontalArrangement = Arrangement.spacedBy(space8),
            verticalArrangement = Arrangement.spacedBy(space8)
        ) {
            items(cards, key = { it.id }) { card ->
                SelectableCardItem(
                    card = card,
                    selected = card.id in selectedIds,
                    onClick = {
                        selectedIds = if (card.id in selectedIds) {
                            selectedIds - card.id
                        } else if (selectedIds.size < maxSelectable) {
                            selectedIds + card.id
                        } else {
                            selectedIds
                        }
                    }
                )
            }
        }
        BoardDialogButton(
            text = stringResource(R.string.dialog_reshuffle_confirm),
            onClick = { onCardsSelected(cards.filter { it.id in selectedIds }) },
            modifier = Modifier.fillMaxWidth(),
            enabled = validSelection
        )
        Text(
            text = stringResource(R.string.dialog_reshuffle_hint),
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
