package com.example.proyectoaplicativo.presentation.screens.form

import com.example.proyectoaplicativo.domain.model.Cliente


data class FormUiState(
    val id: String? = null,
    val codigo: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val estado: String = "Abierto",
    val prioridad: String = "Media",
    val cliente: String = "",
    val clientes: List<Cliente> = emptyList(),
    val isLoading: Boolean = false,
    val isEditing: Boolean = false
)
