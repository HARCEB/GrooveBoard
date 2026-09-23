package com.example.grooveboard.domain.model

data class Client(
    val id: String,
    val name: String,
    val code: String,
    val environment: String,
    val slaLevel: String
)
