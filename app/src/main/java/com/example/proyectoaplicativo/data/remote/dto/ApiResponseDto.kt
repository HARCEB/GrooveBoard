package com.example.proyectoaplicativo.data.remote.dto

import java.sql.Timestamp

data class ApiResponseDto<T>(
    val success: Boolean,
    val data: T,
    val timestamp: String? = null
)
