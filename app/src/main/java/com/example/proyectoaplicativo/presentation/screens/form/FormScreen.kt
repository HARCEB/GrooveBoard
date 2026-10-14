package com.example.proyectoaplicativo.presentation.screens.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyectoaplicativo.presentation.common.LoadingOverlay
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    onSuccess: () -> Unit,
    viewModel: FormViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    var clienteMenuExpanded by remember { mutableStateOf(false) }
    var prioridadMenuExpanded by remember { mutableStateOf(false) }
    var estadoMenuExpanded by remember { mutableStateOf(false) }
    val colorAzul = Color(0xFF0D47A1)
    val colorEtiqueta = Color(0xFF334155)
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Text(
                text = if (uiState.isEditing) "Editar Ticket" else "Nuevo Ticket de Soporte",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = colorAzul
            )
            Text(
                text = "Completa los datos",
                fontSize = 13.sp,
                color = Color(0xFF64748B)
            )
            Spacer(modifier = Modifier.height(18.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    // TÍTULO DEL TICKET
                    Text("Título del Ticket", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = colorEtiqueta)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = uiState.titulo,
                        onValueChange = viewModel::onTituloChange,
                        placeholder = { Text("Ej. Falla de conexión a base de datos") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // CLIENTE (Desplegable)
                    Text("Cliente / Empresa", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = colorEtiqueta)
                    Spacer(modifier = Modifier.height(6.dp))
                    ExposedDropdownMenuBox(
                        expanded = clienteMenuExpanded,
                        onExpandedChange = { clienteMenuExpanded = !clienteMenuExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = uiState.cliente.ifBlank { "Seleccionar cliente..." },
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = clienteMenuExpanded) },
                            modifier = Modifier
                                .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = clienteMenuExpanded,
                            onDismissRequest = { clienteMenuExpanded = false }
                        ) {
                            uiState.clientes.forEach { cliente ->
                                DropdownMenuItem(
                                    text = { Text(cliente.nombre) },
                                    onClick = {
                                        viewModel.onClienteChange(cliente.nombre)
                                        clienteMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    // 3. PRIORIDAD Y ESTADO (En dos columnas)
                    Row(modifier = Modifier.fillMaxWidth()) {
                        // Selector Prioridad
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Prioridad", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = colorEtiqueta)
                            Spacer(modifier = Modifier.height(6.dp))
                            ExposedDropdownMenuBox(
                                expanded = prioridadMenuExpanded,
                                onExpandedChange = { prioridadMenuExpanded = !prioridadMenuExpanded },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = uiState.prioridad,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = prioridadMenuExpanded) },
                                    modifier = Modifier
                                        .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                ExposedDropdownMenu(
                                    expanded = prioridadMenuExpanded,
                                    onDismissRequest = { prioridadMenuExpanded = false }
                                ) {
                                    listOf("Alta", "Media", "Baja").forEach { opcion ->
                                        DropdownMenuItem(
                                            text = { Text(opcion) },
                                            onClick = {
                                                viewModel.onPrioridadChange(opcion)
                                                prioridadMenuExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.padding(horizontal = 6.dp))
                        // Selector Estado
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Estado", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = colorEtiqueta)
                            Spacer(modifier = Modifier.height(6.dp))
                            ExposedDropdownMenuBox(
                                expanded = estadoMenuExpanded,
                                onExpandedChange = { estadoMenuExpanded = !estadoMenuExpanded },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = uiState.estado,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = estadoMenuExpanded) },
                                    modifier = Modifier
                                        .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                ExposedDropdownMenu(
                                    expanded = estadoMenuExpanded,
                                    onDismissRequest = { estadoMenuExpanded = false }
                                ) {
                                    listOf("Abierto", "En Proceso", "Resuelto").forEach { opcion ->
                                        DropdownMenuItem(
                                            text = { Text(opcion) },
                                            onClick = {
                                                viewModel.onEstadoChange(opcion)
                                                estadoMenuExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    // 4. DESCRIPCIÓN
                    Text("Descripción del problema", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = colorEtiqueta)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = uiState.descripcion,
                        onValueChange = viewModel::onDescripcionChange,
                        placeholder = { Text("Escribe los detalles técnicos del incidente...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        minLines = 4
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    // BOTÓN GUARDAR
                    Button(
                        onClick = { viewModel.guardarTicket(onSuccess) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorAzul)
                    ) {
                        Text(
                            text = if (uiState.isEditing) "Actualizar Ticket" else "Registrar Ticket",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        LoadingOverlay(isLoading = uiState.isLoading)
    }
}