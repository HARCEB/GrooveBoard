package com.example.grooveboard.domain.model

data class UserStoryDetails(
    val asA: String,                  // Como [rol]
    val iWant: String,                // Quiero [funcionalidad]
    val soThat: String,               // Para [beneficio]
    val acceptanceCriteria: List<String> = emptyList(),
    val storyPoints: Int = 3
)
