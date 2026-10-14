package com.example.proyectoaplicativo.presentation.screens.buscar

import com.example.proyectoaplicativo.domain.model.Ticket

data class BuscarUiState(
    val query: String = "",
    val allTickets: List<Ticket> = emptyList(),
    val filteredTickets: List<Ticket> = emptyList(),
    val isLoading: Boolean = false
)