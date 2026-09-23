package com.example.grooveboard.ui.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grooveboard.domain.model.Client
import com.example.grooveboard.domain.model.Priority
import com.example.grooveboard.domain.model.Ticket
import com.example.grooveboard.domain.model.TicketStatus
import com.example.grooveboard.domain.model.TicketType
import com.example.grooveboard.domain.model.User
import com.example.grooveboard.domain.repository.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

data class CreateTicketUiState(
    val title: String = "",
    val description: String = "",
    val selectedClient: Client? = null,
    val selectedType: TicketType = TicketType.INCIDENT,
    val selectedPriority: Priority = Priority.HIGH,
    val selectedAgent: User? = null,
    val clients: List<Client> = emptyList(),
    val agents: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class CreateTicketViewModel @Inject constructor(
    private val ticketRepository: TicketRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateTicketUiState())
    val uiState = _uiState.asStateFlow()

    private val _ticketCreatedEvent = MutableSharedFlow<Unit>()
    val ticketCreatedEvent = _ticketCreatedEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            val clients = ticketRepository.getClients()
            val agents = ticketRepository.getAgents()
            _uiState.update {
                it.copy(
                    clients = clients,
                    agents = agents,
                    selectedClient = clients.firstOrNull(),
                    selectedAgent = agents.firstOrNull()
                )
            }
        }
    }

    fun onTitleChange(title: String) {
        _uiState.update { it.copy(title = title, errorMessage = null) }
    }

    fun onDescriptionChange(desc: String) {
        _uiState.update { it.copy(description = desc, errorMessage = null) }
    }

    fun onClientSelect(client: Client) {
        _uiState.update { it.copy(selectedClient = client) }
    }

    fun onTypeSelect(type: TicketType) {
        _uiState.update { it.copy(selectedType = type) }
    }

    fun onPrioritySelect(priority: Priority) {
        _uiState.update { it.copy(selectedPriority = priority) }
    }

    fun onAgentSelect(agent: User) {
        _uiState.update { it.copy(selectedAgent = agent) }
    }

    fun createTicket() {
        val state = _uiState.value
        if (state.title.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Por favor ingrese el título del ticket.") }
            return
        }
        if (state.description.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Por favor ingrese la descripción técnica.") }
            return
        }
        if (state.selectedClient == null) {
            _uiState.update { it.copy(errorMessage = "Debe asociar el ticket a un cliente corporativo.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val nowStr = sdf.format(Date())
            val randomCodeNumber = (107..999).random()

            val newTicket = Ticket(
                id = "tck-${UUID.randomUUID()}",
                code = "GRV-$randomCodeNumber",
                title = state.title.trim(),
                description = state.description.trim(),
                status = TicketStatus.TODO,
                priority = state.selectedPriority,
                type = state.selectedType,
                client = state.selectedClient,
                assignedAgent = state.selectedAgent,
                createdAt = nowStr,
                slaDeadline = "Quedan ${state.selectedPriority.slaHours}h",
                comments = emptyList()
            )

            ticketRepository.createTicket(newTicket)
            _uiState.update { it.copy(isLoading = false) }
            _ticketCreatedEvent.emit(Unit)
        }
    }
}
