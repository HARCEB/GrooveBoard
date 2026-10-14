package com.example.proyectoaplicativo.presentation.screens.login

data class LoginUiState(
    val usuario: String = "",
    val contrasena: String = "",
    val isLoading: Boolean = false
)
