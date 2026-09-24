package io.github.winfeo.superpositiongame.android.domain.invitations.usecase

import io.github.winfeo.superpositiongame.android.domain.invitations.InvitationRepository
import io.github.winfeo.superpositiongame.android.domain.invitations.model.Invitation
import kotlinx.coroutines.flow.Flow

class ObserveInvitationsUseCase(
    private val repository: InvitationRepository
) {
    operator fun invoke(userId: String): Flow<List<Invitation>> {
        return repository.observeInvitations(userId)
    }
}
