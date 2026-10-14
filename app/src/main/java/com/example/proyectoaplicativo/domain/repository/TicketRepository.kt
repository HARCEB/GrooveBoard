package com.example.proyectoaplicativo.domain.repository

import com.example.proyectoaplicativo.domain.model.Cliente
import com.example.proyectoaplicativo.domain.model.Ticket
import com.example.proyectoaplicativo.domain.model.Usuario

interface TicketRepository {
    suspend fun getTickets(): Result<List<Ticket>>
    suspend fun getTicketById(id: String): Result<Ticket>
    suspend fun insertarTicket(ticket: Ticket): Result<Unit>
    suspend fun actualizarTicket(id: String, ticket: Ticket): Result<Unit>
    suspend fun eliminarTicket(id: String): Result<Unit>
    suspend fun getClientes(): Result<List<Cliente>>
    suspend fun login(correo: String, contrasena: String): Result<Usuario>
}
