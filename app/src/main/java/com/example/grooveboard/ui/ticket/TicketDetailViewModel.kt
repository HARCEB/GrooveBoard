package com.example.grooveboard.ui.ticket

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grooveboard.domain.model.Comment
import com.example.grooveboard.domain.model.Ticket
import com.example.grooveboard.domain.model.TicketStatus
import com.example.grooveboard.domain.model.User
import com.example.grooveboard.domain.model.UserRole
import com.example.grooveboard.domain.repository.AuthRepository
import com.example.grooveboard.domain.repository.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

data class TicketDetailUiState(
    val ticket: Ticket? = null,
    val currentUser: User? = null,
    val allAgents: List<User> = emptyList(),
    val commentInput: String = "",
    val isSendingComment: Boolean = false,
    val showReassignDialog: Boolean = false
)

@HiltViewModel
class TicketDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val ticketRepository: TicketRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val ticketId: String = checkNotNull(savedStateHandle["ticketId"])

    private val _uiState = MutableStateFlow(TicketDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadTicketData()
        loadAgentsAndUser()
    }

    private fun loadTicketData() {
        viewModelScope.launch {
            ticketRepository.tickets.collect { tickets ->
                val found = tickets.find { it.id == ticketId }
                _uiState.update { it.copy(ticket = found) }
            }
        }
    }

    private fun loadAgentsAndUser() {
        viewModelScope.launch {
            val agents = ticketRepository.getAgents()
            val user = authRepository.getCurrentUserSync()
            _uiState.update { it.copy(allAgents = agents, currentUser = user) }
        }
    }

    fun onCommentInputChange(text: String) {
        _uiState.update { it.copy(commentInput = text) }
    }

    fun addComment() {
        val content = _uiState.value.commentInput.trim()
        val user = _uiState.value.currentUser ?: return
        if (content.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSendingComment = true) }

            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val nowStr = sdf.format(Date())

            val newComment = Comment(
                id = "c-${UUID.randomUUID()}",
                authorName = user.name,
                authorRole = user.roleTitle,
                authorInitials = user.avatarInitials,
                content = content,
                timestamp = nowStr
            )

            ticketRepository.addComment(ticketId, newComment)
            _uiState.update { it.copy(commentInput = "", isSendingComment = false) }
        }
    }

    fun updateStatus(newStatus: TicketStatus) {
        viewModelScope.launch {
            ticketRepository.updateTicketStatus(ticketId, newStatus)
        }
    }

    fun openReassignDialog() {
        _uiState.update { it.copy(showReassignDialog = true) }
    }

    fun closeReassignDialog() {
        _uiState.update { it.copy(showReassignDialog = false) }
    }

    fun assignToAgent(agent: User) {
        viewModelScope.launch {
            ticketRepository.assignTicket(ticketId, agent)
            closeReassignDialog()
        }
    }

    fun selfAssign() {
        val user = _uiState.value.currentUser ?: return
        viewModelScope.launch {
            ticketRepository.assignTicket(ticketId, user)
        }
    }
}
