package com.example.grooveboard.domain.model

data class Comment(
    val id: String,
    val authorName: String,
    val authorRole: String,
    val authorInitials: String,
    val content: String,
    val timestamp: String
)
