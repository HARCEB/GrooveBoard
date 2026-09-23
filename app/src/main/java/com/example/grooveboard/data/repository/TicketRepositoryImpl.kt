package com.example.grooveboard.data.repository

import com.example.grooveboard.data.datasource.MockData
import com.example.grooveboard.domain.model.Client
import com.example.grooveboard.domain.model.Comment
import com.example.grooveboard.domain.model.Ticket
import com.example.grooveboard.domain.model.TicketStatus
import com.example.grooveboard.domain.model.User
import com.example.grooveboard.domain.repository.TicketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TicketRepositoryImpl @Inject constructor() : TicketRepository {

    private val _tickets = MutableStateFlow<List<Ticket>>(MockData.getInitialTickets())
    override val tickets: Flow<List<Ticket>> = _tickets.asStateFlow()

    override suspend fun getTicketById(id: String): Ticket? {
        return _tickets.value.find { it.id == id || it.code == id }
    }

    override suspend fun createTicket(ticket: Ticket): Result<Ticket> {
        val currentList = _tickets.value.toMutableList()
        // Agregar al inicio para que aparezca primero
        currentList.add(0, ticket)
        _tickets.value = currentList
        return Result.success(ticket)
    }

    override suspend fun updateTicketStatus(ticketId: String, newStatus: TicketStatus): Result<Unit> {
        val currentList = _tickets.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == ticketId }
        if (index == -1) {
            return Result.failure(Exception("Ticket con ID $ticketId no encontrado."))
        }

        val updated = currentList[index].copy(status = newStatus)
        currentList[index] = updated
        _tickets.value = currentList
        return Result.success(Unit)
    }

    override suspend fun assignTicket(ticketId: String, agent: User): Result<Unit> {
        val currentList = _tickets.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == ticketId }
        if (index == -1) {
            return Result.failure(Exception("Ticket no encontrado."))
        }

        val updated = currentList[index].copy(assignedAgent = agent)
        currentList[index] = updated
        _tickets.value = currentList
        return Result.success(Unit)
    }

    override suspend fun addComment(ticketId: String, comment: Comment): Result<Unit> {
        val currentList = _tickets.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == ticketId }
        if (index == -1) {
            return Result.failure(Exception("Ticket no encontrado."))
        }

        val currentComments = currentList[index].comments.toMutableList()
        currentComments.add(comment)
        val updated = currentList[index].copy(comments = currentComments)
        currentList[index] = updated
        _tickets.value = currentList
        return Result.success(Unit)
    }

    override suspend fun getClients(): List<Client> {
        return MockData.clients
    }

    override suspend fun getAgents(): List<User> {
        return MockData.allUsers
    }
}
