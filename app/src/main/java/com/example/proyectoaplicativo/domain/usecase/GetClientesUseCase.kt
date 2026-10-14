package com.example.proyectoaplicativo.domain.usecase

import com.example.proyectoaplicativo.domain.model.Cliente
import com.example.proyectoaplicativo.domain.repository.TicketRepository
import javax.inject.Inject

class GetClientesUseCase @Inject constructor(
    private val repository: TicketRepository
) {
    suspend operator fun invoke(): Result<List<Cliente>>{
        return repository.getClientes()
    }
}