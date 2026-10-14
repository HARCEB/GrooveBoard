package com.example.proyectoaplicativo.domain.usecase

import com.example.proyectoaplicativo.domain.repository.TicketRepository
import javax.inject.Inject

class EliminarTicketUseCase @Inject constructor(
    private val repository: TicketRepository
) {
    suspend operator fun invoke(id: String): Result<Unit>{
        return repository.eliminarTicket(id)
    }
}