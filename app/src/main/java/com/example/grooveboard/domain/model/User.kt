package com.example.grooveboard.domain.model

enum class UserRole(val displayName: String) {
    ADMIN("Líder / Administrador"),
    AGENT("Agente de Soporte Cloud")
}

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole,
    val avatarInitials: String,
    val roleTitle: String,
    val activeTicketsCount: Int = 0,
    val resolvedTicketsCount: Int = 0
)
