package io.github.winfeo.superpositiongame.android.domain.invitations.usecase

import io.github.winfeo.superpositiongame.android.domain.invitations.InvitationRepository
import io.github.winfeo.superpositiongame.android.domain.invitations.model.Invitation

class RejectInvitationUseCase(
    private val repository: InvitationRepository
) {
    suspend operator fun invoke(
        invitation: Invitation,
        currentUserId: String
    ) {
        repository.rejectInvitation(
            invitation = invitation,
            currentUserId = currentUserId
        )
    }
}
