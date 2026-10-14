package com.example.proyectoaplicativo.domain.usecase

import com.example.proyectoaplicativo.domain.model.Usuario
import com.example.proyectoaplicativo.domain.repository.TicketRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: TicketRepository
) {
    suspend operator fun invoke(correo: String, contrasena: String): Result<Usuario>{
        return repository.login(correo, contrasena)
    }
}