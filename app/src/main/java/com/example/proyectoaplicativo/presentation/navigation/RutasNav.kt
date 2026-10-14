package com.example.proyectoaplicativo.presentation.navigation

object RutasNav {
    const val LOGIN = "login"
    const val INICIO = "inicio"
    const val FORMULARIO = "form?id={id}"
    const val BUSCAR = "buscar"
    const val DETALLE = "detalle/{id}"

    fun getTitulo(ruta: String?): String{
        return when {
            ruta == LOGIN -> "GrooveBoard Cloud"
            ruta == INICIO -> "Tablero de Tickets"
            ruta?.startsWith("form") == true -> "Gestion de Ticket"
            ruta?.startsWith(BUSCAR) == true -> "Busqueda de Tickets"
            ruta?.startsWith("detalle") == true -> "Detalle de Ticket"
            else -> "GrooveBoard Soporte"
        }
    }
}