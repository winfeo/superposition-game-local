package io.github.winfeo.superpositiongame.android.domain.invitations.usecase

import io.github.winfeo.superpositiongame.android.domain.invitations.InvitationRepository
import io.github.winfeo.superpositiongame.android.domain.invitations.model.Invitation

class AcceptInvitationUseCase(
    private val repository: InvitationRepository
) {
    suspend operator fun invoke(
        invitation: Invitation,
        currentUserId: String
    ) {
        repository.acceptInvitation(
            invitation = invitation,
            currentUserId = currentUserId
        )
    }
}
