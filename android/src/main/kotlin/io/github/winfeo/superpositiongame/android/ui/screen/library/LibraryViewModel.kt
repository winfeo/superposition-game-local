package io.github.winfeo.superpositiongame.android.ui.screen.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.winfeo.superpositiongame.android.domain.library.Card
import io.github.winfeo.superpositiongame.android.domain.library.CardsRepository
import io.github.winfeo.superpositiongame.android.domain.library.GetAllCardsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val repository: CardsRepository
): ViewModel() {
    private val getAllCardsUseCase = GetAllCardsUseCase(repository)
    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards

    init {
        loadAllCards()
    }

    private fun loadAllCards() {
        viewModelScope.launch {
            getAllCardsUseCase().collect { cards ->
                _cards.value = cards
            }
        }
    }
}
