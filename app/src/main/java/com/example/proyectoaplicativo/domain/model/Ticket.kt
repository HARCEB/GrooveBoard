package com.example.proyectoaplicativo.domain.model

import android.R

data class Ticket(
    val id: String,
    val codigo: String,
    val titulo: String,
    val descripcion: String,
    val estado: String,
    val prioridad: String,
    val cliente: String
)

