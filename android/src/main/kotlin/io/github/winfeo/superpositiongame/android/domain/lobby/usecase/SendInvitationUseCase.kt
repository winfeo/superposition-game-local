package io.github.winfeo.superpositiongame.android.domain.lobby.usecase

import io.github.winfeo.superpositiongame.android.domain.lobby.LobbyRepository

class SendInvitationUseCase(
    private val repository: LobbyRepository
) {
    suspend operator fun invoke(
        senderId: String,
        senderNickname: String?,
        receiverId: String
    ) {
        repository.sendInvitation(
            senderId = senderId,
            senderNickname = senderNickname,
            receiverId = receiverId
        )
    }
}
