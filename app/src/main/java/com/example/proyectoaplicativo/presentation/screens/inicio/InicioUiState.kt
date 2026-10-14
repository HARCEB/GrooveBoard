package com.example.proyectoaplicativo.presentation.screens.inicio

import com.example.proyectoaplicativo.domain.model.Ticket


data class InicioUiState(
    val tickets: List<Ticket> = emptyList(),
    val isLoading: Boolean = false
)