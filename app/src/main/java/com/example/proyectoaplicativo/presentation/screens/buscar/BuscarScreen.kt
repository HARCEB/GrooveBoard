package com.example.proyectoaplicativo.presentation.screens.buscar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyectoaplicativo.presentation.common.LoadingOverlay
import com.example.proyectoaplicativo.presentation.common.TicketItem


@Composable
fun BuscarScreen(
    onDetalleTicket: (String) -> Unit,
    viewModel: BuscarViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colorAzul = Color(0xFF0D47A1)
    // Refresca la lista de tickets de AWS al entrar
    LaunchedEffect(Unit) {
        viewModel.cargarTickets()
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Campo de Búsqueda
            OutlinedTextField(
                value = uiState.query,
                onValueChange = viewModel::onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar por título, cliente o código...", color = Color(0xFF94A3B8)) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = colorAzul)
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorAzul,
                    unfocusedBorderColor = Color(0xFFCBD5E1)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Si no hay resultados para el filtro
            if (!uiState.isLoading && uiState.filteredTickets.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Outlined.SearchOff,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (uiState.query.isBlank()) "No hay tickets registrados" else "No se encontraron coincidencias",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            } else {
                // Lista de Tickets filtrados
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.filteredTickets, key = { it.id }) { ticket ->
                        TicketItem(
                            ticket = ticket,
                            onDetalleClick = { onDetalleTicket(ticket.id) }
                        )
                    }
                }
            }
        }
        LoadingOverlay(isLoading = uiState.isLoading)
    }
}