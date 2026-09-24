package io.github.winfeo.superpositiongame.android.data.dto.socket

import kotlinx.serialization.Serializable

@Serializable
data class InvitationDTO(
    val senderId: String,
    val senderNickname: String?,
    val receiverId: String,
    val sendTime: String?
)
