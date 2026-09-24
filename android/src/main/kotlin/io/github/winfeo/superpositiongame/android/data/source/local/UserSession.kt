package io.github.winfeo.superpositiongame.android.data.source.local

import io.github.winfeo.superpositiongame.android.data.source.AppModule
import io.github.winfeo.superpositiongame.android.domain.auth.model.AuthorizedUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserSession {
    private const val GUEST_ID_PREFIX = "guest-"

    private val _isAuthorized = MutableStateFlow(false)
    val isAuthorized: StateFlow<Boolean> = _isAuthorized.asStateFlow()

    private val _currentUser = MutableStateFlow<AuthorizedUser?>(null)
    val currentUser: StateFlow<AuthorizedUser?> = _currentUser.asStateFlow()

    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId.asStateFlow()

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token.asStateFlow()

    fun login(
        user: AuthorizedUser,
        token: String
    ) {
        _currentUser.value = user
        _isAuthorized.value = true
        _token.value = token

        AppModule.tokenManager.saveToken(token)
        AppModule.guestSessionManager.clearGuestId()
    }

    fun restoreToken(token: String) {
        _token.value = token
    }

    fun restoreSession(
        user: AuthorizedUser,
        token: String
    ) {
        _currentUser.value = user
        _isAuthorized.value = true
        _token.value = token

        AppModule.guestSessionManager.clearGuestId()
    }

    fun logout() {
        _currentUser.value = null
        _isAuthorized.value = false
        _token.value = null
        AppModule.tokenManager.clearToken()
    }

    fun setUserId(userId: String) {
        _currentUserId.value = userId

        if (userId.startsWith(GUEST_ID_PREFIX)) {
            AppModule.guestSessionManager.saveGuestId(userId)
        }
    }

    fun updateUser(user: AuthorizedUser) {
        _currentUser.value = user
    }

    fun clear() {
        _currentUserId.value = null
    }
}
