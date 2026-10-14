package com.example.proyectoaplicativo.domain.usecase

import com.example.proyectoaplicativo.domain.model.Ticket
import com.example.proyectoaplicativo.domain.repository.TicketRepository
import javax.inject.Inject

class GetTicketsUseCase @Inject constructor(
    private val repository: TicketRepository
){
    suspend operator fun invoke(): Result<List<Ticket>>{
        return repository.getTickets()
    }
}
