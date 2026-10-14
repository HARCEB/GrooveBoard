package com.example.proyectoaplicativo.domain.usecase

import com.example.proyectoaplicativo.domain.model.Ticket
import com.example.proyectoaplicativo.domain.repository.TicketRepository
import javax.inject.Inject

class GetTicketUseCase @Inject constructor(
    private val repository: TicketRepository
){
    suspend operator fun invoke(id: String): Result<Ticket>{
        return repository.getTicketById(id)
    }
}