package com.example.proyectoaplicativo.presentation.event

import com.example.proyectoaplicativo.presentation.common.SnackbarType

interface UIEvent {
    data class ShowSnackBar(val  message: String, val type: SnackbarType) : UIEvent
}