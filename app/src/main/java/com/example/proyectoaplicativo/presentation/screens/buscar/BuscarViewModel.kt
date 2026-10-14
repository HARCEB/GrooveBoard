package com.example.proyectoaplicativo.presentation.screens.buscar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoaplicativo.domain.usecase.GetTicketsUseCase
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
class BuscarViewModel @Inject constructor(
    private val getTicketsUseCase: GetTicketsUseCase,
    private val eventBus: UIEventBus
) : ViewModel() {
    private val _uiState = MutableStateFlow(BuscarUiState())
    val uiState = _uiState.asStateFlow()
    init {
        cargarTickets()
    }
    fun cargarTickets() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getTicketsUseCase()
                .onSuccess { tickets ->
                    _uiState.update {
                        it.copy(
                            allTickets = tickets,
                            filteredTickets = tickets,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false) }
                    eventBus.SendEvent(
                        UIEvent.ShowSnackBar(
                            error.message ?: "Error al cargar tickets para la búsqueda",
                            SnackbarType.ERROR
                        )
                    )
                }
        }
    }
    fun onQueryChange(newQuery: String) {
        _uiState.update { state ->
            val filtrados = if (newQuery.isBlank()) {
                state.allTickets
            } else {
                state.allTickets.filter { ticket ->
                    ticket.titulo.contains(newQuery, ignoreCase = true) ||
                            ticket.cliente.contains(newQuery, ignoreCase = true) ||
                            ticket.codigo.contains(newQuery, ignoreCase = true)
                }
            }
            state.copy(query = newQuery, filteredTickets = filtrados)
        }
    }
}
