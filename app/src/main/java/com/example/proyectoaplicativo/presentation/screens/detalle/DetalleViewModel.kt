package com.example.proyectoaplicativo.presentation.screens.detalle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoaplicativo.domain.usecase.EliminarTicketUseCase
import com.example.proyectoaplicativo.domain.usecase.GetTicketUseCase
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
class DetalleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTicketUseCase: GetTicketUseCase,
    private val eliminarTicketUseCase: EliminarTicketUseCase,
    private val eventBus: UIEventBus
) : ViewModel() {
    private val _uiState = MutableStateFlow(DetalleUiState())
    val uiState = _uiState.asStateFlow()
    private val ticketId: String? = savedStateHandle["id"]
    init {
        cargarTicket()
    }
    fun cargarTicket() {
        val id = ticketId ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getTicketUseCase(id)
                .onSuccess { ticket ->
                    _uiState.update { it.copy(ticket = ticket, isLoading = false) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false) }
                    eventBus.SendEvent(
                        UIEvent.ShowSnackBar(
                            error.message ?: "Error al obtener el detalle del ticket",
                            SnackbarType.ERROR
                        )
                    )
                }
        }
    }
    fun showDeleteDialog() = _uiState.update { it.copy(showDeleteDialog = true) }
    fun dismissDeleteDialog() = _uiState.update { it.copy(showDeleteDialog = false) }
    fun confirmarEliminar(onDeleted: () -> Unit) {
        val id = ticketId ?: return
        viewModelScope.launch {
            dismissDeleteDialog()
            _uiState.update { it.copy(isLoading = true) }
            eliminarTicketUseCase(id)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    eventBus.SendEvent(
                        UIEvent.ShowSnackBar("Ticket eliminado con éxito", SnackbarType.SUCCESS)
                    )
                    onDeleted() // Regresa a la lista
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false) }
                    eventBus.SendEvent(
                        UIEvent.ShowSnackBar(
                            error.message ?: "Error al eliminar el ticket",
                            SnackbarType.ERROR
                        )
                    )
                }
        }
    }
}