package com.example.proyectoaplicativo.presentation.screens.inicio

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
class InicioViewModel @Inject constructor(
    private val getTicketsUseCase: GetTicketsUseCase,
    private val eventBus: UIEventBus
) : ViewModel() {
    private val _uiState = MutableStateFlow(InicioUiState())
    val uiState = _uiState.asStateFlow()
    fun cargarTickets() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getTicketsUseCase()
                .onSuccess { listaTickets ->
                    _uiState.update {
                        it.copy(tickets = listaTickets, isLoading = false)
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false) }
                    eventBus.SendEvent(
                        UIEvent.ShowSnackBar(
                            error.message ?: "Error al conectar",
                            SnackbarType.ERROR
                        )
                    )
                }
        }
    }
}
