package io.github.winfeo.superpositiongame.android.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.winfeo.superpositiongame.android.data.source.local.UserSession
import io.github.winfeo.superpositiongame.android.domain.profile.ProfileRepository
import io.github.winfeo.superpositiongame.android.domain.profile.usecase.GetGameHistoryUseCase
import io.github.winfeo.superpositiongame.android.domain.profile.usecase.GetUserProfileUseCase
import io.github.winfeo.superpositiongame.android.domain.profile.usecase.UpdateNicknameUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: ProfileRepository
) : ViewModel() {
    private val getGameHistoryUseCase = GetGameHistoryUseCase(repository)
    private val getUserProfileUseCase = GetUserProfileUseCase(repository)
    private val updateNicknameUseCase = UpdateNicknameUseCase(repository)

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    private val _isEditNicknameDialogVisible = MutableStateFlow(false)
    val isEditNicknameDialogVisible: StateFlow<Boolean> = _isEditNicknameDialogVisible.asStateFlow()

    private val _editNicknameError = MutableStateFlow<String?>(null)
    val editNicknameError: StateFlow<String?> = _editNicknameError.asStateFlow()


    init {
        viewModelScope.launch {
            combine(
                UserSession.isAuthorized,
                UserSession.currentUser
            ) { isAuth, user ->
                isAuth to user
            }.collect { (isAuth, user) ->
                _state.value = if (isAuth) {
                    _state.value.copy(
                        isAuthorized = true,
                        user = user
                    )
                } else {
                    ProfileState()
                }

                if (isAuth && user != null) {
                    loadGameHistory(user.id)
                    loadUserStats(user.id)
                }
            }
        }
    }

    fun loadGameHistory(userId: Long) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoadingHistory = true,
                historyError = null
            )

            val result = getGameHistoryUseCase(userId)
            result.fold(
                onSuccess = { history ->
                    _state.value = _state.value.copy(
                        isLoadingHistory = false,
                        gameHistory = history
                    )
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        isLoadingHistory = false,
                        historyError = error.message
                    )
                }
            )
        }
    }

    fun loadUserStats(userId: Long) {
        viewModelScope.launch {
            val result = getUserProfileUseCase(userId)
            result.onSuccess { updatedUser ->
                _state.value = _state.value.copy(user = updatedUser)
                UserSession.updateUser(updatedUser)
            }
        }
    }

    fun showEditNicknameDialog() {
        _editNicknameError.value = null
        _isEditNicknameDialogVisible.value = true
    }

    fun hideEditNicknameDialog() {
        _isEditNicknameDialogVisible.value = false
        _editNicknameError.value = null
    }

    fun updateNickname(newNickname: String) {
        val currentUser = _state.value.user ?: return
        viewModelScope.launch {
            val result = updateNicknameUseCase(currentUser.id, newNickname)
            result.fold(
                onSuccess = { updatedUser ->
                    _state.value = _state.value.copy(user = updatedUser)
                    UserSession.updateUser(updatedUser)
                    hideEditNicknameDialog()
                },
                onFailure = { error ->
                    _editNicknameError.value = error.message
                }
            )
        }
    }
}
