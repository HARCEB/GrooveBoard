package com.example.proyectoaplicativo.data.remote.dto

data class TicketsDataDto(
    val items: List<TicketDto> = emptyList(),
    val nextkey: String? = null
)
