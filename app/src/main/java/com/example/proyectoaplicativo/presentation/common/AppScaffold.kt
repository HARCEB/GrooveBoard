package com.example.proyectoaplicativo.presentation.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.proyectoaplicativo.presentation.event.UIEvent
import com.example.proyectoaplicativo.presentation.event.UIEventBus
import com.example.proyectoaplicativo.presentation.navigation.RutasNav
import kotlinx.coroutines.flow.collectLatest


@Composable
fun AppScaffold(
    navController: NavHostController,
    eventBus: UIEventBus,
    content: @Composable (PaddingValues) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarType by remember { mutableStateOf(SnackbarType.SUCCESS) }
    // Escucha eventos globales enviados por cualquier ViewModel
    LaunchedEffect(Unit) {
        eventBus.events.collectLatest { event ->
            when (event) {
                is UIEvent.ShowSnackBar -> {
                    snackbarType = event.type
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    // En la pantalla de Login no mostramos barras superior ni inferior
    val esLogin = currentRoute == RutasNav.LOGIN
    Scaffold(
        snackbarHost = { CustomSnackbarHost(hostState = snackbarHostState, currentType = snackbarType) },
        topBar = { if (!esLogin) AppTopBar(navController) },
        bottomBar = { if (!esLogin) AppBottomBar(navController) }
    ) { paddingValues ->
        content(paddingValues)
    }
}