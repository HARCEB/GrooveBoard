package com.example.proyectoaplicativo.data.remote.dto

import com.example.proyectoaplicativo.domain.model.Cliente

data class ClienteDto(
    val id: String?,
    val nombre: String?
){
    fun toDomain(): Cliente{
        return Cliente(
            id = id.orEmpty(),
            nombre = nombre.orEmpty()
        )
    }
}
