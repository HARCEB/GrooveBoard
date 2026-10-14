package com.example.proyectoaplicativo.presentation.screens.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.proyectoaplicativo.presentation.common.LoadingScreen
@Composable
fun DetalleScreen(
    onTicketDeleted: () -> Unit,
    viewModel: DetalleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) {
        viewModel.cargarTicket()
    }
    val colorAzul = Color(0xFF0D47A1)
    val colorRojo = Color(0xFFB71C1C)
    if (uiState.isLoading && uiState.ticket == null) {
        LoadingScreen(mensaje = "Cargando detalle del ticket...")
    } else {
        uiState.ticket?.let { ticket ->
            val (colorFondoPrioridad, colorTextoPrioridad) = when (ticket.prioridad) {
                "Alta" -> Color(0xFFFFEBEE) to Color(0xFFC62828)
                "Media" -> Color(0xFFFFF3E0) to Color(0xFFEF6C00)
                else -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
            }
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                ) {
                    // UN SOLO RECUADRO CON TODA LA INFORMACIÓN
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            // Fila superior: Código + Badges (Prioridad y Estado)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Outlined.ConfirmationNumber,
                                        contentDescription = null,
                                        tint = colorAzul,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = ticket.codigo,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colorAzul
                                    )
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(
                                        text = ticket.prioridad,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colorTextoPrioridad,
                                        modifier = Modifier
                                            .background(colorFondoPrioridad, RoundedCornerShape(6.dp))
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                    Surface(
                                        shape = CircleShape,
                                        color = if (ticket.estado == "Resuelto") Color(0xFFE8F5E9) else Color(0xFFFFF8E1),
                                        modifier = Modifier.height(24.dp)
                                    ) {
                                        Text(
                                            text = ticket.estado,
                                            color = if (ticket.estado == "Resuelto") Color(0xFF1B5E20) else Color(0xFFF57F17),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                            // Título
                            Text(
                                text = ticket.titulo,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            // Cliente
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.Business,
                                    contentDescription = null,
                                    tint = Color(0xFF64748B),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = ticket.cliente,
                                    fontSize = 14.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            // Línea divisoria elegante
                            HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
                            Spacer(modifier = Modifier.height(14.dp))
                            // Sección: Descripción Técnica
                            Text(
                                text = "Descripción Técnica",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorAzul
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = ticket.descripcion,
                                fontSize = 14.sp,
                                lineHeight = 22.sp,
                                color = Color(0xFF334155)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    // Botón Eliminar
                    Button(
                        onClick = { viewModel.showDeleteDialog() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorRojo)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Eliminar Ticket", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                }
                // Diálogo de Confirmación
                if (uiState.showDeleteDialog) {
                    AlertDialog(
                        onDismissRequest = { viewModel.dismissDeleteDialog() },
                        title = { Text(text = "Eliminar Ticket", fontWeight = FontWeight.Bold) },
                        text = {
                            Text("¿Estás seguro de que deseas eliminar este ticket de AWS DynamoDB? Esta acción no se puede deshacer.")
                        },
                        confirmButton = {
                            TextButton(onClick = { viewModel.confirmarEliminar(onTicketDeleted) }) {
                                Text("Eliminar", color = colorRojo, fontWeight = FontWeight.Bold)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { viewModel.dismissDeleteDialog() }) {
                                Text("Cancelar", color = Color.Gray)
                            }
                        }
                    )
                }
                LoadingOverlay(isLoading = uiState.isLoading)
            }
        }
    }
}
