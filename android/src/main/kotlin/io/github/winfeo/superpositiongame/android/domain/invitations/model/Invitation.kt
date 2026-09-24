package io.github.winfeo.superpositiongame.android.domain.invitations.model

data class Invitation(
    val senderId: String,
    val senderNickname: String?,
    val receiverId: String,
    val sendTime: String
)
