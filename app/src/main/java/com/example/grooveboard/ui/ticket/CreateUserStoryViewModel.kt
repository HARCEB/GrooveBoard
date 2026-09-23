package com.example.grooveboard.ui.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grooveboard.domain.model.Client
import com.example.grooveboard.domain.model.Priority
import com.example.grooveboard.domain.model.Ticket
import com.example.grooveboard.domain.model.TicketStatus
import com.example.grooveboard.domain.model.TicketType
import com.example.grooveboard.domain.model.User
import com.example.grooveboard.domain.model.UserStoryDetails
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

data class CreateUserStoryUiState(
    val asA: String = "",
    val iWant: String = "",
    val soThat: String = "",
    val criteriaList: List<String> = listOf(""),
    val storyPoints: Int = 3,
    val selectedClient: Client? = null,
    val selectedPriority: Priority = Priority.MEDIUM,
    val selectedAgent: User? = null,
    val clients: List<Client> = emptyList(),
    val agents: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class CreateUserStoryViewModel @Inject constructor(
    private val ticketRepository: TicketRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateUserStoryUiState())
    val uiState = _uiState.asStateFlow()

    private val _storyCreatedEvent = MutableSharedFlow<Unit>()
    val storyCreatedEvent = _storyCreatedEvent.asSharedFlow()

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

    fun onAsAChange(asA: String) {
        _uiState.update { it.copy(asA = asA, errorMessage = null) }
    }

    fun onIWantChange(iWant: String) {
        _uiState.update { it.copy(iWant = iWant, errorMessage = null) }
    }

    fun onSoThatChange(soThat: String) {
        _uiState.update { it.copy(soThat = soThat, errorMessage = null) }
    }

    fun onStoryPointsChange(points: Int) {
        _uiState.update { it.copy(storyPoints = points) }
    }

    fun onClientSelect(client: Client) {
        _uiState.update { it.copy(selectedClient = client) }
    }

    fun onPrioritySelect(priority: Priority) {
        _uiState.update { it.copy(selectedPriority = priority) }
    }

    fun onAgentSelect(agent: User) {
        _uiState.update { it.copy(selectedAgent = agent) }
    }

    fun onCriterionChange(index: Int, text: String) {
        val updated = _uiState.value.criteriaList.toMutableList()
        if (index in updated.indices) {
            updated[index] = text
            _uiState.update { it.copy(criteriaList = updated) }
        }
    }

    fun addCriterion() {
        val updated = _uiState.value.criteriaList.toMutableList()
        updated.add("")
        _uiState.update { it.copy(criteriaList = updated) }
    }

    fun removeCriterion(index: Int) {
        val updated = _uiState.value.criteriaList.toMutableList()
        if (index in updated.indices && updated.size > 1) {
            updated.removeAt(index)
            _uiState.update { it.copy(criteriaList = updated) }
        }
    }

    fun createStory() {
        val state = _uiState.value
        if (state.asA.isBlank() || state.iWant.isBlank() || state.soThat.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Complete todos los campos del formato: Como, Quiero y Para.") }
            return
        }
        if (state.selectedClient == null) {
            _uiState.update { it.copy(errorMessage = "Seleccione un cliente corporativo.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val nowStr = sdf.format(Date())
            val randomCodeNumber = (107..999).random()

            val validCriteria = state.criteriaList.filter { it.isNotBlank() }

            val userStoryDetails = UserStoryDetails(
                asA = state.asA.trim(),
                iWant = state.iWant.trim(),
                soThat = state.soThat.trim(),
                acceptanceCriteria = validCriteria,
                storyPoints = state.storyPoints
            )

            val title = "Como ${state.asA.trim()}, quiero ${state.iWant.trim()}"
            val description = "Historia de Usuario Scrum: Como ${state.asA.trim()}, quiero ${state.iWant.trim()}, para ${state.soThat.trim()}."

            val newTicket = Ticket(
                id = "tck-${UUID.randomUUID()}",
                code = "GRV-$randomCodeNumber",
                title = title,
                description = description,
                status = TicketStatus.TODO,
                priority = state.selectedPriority,
                type = TicketType.USER_STORY,
                client = state.selectedClient,
                assignedAgent = state.selectedAgent,
                userStoryDetails = userStoryDetails,
                createdAt = nowStr,
                slaDeadline = "Quedan ${state.selectedPriority.slaHours}h",
                comments = emptyList()
            )

            ticketRepository.createTicket(newTicket)
            _uiState.update { it.copy(isLoading = false) }
            _storyCreatedEvent.emit(Unit)
        }
    }
}
