package com.example.grooveboard.ui.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grooveboard.domain.model.Client
import com.example.grooveboard.domain.model.Priority
import com.example.grooveboard.domain.model.Ticket
import com.example.grooveboard.domain.model.TicketStatus
import com.example.grooveboard.domain.model.User
import com.example.grooveboard.domain.repository.AuthRepository
import com.example.grooveboard.domain.repository.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FilterCriteria(
    val statusTab: TicketStatus = TicketStatus.TODO,
    val clientId: String? = null,
    val priority: Priority? = null,
    val query: String = ""
)

data class BoardUiState(
    val currentUser: User? = null,
    val selectedStatusTab: TicketStatus = TicketStatus.TODO,
    val selectedClientId: String? = null,
    val selectedPriority: Priority? = null,
    val searchQuery: String = "",
    val allClients: List<Client> = emptyList(),
    val todoTickets: List<Ticket> = emptyList(),
    val inProgressTickets: List<Ticket> = emptyList(),
    val resolvedTickets: List<Ticket> = emptyList()
)

@HiltViewModel
class BoardViewModel @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _filters = MutableStateFlow(FilterCriteria())
    private val _clients = MutableStateFlow<List<Client>>(emptyList())

    init {
        viewModelScope.launch {
            _clients.value = ticketRepository.getClients()
        }
    }

    val uiState = combine(
        authRepository.currentUser,
        ticketRepository.tickets,
        _filters,
        _clients
    ) { user: User?, tickets: List<Ticket>, filters: FilterCriteria, clients: List<Client> ->
        val filtered = tickets.filter { ticket ->
            val matchClient = filters.clientId == null || ticket.client.id == filters.clientId
            val matchPriority = filters.priority == null || ticket.priority == filters.priority
            val matchQuery = filters.query.isBlank() ||
                    ticket.title.contains(filters.query, ignoreCase = true) ||
                    ticket.code.contains(filters.query, ignoreCase = true) ||
                    ticket.description.contains(filters.query, ignoreCase = true)

            matchClient && matchPriority && matchQuery
        }

        BoardUiState(
            currentUser = user,
            selectedStatusTab = filters.statusTab,
            selectedClientId = filters.clientId,
            selectedPriority = filters.priority,
            searchQuery = filters.query,
            allClients = clients,
            todoTickets = filtered.filter { it.status == TicketStatus.TODO },
            inProgressTickets = filtered.filter { it.status == TicketStatus.IN_PROGRESS },
            resolvedTickets = filtered.filter { it.status == TicketStatus.RESOLVED }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BoardUiState()
    )

    fun onTabSelected(status: TicketStatus) {
        _filters.update { it.copy(statusTab = status) }
    }

    fun onClientFilterChanged(clientId: String?) {
        _filters.update { it.copy(clientId = clientId) }
    }

    fun onPriorityFilterChanged(priority: Priority?) {
        _filters.update { it.copy(priority = priority) }
    }

    fun onSearchQueryChanged(query: String) {
        _filters.update { it.copy(query = query) }
    }

    fun updateTicketStatus(ticketId: String, newStatus: TicketStatus) {
        viewModelScope.launch {
            ticketRepository.updateTicketStatus(ticketId, newStatus)
        }
    }
}
