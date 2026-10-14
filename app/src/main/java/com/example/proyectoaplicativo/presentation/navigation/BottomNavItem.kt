package com.example.proyectoaplicativo.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
   object Inicio : BottomNavItem(RutasNav.INICIO, "Tickets", Icons.Default.Home)
    object Form : BottomNavItem(RutasNav.FORMULARIO, "Nuevo", Icons.Default.Add) {
        fun passID(id: String? = null): String = if (id !=null) "form?id=$id" else "form"
    }
    object Buscar : BottomNavItem(RutasNav.BUSCAR, "Buscar", Icons.Default.Search)
    object Detalle : BottomNavItem(RutasNav.DETALLE, "Detalle", Icons.Default.ConfirmationNumber){
        fun passID(id: String): String = "detalle/$id"
    }
}