package io.github.winfeo.superpositiongame.android.domain.lobby

import io.github.winfeo.superpositiongame.android.domain.lobby.model.Player
import kotlinx.coroutines.flow.Flow

interface LobbyRepository {
    fun observePlayersInLobby(currentUserId: String): Flow<List<Player>>
    suspend fun sendInvitation(senderId: String, senderNickname: String?, receiverId: String)
}
