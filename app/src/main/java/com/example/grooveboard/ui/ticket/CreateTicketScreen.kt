package com.example.grooveboard.ui.ticket

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.grooveboard.domain.model.Priority
import com.example.grooveboard.domain.model.TicketType
import com.example.grooveboard.ui.theme.GrooveBackground
import com.example.grooveboard.ui.theme.GrooveBlue
import com.example.grooveboard.ui.theme.GrooveCyan
import com.example.grooveboard.ui.theme.GrooveNavy
import com.example.grooveboard.ui.theme.PriorityHighContainer
import com.example.grooveboard.ui.theme.PriorityHighRed
import com.example.grooveboard.ui.theme.TextMuted
import com.example.grooveboard.ui.theme.TextSecondary
import com.example.grooveboard.ui.theme.TypeIncidentRed
import com.example.grooveboard.ui.theme.TypeTaskBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTicketScreen(
    onNavigateBack: () -> Unit,
    viewModel: CreateTicketViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var clientMenuExpanded by remember { mutableStateOf(false) }
    var agentMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.ticketCreatedEvent.collect {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Nuevo Ticket / Tarea",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GrooveNavy)
            )
        },
        containerColor = GrooveBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Error banner if any
            if (uiState.errorMessage != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = PriorityHighContainer)
                ) {
                    Text(
                        text = uiState.errorMessage!!,
                        color = PriorityHighRed,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // Cliente selector
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Cliente Corporativo",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = GrooveNavy
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    ExposedDropdownMenuBox(
                        expanded = clientMenuExpanded,
                        onExpandedChange = { clientMenuExpanded = !clientMenuExpanded }
                    ) {
                        OutlinedTextField(
                            value = uiState.selectedClient?.name ?: "Seleccione un cliente",
                            onValueChange = {},
                            readOnly = true,
                            leadingIcon = {
                                Icon(Icons.Default.Business, contentDescription = null, tint = GrooveCyan)
                            },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = clientMenuExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = clientMenuExpanded,
                            onDismissRequest = { clientMenuExpanded = false }
                        ) {
                            uiState.clients.forEach { client ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(client.name, fontWeight = FontWeight.SemiBold)
                                            Text(
                                                "${client.environment} • ${client.slaLevel}",
                                                fontSize = 11.sp,
                                                color = TextSecondary
                                            )
                                        }
                                    },
                                    onClick = {
                                        viewModel.onClientSelect(client)
                                        clientMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Tipo de Ticket (Incidencia vs Tarea)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Tipo de Requerimiento",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = GrooveNavy
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FilterChip(
                            selected = uiState.selectedType == TicketType.INCIDENT,
                            onClick = { viewModel.onTypeSelect(TicketType.INCIDENT) },
                            label = { Text("Incidencia (Falla)") },
                            leadingIcon = {
                                Icon(Icons.Default.Warning, contentDescription = null)
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = TypeIncidentRed,
                                selectedLabelColor = Color.White,
                                selectedLeadingIconColor = Color.White
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        FilterChip(
                            selected = uiState.selectedType == TicketType.TASK,
                            onClick = { viewModel.onTypeSelect(TicketType.TASK) },
                            label = { Text("Tarea Operativa") },
                            leadingIcon = {
                                Icon(Icons.Default.Assignment, contentDescription = null)
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = TypeTaskBlue,
                                selectedLabelColor = Color.White,
                                selectedLeadingIconColor = Color.White
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Prioridad SLA
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Nivel de Prioridad (SLA)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = GrooveNavy
                    )
                    Text(
                        text = "Define el plazo máximo de atención comprometido con el cliente",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Priority.values().forEach { prio ->
                            FilterChip(
                                selected = uiState.selectedPriority == prio,
                                onClick = { viewModel.onPrioritySelect(prio) },
                                label = { Text("${prio.displayName} (${prio.slaHours}h)") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = if (prio == Priority.HIGH) PriorityHighRed else GrooveBlue,
                                    selectedLabelColor = Color.White
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Información Técnica: Título y Descripción
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Detalle del Ticket",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = GrooveNavy
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = uiState.title,
                        onValueChange = viewModel::onTitleChange,
                        label = { Text("Título del Ticket") },
                        placeholder = { Text("Ej: Error 502 Bad Gateway en backend de pagos") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = uiState.description,
                        onValueChange = viewModel::onDescriptionChange,
                        label = { Text("Descripción Técnica y Pasos de Reproducción") },
                        placeholder = { Text("Describa el comportamiento observado, servicios cloud afectados, logs o URLs...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        shape = RoundedCornerShape(10.dp),
                        maxLines = 6
                    )
                }
            }

            // Agente Responsable (HU05)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Asignación de Agente Responsable",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = GrooveNavy
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    ExposedDropdownMenuBox(
                        expanded = agentMenuExpanded,
                        onExpandedChange = { agentMenuExpanded = !agentMenuExpanded }
                    ) {
                        OutlinedTextField(
                            value = uiState.selectedAgent?.name ?: "Seleccione un agente",
                            onValueChange = {},
                            readOnly = true,
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = GrooveCyan)
                            },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = agentMenuExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = agentMenuExpanded,
                            onDismissRequest = { agentMenuExpanded = false }
                        ) {
                            uiState.agents.forEach { agent ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(agent.name, fontWeight = FontWeight.SemiBold)
                                            Text(
                                                "${agent.roleTitle} (${agent.role.displayName})",
                                                fontSize = 11.sp,
                                                color = TextSecondary
                                            )
                                        }
                                    },
                                    onClick = {
                                        viewModel.onAgentSelect(agent)
                                        agentMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Botón Guardar
            Button(
                onClick = viewModel::createTicket,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GrooveNavy),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Crear Ticket en 'Por hacer'",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
