package com.example.grooveboard.ui.board

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.example.grooveboard.domain.model.TicketStatus
import com.example.grooveboard.ui.board.components.TicketCard
import com.example.grooveboard.ui.theme.GrooveBackground
import com.example.grooveboard.ui.theme.GrooveBlue
import com.example.grooveboard.ui.theme.GrooveCyan
import com.example.grooveboard.ui.theme.GrooveNavy
import com.example.grooveboard.ui.theme.PriorityHighRed
import com.example.grooveboard.ui.theme.TextMuted
import com.example.grooveboard.ui.theme.TextSecondary
import com.example.grooveboard.ui.theme.TypeStoryPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardScreen(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToCreateTicket: () -> Unit,
    onNavigateToCreateStory: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: BoardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showFabMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(GrooveCyan),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cloud,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "GrooveBoard",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Tablero Kanban • GROOVE",
                                style = MaterialTheme.typography.labelSmall,
                                color = GrooveCyan.copy(alpha = 0.9f)
                            )
                        }
                    }
                },
                actions = {
                    // Profile button with user initials
                    IconButton(onClick = onNavigateToProfile) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(GrooveCyan),
                            contentAlignment = Alignment.Center
                        ) {
                            if (uiState.currentUser != null) {
                                Text(
                                    text = uiState.currentUser!!.avatarInitials,
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = "Perfil",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GrooveNavy)
            )
        },
        floatingActionButton = {
            Box {
                ExtendedFloatingActionButton(
                    onClick = { showFabMenu = true },
                    containerColor = GrooveNavy,
                    contentColor = Color.White,
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("Nuevo Requerimiento", fontWeight = FontWeight.Bold) }
                )

                DropdownMenu(
                    expanded = showFabMenu,
                    onDismissRequest = { showFabMenu = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text("Registrar Ticket / Tarea", fontWeight = FontWeight.SemiBold)
                                Text("Incidencias o tareas operativas", fontSize = 11.sp, color = TextSecondary)
                            }
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Assignment, contentDescription = null, tint = GrooveCyan)
                        },
                        onClick = {
                            showFabMenu = false
                            onNavigateToCreateTicket()
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Column {
                                Text("Registrar Historia Scrum", fontWeight = FontWeight.SemiBold)
                                Text("Mejoras en formato Como/Quiero/Para", fontSize = 11.sp, color = TypeStoryPurple)
                            }
                        },
                        leadingIcon = {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = TypeStoryPurple)
                        },
                        onClick = {
                            showFabMenu = false
                            onNavigateToCreateStory()
                        }
                    )
                }
            }
        },
        containerColor = GrooveBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Buscador
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = viewModel::onSearchQueryChanged,
                placeholder = { Text("Buscar ticket por código, título o detalle...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = GrooveCyan)
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            // Chips de Filtro por Cliente
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filtro",
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                )

                FilterChip(
                    selected = uiState.selectedClientId == null,
                    onClick = { viewModel.onClientFilterChanged(null) },
                    label = { Text("Todos los Clientes") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GrooveNavy,
                        selectedLabelColor = Color.White
                    )
                )

                uiState.allClients.forEach { client ->
                    FilterChip(
                        selected = uiState.selectedClientId == client.id,
                        onClick = { viewModel.onClientFilterChanged(client.id) },
                        label = { Text(client.code) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GrooveCyan,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            // Chips de Prioridad (Alta / Media / Baja)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = uiState.selectedPriority == null,
                    onClick = { viewModel.onPriorityFilterChanged(null) },
                    label = { Text("Cualquier Prioridad") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GrooveNavy,
                        selectedLabelColor = Color.White
                    )
                )

                Priority.values().forEach { prio ->
                    FilterChip(
                        selected = uiState.selectedPriority == prio,
                        onClick = {
                            if (uiState.selectedPriority == prio) {
                                viewModel.onPriorityFilterChanged(null)
                            } else {
                                viewModel.onPriorityFilterChanged(prio)
                            }
                        },
                        label = { Text("Prioridad ${prio.displayName}") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = if (prio == Priority.HIGH) PriorityHighRed else GrooveBlue,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Kanban Tab Row (HU04)
            SecondaryTabRow(
                selectedTabIndex = uiState.selectedStatusTab.ordinal,
                containerColor = Color.White,
                contentColor = GrooveNavy,
                indicator = {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(uiState.selectedStatusTab.ordinal),
                        color = GrooveCyan
                    )
                }
            ) {
                val tabs = listOf(
                    Triple(TicketStatus.TODO, "Por hacer", uiState.todoTickets.size),
                    Triple(TicketStatus.IN_PROGRESS, "En proceso", uiState.inProgressTickets.size),
                    Triple(TicketStatus.RESOLVED, "Resuelto", uiState.resolvedTickets.size)
                )

                tabs.forEach { (status, label, count) ->
                    Tab(
                        selected = uiState.selectedStatusTab == status,
                        onClick = { viewModel.onTabSelected(status) },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = label,
                                    fontWeight = if (uiState.selectedStatusTab == status) FontWeight.Bold else FontWeight.Normal
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(if (uiState.selectedStatusTab == status) GrooveCyan else Color(0xFFCBD5E1))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = count.toString(),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (uiState.selectedStatusTab == status) Color.White else TextSecondary
                                    )
                                }
                            }
                        }
                    )
                }
            }

            // Lista de Tickets según la columna Kanban seleccionada
            val activeTickets = when (uiState.selectedStatusTab) {
                TicketStatus.TODO -> uiState.todoTickets
                TicketStatus.IN_PROGRESS -> uiState.inProgressTickets
                TicketStatus.RESOLVED -> uiState.resolvedTickets
            }

            if (activeTickets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Assignment,
                            contentDescription = null,
                            modifier = Modifier.size(56.dp),
                            tint = TextMuted
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No hay tickets en esta columna",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondary
                        )
                        Text(
                            text = "Utiliza el botón inferior para registrar un nuevo ticket o historia de usuario.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(activeTickets, key = { it.id }) { ticket ->
                        TicketCard(
                            ticket = ticket,
                            onClick = { onNavigateToDetail(ticket.id) },
                            onMoveStatus = { newStatus ->
                                viewModel.updateTicketStatus(ticket.id, newStatus)
                            }
                        )
                    }
                }
            }
        }
    }
}
