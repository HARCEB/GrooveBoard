package com.example.proyectoaplicativo.data.repository

import com.example.proyectoaplicativo.data.remote.api.TicketApiService
import com.example.proyectoaplicativo.data.remote.dto.InsertarTicketDto
import com.example.proyectoaplicativo.domain.model.Cliente
import com.example.proyectoaplicativo.domain.model.Ticket
import com.example.proyectoaplicativo.domain.model.Usuario
import com.example.proyectoaplicativo.domain.repository.TicketRepository
import javax.inject.Inject

class TicketRepositoryImpl @Inject constructor(
    private val apiTicket: TicketApiService
) : TicketRepository {
    override suspend fun getTickets(): Result<List<Ticket>> = runCatching {
        val response = apiTicket.getTickets()
        if (response.success) {
            response.data.items.map { it.toDomain() }
        } else {
            throw Exception("Error en la respuesta del servidor al obtener tickets")
        }
    }

    override suspend fun getTicketById(id: String): Result<Ticket> = runCatching {
        val response = apiTicket.getTicketById(id)
        if (response.success && response.data != null) {
            response.data.toDomain()
        } else {
            throw Exception("No se pudo obtener el detalle del ticket")
        }
    }

    override suspend fun insertarTicket(ticket: Ticket): Result<Unit> = runCatching {
        val dto = InsertarTicketDto(
            codigo = ticket.codigo,
            titulo = ticket.titulo,
            descripcion = ticket.descripcion,
            estado = ticket.estado,
            prioridad = ticket.prioridad,
            cliente = ticket.cliente
        )
        val response = apiTicket.insertarTicket(dto)
        if (!response.success) {
            throw Exception("No se pudo registrar el ticket en el servidor")
        }
    }

    override suspend fun actualizarTicket(id: String, ticket: Ticket): Result<Unit> = runCatching {
        val dto = InsertarTicketDto(
            codigo = ticket.codigo,
            titulo = ticket.titulo,
            descripcion = ticket.descripcion,
            estado = ticket.estado,
            prioridad = ticket.prioridad,
            cliente = ticket.cliente
        )
        val response = apiTicket.actualizarTicket(id, dto)
        if (!response.success) {
            throw Exception("No se pudo actualizar el ticket en el servidor")
        }
    }

    override suspend fun eliminarTicket(id: String): Result<Unit> = runCatching {
        val response = apiTicket.eliminarTicket(id)
        if (!response.isSuccessful) {
            throw Exception("Error del servidor al eliminar el ticket: código ${response.code()}")
        }
    }

    override suspend fun getClientes(): Result<List<Cliente>> = runCatching {
        val response = apiTicket.getClientes()
        if (response.success) {
            response.data.items.map { it.toDomain() }
        } else {
            throw Exception("Error al cargar la lista de clientes")
        }
    }

    override suspend fun login(correo: String, contrasena: String): Result<Usuario> = runCatching {
        val userLimpio = correo.trim().lowercase()
        val passLimpia = contrasena.trim()
        //usuario admin
        if (userLimpio == "admin" && (passLimpia == "123456" || passLimpia == "admin")) {
            Usuario(
                id = "usr-01",
                nombre = "Administrador",
                correo = "admin@groove.com",
                rol = "Admin"
            )
        } else {
            throw Exception("Usuario o contraseña incorrectos")
        }
    }
}