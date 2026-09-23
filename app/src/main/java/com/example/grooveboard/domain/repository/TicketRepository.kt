package com.example.grooveboard.domain.repository

import com.example.grooveboard.domain.model.Client
import com.example.grooveboard.domain.model.Comment
import com.example.grooveboard.domain.model.Ticket
import com.example.grooveboard.domain.model.TicketStatus
import com.example.grooveboard.domain.model.User
import kotlinx.coroutines.flow.Flow

interface TicketRepository {
    val tickets: Flow<List<Ticket>>
    suspend fun getTicketById(id: String): Ticket?
    suspend fun createTicket(ticket: Ticket): Result<Ticket>
    suspend fun updateTicketStatus(ticketId: String, newStatus: TicketStatus): Result<Unit>
    suspend fun assignTicket(ticketId: String, agent: User): Result<Unit>
    suspend fun addComment(ticketId: String, comment: Comment): Result<Unit>
    suspend fun getClients(): List<Client>
    suspend fun getAgents(): List<User>
}
