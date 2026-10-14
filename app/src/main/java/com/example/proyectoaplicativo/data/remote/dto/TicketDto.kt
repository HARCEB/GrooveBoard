package com.example.proyectoaplicativo.data.remote.dto

import androidx.compose.ui.input.pointer.PointerId
import com.example.proyectoaplicativo.domain.model.Ticket

data class TicketDto(
    val id: String?,
    val codigo: String?,
    val titulo: String?,
    val descripcion: String?,
    val estado: String?,
    val prioridad: String?,
    val cliente: String?
){
    fun toDomain(): Ticket{
        return Ticket(
            id = id.orEmpty(),
            codigo = codigo.orEmpty(),
            titulo = titulo.orEmpty(),
            descripcion = descripcion.orEmpty(),
            estado = estado ?: "Abierto",
            prioridad = prioridad ?: "Media",
            cliente = cliente.orEmpty()

        )
    }

}
