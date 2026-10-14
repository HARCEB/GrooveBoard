package com.example.proyectoaplicativo.domain.usecase

import com.example.proyectoaplicativo.domain.model.Ticket
import com.example.proyectoaplicativo.domain.repository.TicketRepository
import javax.inject.Inject

class ActualizarTicketUseCase @Inject constructor(
    private val repository: TicketRepository
) {
    suspend operator fun invoke(id: String, ticket: Ticket): Result<Unit> {
        return repository.actualizarTicket(id, ticket)
    }
}