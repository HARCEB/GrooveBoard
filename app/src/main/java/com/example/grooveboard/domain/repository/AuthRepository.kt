package com.example.grooveboard.domain.repository

import com.example.grooveboard.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<User?>
    suspend fun login(email: String, password: String): Result<User>
    suspend fun logout()
    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit>
    suspend fun getCurrentUserSync(): User?
}
