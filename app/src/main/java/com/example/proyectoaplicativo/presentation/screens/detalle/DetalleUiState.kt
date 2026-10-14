package com.example.proyectoaplicativo.presentation.screens.detalle

import com.example.proyectoaplicativo.domain.model.Ticket


data class DetalleUiState(
    val ticket: Ticket? = null,
    val isLoading: Boolean = false,
    val showDeleteDialog: Boolean = false
)
