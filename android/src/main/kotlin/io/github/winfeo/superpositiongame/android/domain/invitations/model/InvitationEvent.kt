package io.github.winfeo.superpositiongame.android.domain.invitations.model

sealed class InvitationEvent {
    data class New(
        val invitation: Invitation
    ): InvitationEvent()

    data class Removed(
        val invitation: Invitation
    ): InvitationEvent()

    data class Initialized(
        val invitations: List<Invitation>
    ): InvitationEvent()
}
