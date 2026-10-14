package com.example.proyectoaplicativo.presentation.screens.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoaplicativo.domain.model.Ticket
import com.example.proyectoaplicativo.domain.usecase.ActualizarTicketUseCase
import com.example.proyectoaplicativo.domain.usecase.GetClientesUseCase
import com.example.proyectoaplicativo.domain.usecase.GetTicketUseCase
import com.example.proyectoaplicativo.domain.usecase.InsertarTicketUseCase
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
class FormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTicketUseCase: GetTicketUseCase,
    private val getClientesUseCase: GetClientesUseCase,
    private val insertarTicketUseCase: InsertarTicketUseCase,
    private val actualizarTicketUseCase: ActualizarTicketUseCase,
    private val eventBus: UIEventBus
) : ViewModel() {
    private val _uiState = MutableStateFlow(FormUiState())
    val uiState = _uiState.asStateFlow()
    init {
        cargarClientes()
        val ticketId: String? = savedStateHandle["id"]
        if (!ticketId.isNullOrEmpty() && ticketId != "{id}") {
            _uiState.update { it.copy(id = ticketId, isEditing = true) }
            cargarTicketData(ticketId)
        }
    }
    private fun cargarClientes() {
        viewModelScope.launch {
            getClientesUseCase()
                .onSuccess { listaClientes ->
                    _uiState.update { it.copy(clientes = listaClientes) }
                }
                .onFailure { error ->
                    eventBus.SendEvent(
                        UIEvent.ShowSnackBar(
                            error.message ?: "Error al cargar la lista de clientes",
                            SnackbarType.WARNING
                        )
                    )
                }
        }
    }
    private fun cargarTicketData(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getTicketUseCase(id)
                .onSuccess { ticket ->
                    _uiState.update {
                        it.copy(
                            codigo = ticket.codigo,
                            titulo = ticket.titulo,
                            descripcion = ticket.descripcion,
                            estado = ticket.estado,
                            prioridad = ticket.prioridad,
                            cliente = ticket.cliente,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false) }
                    eventBus.SendEvent(
                        UIEvent.ShowSnackBar(
                            error.message ?: "Error al cargar el ticket",
                            SnackbarType.ERROR
                        )
                    )
                }
        }
    }
    fun onCodigoChange(value: String) = _uiState.update { it.copy(codigo = value) }
    fun onTituloChange(value: String) = _uiState.update { it.copy(titulo = value) }
    fun onDescripcionChange(value: String) = _uiState.update { it.copy(descripcion = value) }
    fun onEstadoChange(value: String) = _uiState.update { it.copy(estado = value) }
    fun onPrioridadChange(value: String) = _uiState.update { it.copy(prioridad = value) }
    fun onClienteChange(value: String) = _uiState.update { it.copy(cliente = value) }
    fun guardarTicket(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.titulo.isBlank() || state.descripcion.isBlank() || state.cliente.isBlank()) {
            viewModelScope.launch {
                eventBus.SendEvent(
                    UIEvent.ShowSnackBar("El título, cliente y descripción son obligatorios", SnackbarType.WARNING)
                )
            }
            return
        }
        val ticket = Ticket(
            id = state.id.orEmpty(),
            codigo = state.codigo.ifBlank { "TCK-${System.currentTimeMillis().toString().takeLast(4)}" },
            titulo = state.titulo,
            descripcion = state.descripcion,
            estado = state.estado,
            prioridad = state.prioridad,
            cliente = state.cliente
        )
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val resultado = if (state.isEditing) {
                actualizarTicketUseCase(state.id!!, ticket)
            } else {
                insertarTicketUseCase(ticket)
            }
            resultado
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    val mensaje = if (state.isEditing) "Ticket actualizado con éxito" else "Ticket registrado con éxito en AWS"
                    eventBus.SendEvent(UIEvent.ShowSnackBar(mensaje, SnackbarType.SUCCESS))
                    onSuccess()
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false) }
                    eventBus.SendEvent(
                        UIEvent.ShowSnackBar(
                            error.message ?: "Error al guardar en el servidor",
                            SnackbarType.ERROR
                        )
                    )
                }
        }
    }
}