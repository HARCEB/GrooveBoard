package com.example.proyectoaplicativo.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoaplicativo.domain.usecase.LoginUseCase
import com.example.proyectoaplicativo.presentation.common.SnackbarType
import com.example.proyectoaplicativo.presentation.event.UIEvent
import com.example.proyectoaplicativo.presentation.event.UIEventBus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val eventBus: UIEventBus
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()
    fun onUsuarioChange(valor: String) {
        _uiState.update { it.copy(usuario = valor) }
    }
    fun onContrasenaChange(valor: String) {
        _uiState.update { it.copy(contrasena = valor) }
    }
    fun iniciarSesion(onLoginSuccess: () -> Unit) {
        val estado = _uiState.value
        if (estado.usuario.isBlank() || estado.contrasena.isBlank()) {
            viewModelScope.launch {
                eventBus.SendEvent(
                    UIEvent.ShowSnackBar("Ingresa tu usuario y contraseña", SnackbarType.WARNING)
                )
            }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            loginUseCase(estado.usuario.trim(), estado.contrasena.trim())
                .onSuccess { usuario ->
                    _uiState.update { it.copy(isLoading = false) }
                    eventBus.SendEvent(
                        UIEvent.ShowSnackBar("¡Bienvenido, ${usuario.nombre}!", SnackbarType.SUCCESS)
                    )
                    onLoginSuccess()
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false) }
                    eventBus.SendEvent(
                        UIEvent.ShowSnackBar(
                            error.message ?: "Usuario o contraseña incorrectos",
                            SnackbarType.ERROR
                        )
                    )
                }
        }
    }
}
