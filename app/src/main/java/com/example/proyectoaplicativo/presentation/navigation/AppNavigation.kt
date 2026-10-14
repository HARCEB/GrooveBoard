package com.example.proyectoaplicativo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.proyectoaplicativo.presentation.screens.buscar.BuscarScreen
import com.example.proyectoaplicativo.presentation.screens.detalle.DetalleScreen
import com.example.proyectoaplicativo.presentation.screens.form.FormScreen
import com.example.proyectoaplicativo.presentation.screens.inicio.InicioScreen
import com.example.proyectoaplicativo.presentation.screens.login.LoginScreen
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = RutasNav.LOGIN
    ) {
        //Login
        composable(RutasNav.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(BottomNavItem.Inicio.route) {
                        popUpTo(RutasNav.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        //Inicio (Lista de tickets)
        composable(BottomNavItem.Inicio.route) {
            InicioScreen(
                onDetalleTicket = { ticketId ->
                    navController.navigate(BottomNavItem.Detalle.passID(ticketId))
                }
            )
        }
        //Formulario (Crear Ticket)
        composable(
            route = BottomNavItem.Form.route,
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) {
            FormScreen(onSuccess = { navController.popBackStack() })
        }
        //Detalle del Ticket
        composable(
            route = BottomNavItem.Detalle.route,
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            })
        ) {
            DetalleScreen(
                onTicketDeleted = {
                    navController.popBackStack()
                }
            )
        }
        //Búsqueda de Tickets
        composable(BottomNavItem.Buscar.route) {
            BuscarScreen(
                onDetalleTicket = { ticketId ->
                    navController.navigate(BottomNavItem.Detalle.passID(ticketId))
                }
            )
        }
    }
}