package io.github.winfeo.superpositiongame.android.ui.screen.invites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.winfeo.superpositiongame.android.data.source.local.UserSession
import io.github.winfeo.superpositiongame.android.domain.invitations.InvitationRepository
import io.github.winfeo.superpositiongame.android.domain.invitations.model.Invitation
import io.github.winfeo.superpositiongame.android.domain.invitations.usecase.AcceptInvitationUseCase
import io.github.winfeo.superpositiongame.android.domain.invitations.usecase.ObserveInvitationsUseCase
import io.github.winfeo.superpositiongame.android.domain.invitations.usecase.RejectInvitationUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class InvitationViewModel(
    private val repository: InvitationRepository
): ViewModel() {
    private val observeInvitationsUseCase = ObserveInvitationsUseCase(repository)
    private val acceptInvitationUseCase = AcceptInvitationUseCase(repository)
    private val rejectInvitationUseCase = RejectInvitationUseCase(repository)
    private val _state = MutableStateFlow(InvitationState())
    val state: StateFlow<InvitationState> = _state

    private var loadJob: Job? = null

    init {
        viewModelScope.launch {
            UserSession.currentUserId.collect { userId ->
                if (userId != null) {
                    loadInvitations(userId)
                }
            }
        }
    }

    fun loadInvitations(userId: String) {
        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            observeInvitationsUseCase(userId)
                .onStart { _state.value = _state.value.copy(isLoading = true) }
                .catch { _state.value = _state.value.copy(isLoading = false, error = it.message) }
                .collect { invites ->
                    _state.value = InvitationState(invitations = invites, isLoading = false)
                }
        }
    }

    fun acceptInvitation(invitation: Invitation) {
        val currentUserId = UserSession.currentUserId.value?: return
        viewModelScope.launch {
            acceptInvitationUseCase(
                invitation = invitation,
                currentUserId = currentUserId
            )
        }
    }

    fun rejectInvitation(invitation: Invitation) {
        val currentUserId = UserSession.currentUserId.value?: return
        viewModelScope.launch {
            rejectInvitationUseCase(
                invitation = invitation,
                currentUserId = currentUserId
            )
        }
    }
}
