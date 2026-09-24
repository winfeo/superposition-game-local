package io.github.winfeo.superpositiongame.android.ui.screen.invites

import io.github.winfeo.superpositiongame.android.domain.invitations.model.Invitation

data class InvitationState (
    val invitations: List<Invitation> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
