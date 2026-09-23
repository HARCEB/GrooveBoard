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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.grooveboard.domain.model.Priority
import com.example.grooveboard.ui.theme.GrooveBackground
import com.example.grooveboard.ui.theme.GrooveBlue
import com.example.grooveboard.ui.theme.GrooveCyan
import com.example.grooveboard.ui.theme.GrooveNavy
import com.example.grooveboard.ui.theme.PriorityHighContainer
import com.example.grooveboard.ui.theme.PriorityHighRed
import com.example.grooveboard.ui.theme.TextMuted
import com.example.grooveboard.ui.theme.TextSecondary
import com.example.grooveboard.ui.theme.TypeStoryContainer
import com.example.grooveboard.ui.theme.TypeStoryPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUserStoryScreen(
    onNavigateBack: () -> Unit,
    viewModel: CreateUserStoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var clientMenuExpanded by remember { mutableStateOf(false) }
    var agentMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.storyCreatedEvent.collect {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFFC084FC),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Historia Scrum (HU03)",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
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
            // Banner explicativo Scrum
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = TypeStoryContainer)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = TypeStoryPurple,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Formato Ágil de Historia de Usuario",
                            fontWeight = FontWeight.Bold,
                            color = TypeStoryPurple,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Para solicitudes de mejora y requerimientos cloud, documentados bajo el estándar Como / Quiero / Para.",
                            style = MaterialTheme.typography.bodySmall,
                            color = GrooveNavy
                        )
                    }
                }
            }

            // Error Banner
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

            // Cliente
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Cliente que solicita la mejora",
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
                            shape = RoundedCornerShape(10.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = clientMenuExpanded,
                            onDismissRequest = { clientMenuExpanded = false }
                        ) {
                            uiState.clients.forEach { client ->
                                DropdownMenuItem(
                                    text = { Text("${client.name} (${client.code})") },
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

            // Formato Scrum: Como / Quiero / Para
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Estructura de la Historia",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = GrooveNavy
                    )

                    OutlinedTextField(
                        value = uiState.asA,
                        onValueChange = viewModel::onAsAChange,
                        label = { Text("Como... (Rol del solicitante)") },
                        placeholder = { Text("Ej: Administrador de base de datos de BCP") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = uiState.iWant,
                        onValueChange = viewModel::onIWantChange,
                        label = { Text("Quiero... (Requerimiento o funcionalidad)") },
                        placeholder = { Text("Ej: aprovisionar una réplica de lectura RDS en Multi-AZ") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = uiState.soThat,
                        onValueChange = viewModel::onSoThatChange,
                        label = { Text("Para... (Beneficio u objetivo de negocio)") },
                        placeholder = { Text("Ej: evitar saturar la base primaria durante las ventas navideñas") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }

            // Story Points (Estimación Scrum)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Estimación en Story Points (SP)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = GrooveNavy
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val fibonacci = listOf(1, 2, 3, 5, 8, 13)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        fibonacci.forEach { points ->
                            FilterChip(
                                selected = uiState.storyPoints == points,
                                onClick = { viewModel.onStoryPointsChange(points) },
                                label = { Text("$points SP") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = TypeStoryPurple,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // Criterios de Aceptación
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Criterios de Aceptación",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = GrooveNavy
                        )
                        OutlinedButton(
                            onClick = viewModel::addCriterion,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Agregar", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    uiState.criteriaList.forEachIndexed { index, criterion ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = criterion,
                                onValueChange = { viewModel.onCriterionChange(index, it) },
                                placeholder = { Text("Criterio ${index + 1}: Ej. Latencia < 50ms") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true
                            )
                            if (uiState.criteriaList.size > 1) {
                                IconButton(onClick = { viewModel.removeCriterion(index) }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Eliminar",
                                        tint = TextSecondary
                                    )
                                }
                            }
                        }
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
                        text = "Prioridad SLA",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = GrooveNavy
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

            // Asignación
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Asignar a Agente / Líder",
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
                                Icon(Icons.Default.Person, contentDescription = null, tint = TypeStoryPurple)
                            },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = agentMenuExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(10.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = agentMenuExpanded,
                            onDismissRequest = { agentMenuExpanded = false }
                        ) {
                            uiState.agents.forEach { agent ->
                                DropdownMenuItem(
                                    text = { Text("${agent.name} - ${agent.roleTitle}") },
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
                onClick = viewModel::createStory,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TypeStoryPurple),
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
                        "Guardar Historia en el Tablero",
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
