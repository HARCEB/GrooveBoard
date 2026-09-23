package com.example.grooveboard.domain.model

enum class TicketStatus(val displayName: String) {
    TODO("Por hacer"),
    IN_PROGRESS("En proceso"),
    RESOLVED("Resuelto")
}

enum class Priority(val displayName: String, val slaHours: Int) {
    HIGH("Alta", 2),
    MEDIUM("Media", 8),
    LOW("Baja", 24)
}

enum class TicketType(val displayName: String) {
    INCIDENT("Incidencia"),
    TASK("Tarea"),
    USER_STORY("Historia de Usuario")
}

data class Ticket(
    val id: String,
    val code: String,
    val title: String,
    val description: String,
    val status: TicketStatus,
    val priority: Priority,
    val type: TicketType,
    val client: Client,
    val assignedAgent: User?,
    val userStoryDetails: UserStoryDetails? = null,
    val comments: List<Comment> = emptyList(),
    val createdAt: String,
    val slaDeadline: String
)
