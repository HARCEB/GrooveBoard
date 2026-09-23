package com.example.grooveboard.data.repository

import com.example.grooveboard.data.datasource.MockData
import com.example.grooveboard.domain.model.User
import com.example.grooveboard.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    // Sesión inicial con el administrador Carlos Méndez para facilitar pruebas, o nula para login
    private val _currentUser = MutableStateFlow<User?>(MockData.adminUser)
    override val currentUser: Flow<User?> = _currentUser.asStateFlow()

    override suspend fun login(email: String, password: String): Result<User> {
        val cleanEmail = email.trim().lowercase()
        val expectedPassword = MockData.passwords[cleanEmail]

        if (expectedPassword == null) {
            return Result.failure(Exception("El usuario '$email' no está registrado en el sistema de GROOVE."))
        }

        if (expectedPassword != password) {
            return Result.failure(Exception("Contraseña incorrecta. Por favor verifique sus credenciales."))
        }

        val user = MockData.allUsers.find { it.email.lowercase() == cleanEmail }
            ?: return Result.failure(Exception("No se encontró el perfil de usuario asociado."))

        _currentUser.value = user
        return Result.success(user)
    }

    override suspend fun logout() {
        _currentUser.value = null
    }

    override suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit> {
        val user = _currentUser.value ?: return Result.failure(Exception("No hay una sesión activa."))
        val currentExpected = MockData.passwords[user.email.lowercase()]

        if (currentExpected != currentPassword) {
            return Result.failure(Exception("La contraseña actual no coincide con nuestros registros."))
        }

        if (newPassword.length < 6) {
            return Result.failure(Exception("La nueva contraseña debe tener al menos 6 caracteres."))
        }

        MockData.passwords[user.email.lowercase()] = newPassword
        return Result.success(Unit)
    }

    override suspend fun getCurrentUserSync(): User? {
        return _currentUser.value
    }
}
