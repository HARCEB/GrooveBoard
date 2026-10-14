package com.example.proyectoaplicativo.data.remote.dto

data class InsertarTicketDto(
    val codigo: String,
    val titulo: String,
    val descripcion: String,
    val estado: String,
    val prioridad: String,
    val cliente: String
)
