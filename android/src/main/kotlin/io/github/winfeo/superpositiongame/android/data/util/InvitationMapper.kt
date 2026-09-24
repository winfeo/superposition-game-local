package io.github.winfeo.superpositiongame.android.data.util

import io.github.winfeo.superpositiongame.android.data.dto.socket.InvitationDTO
import io.github.winfeo.superpositiongame.android.domain.invitations.model.Invitation

fun InvitationDTO.toDomain(): Invitation {
    return Invitation(
        senderId = senderId,
        senderNickname = senderNickname,
        receiverId = receiverId,
        sendTime = sendTime?: ""
    )
}

fun Invitation.toDto(
    currentUserId: String,
): InvitationDTO {
    return InvitationDTO(
        senderId = senderId,
        senderNickname = senderNickname,
        receiverId = currentUserId,
        sendTime = sendTime
    )
}
