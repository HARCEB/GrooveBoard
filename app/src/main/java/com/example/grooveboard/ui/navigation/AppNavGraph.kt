package com.example.grooveboard.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.grooveboard.ui.auth.LoginScreen
import com.example.grooveboard.ui.board.BoardScreen
import com.example.grooveboard.ui.profile.ProfileScreen
import com.example.grooveboard.ui.ticket.CreateTicketScreen
import com.example.grooveboard.ui.ticket.CreateUserStoryScreen
import com.example.grooveboard.ui.ticket.TicketDetailScreen

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Board.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Board.route) {
            BoardScreen(
                onNavigateToDetail = { ticketId ->
                    navController.navigate(Screen.TicketDetail.createRoute(ticketId))
                },
                onNavigateToCreateTicket = {
                    navController.navigate(Screen.CreateTicket.route)
                },
                onNavigateToCreateStory = {
                    navController.navigate(Screen.CreateUserStory.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        composable(Screen.CreateTicket.route) {
            CreateTicketScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.CreateUserStory.route) {
            CreateUserStoryScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.TicketDetail.route,
            arguments = listOf(
                navArgument("ticketId") { type = NavType.StringType }
            )
        ) {
            TicketDetailScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
