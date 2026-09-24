package io.github.winfeo.superpositiongame.android.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.winfeo.superpositiongame.android.data.source.socket.Network
import io.github.winfeo.superpositiongame.android.data.source.local.UserSession
import io.github.winfeo.superpositiongame.android.domain.auth.AuthRepository
import io.github.winfeo.superpositiongame.android.domain.auth.model.AuthToken
import io.github.winfeo.superpositiongame.android.domain.auth.model.AuthorizedUser
import io.github.winfeo.superpositiongame.android.domain.auth.usecase.LoginUseCase
import io.github.winfeo.superpositiongame.android.domain.auth.usecase.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {
    private val loginUseCase = LoginUseCase(repository)
    private val registerUseCase = RegisterUseCase(repository)

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    fun onEmailChange(email: String) {
        _state.value = _state.value.copy(email = email, error = null)
    }

    fun onPasswordChange(password: String) {
        _state.value = _state.value.copy(password = password, error = null)
    }

    fun login() {
        val currentState = _state.value
        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            _state.value = currentState.copy(error = "Заполните все поля")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val result = loginUseCase(currentState.email, currentState.password)
            result.fold(
                onSuccess = { (user, token) -> performLogin(user, token) },
                onFailure = { error -> _state.value = _state.value.copy(isLoading = false, error = error.message) }
            )
        }
    }

    fun register() {
        val currentState = _state.value
        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            _state.value = currentState.copy(error = "Заполните все поля")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val result = registerUseCase(currentState.email, currentState.password)
            result.fold(
                onSuccess = { user ->
                    val loginResult = loginUseCase(currentState.email, currentState.password)
                    loginResult.fold(
                        onSuccess = { (_, token) -> performLogin(user, token) },
                        onFailure = { error -> _state.value = _state.value.copy(isLoading = false, error = error.message) }
                    )
                },
                onFailure = { error -> _state.value = _state.value.copy(isLoading = false, error = error.message) }
            )
        }
    }

    private fun performLogin(
        user: AuthorizedUser,
        token: AuthToken
    ) {
        Network.disconnect()
        UserSession.login(user = user, token = token.accessToken)
        UserSession.setUserId(user.id.toString())
        Network.connect(userId = user.id.toString())
        _state.value = _state.value.copy(isLoading = false, isSuccess = true)
    }

    fun resetForm() {
        _state.value = AuthState()
    }

    fun resetSuccess() {
        _state.value = _state.value.copy(isSuccess = false)
    }
}
