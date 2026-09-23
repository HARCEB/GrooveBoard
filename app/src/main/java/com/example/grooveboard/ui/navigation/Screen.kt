package com.example.grooveboard.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Board : Screen("board")
    object CreateTicket : Screen("create_ticket")
    object CreateUserStory : Screen("create_user_story")
    object Profile : Screen("profile")
    object TicketDetail : Screen("ticket_detail/{ticketId}") {
        fun createRoute(ticketId: String) = "ticket_detail/$ticketId"
    }
}
