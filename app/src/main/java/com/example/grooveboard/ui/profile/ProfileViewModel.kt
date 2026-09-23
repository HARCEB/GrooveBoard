package com.example.grooveboard.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grooveboard.domain.model.User
import com.example.grooveboard.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val user: User? = null,
    val currentPasswordInput: String = "",
    val newPasswordInput: String = "",
    val confirmPasswordInput: String = "",
    val isChangingPassword: Boolean = false,
    val passwordSuccessMessage: String? = null,
    val passwordErrorMessage: String? = null,
    val showChangePasswordDialog: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    private val _logoutEvent = MutableSharedFlow<Unit>()
    val logoutEvent = _logoutEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            authRepository.currentUser.collect { user ->
                _uiState.update { it.copy(user = user) }
            }
        }
    }

    fun openChangePasswordDialog() {
        _uiState.update {
            it.copy(
                showChangePasswordDialog = true,
                currentPasswordInput = "",
                newPasswordInput = "",
                confirmPasswordInput = "",
                passwordErrorMessage = null,
                passwordSuccessMessage = null
            )
        }
    }

    fun closeChangePasswordDialog() {
        _uiState.update { it.copy(showChangePasswordDialog = false) }
    }

    fun onCurrentPasswordChange(value: String) {
        _uiState.update { it.copy(currentPasswordInput = value, passwordErrorMessage = null) }
    }

    fun onNewPasswordChange(value: String) {
        _uiState.update { it.copy(newPasswordInput = value, passwordErrorMessage = null) }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(confirmPasswordInput = value, passwordErrorMessage = null) }
    }

    fun changePassword() {
        val currentPass = _uiState.value.currentPasswordInput
        val newPass = _uiState.value.newPasswordInput
        val confirmPass = _uiState.value.confirmPasswordInput

        if (currentPass.isBlank() || newPass.isBlank() || confirmPass.isBlank()) {
            _uiState.update { it.copy(passwordErrorMessage = "Por favor complete todos los campos.") }
            return
        }

        if (newPass != confirmPass) {
            _uiState.update { it.copy(passwordErrorMessage = "La nueva contraseña y su confirmación no coinciden.") }
            return
        }

        if (newPass.length < 6) {
            _uiState.update { it.copy(passwordErrorMessage = "La nueva contraseña debe tener al menos 6 caracteres.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isChangingPassword = true, passwordErrorMessage = null) }
            val result = authRepository.changePassword(currentPass, newPass)
            result.onSuccess {
                _uiState.update {
                    it.copy(
                        isChangingPassword = false,
                        passwordSuccessMessage = "¡Contraseña actualizada correctamente!",
                        showChangePasswordDialog = false
                    )
                }
            }.onFailure { ex ->
                _uiState.update {
                    it.copy(
                        isChangingPassword = false,
                        passwordErrorMessage = ex.message ?: "Error al actualizar la contraseña."
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _logoutEvent.emit(Unit)
        }
    }
}
