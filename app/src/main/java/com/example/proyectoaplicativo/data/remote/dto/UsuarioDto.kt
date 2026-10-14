package com.example.proyectoaplicativo.data.remote.dto

import com.example.proyectoaplicativo.domain.model.Usuario

data class UsuarioDto(
    val id: String?,
    val nombre: String?,
    val correo: String?,
    val rol: String?
){
    fun toDomain(): Usuario {
        return Usuario(
            id = id.orEmpty(),
            nombre = nombre.orEmpty(),
            correo = correo.orEmpty(),
            rol = rol ?: "soporte"
        )
    }
}
