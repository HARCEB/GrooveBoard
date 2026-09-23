package com.example.grooveboard.ui.ticket

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import com.example.grooveboard.domain.model.TicketType
import com.example.grooveboard.domain.model.UserRole
import com.example.grooveboard.ui.theme.GrooveBackground
import com.example.grooveboard.ui.theme.GrooveBlue
import com.example.grooveboard.ui.theme.GrooveCyan
import com.example.grooveboard.ui.theme.GrooveNavy
import com.example.grooveboard.ui.theme.PriorityHighContainer
import com.example.grooveboard.ui.theme.PriorityHighRed
import com.example.grooveboard.ui.theme.PriorityLowContainer
import com.example.grooveboard.ui.theme.PriorityLowTeal
import com.example.grooveboard.ui.theme.PriorityMediumAmber
import com.example.grooveboard.ui.theme.PriorityMediumContainer
import com.example.grooveboard.ui.theme.StatusInProgress
import com.example.grooveboard.ui.theme.StatusInProgressContainer
import com.example.grooveboard.ui.theme.StatusResolved
import com.example.grooveboard.ui.theme.StatusResolvedContainer
import com.example.grooveboard.ui.theme.StatusTodo
import com.example.grooveboard.ui.theme.StatusTodoContainer
import com.example.grooveboard.ui.theme.TextMuted
import com.example.grooveboard.ui.theme.TextSecondary
import com.example.grooveboard.ui.theme.TypeIncidentContainer
import com.example.grooveboard.ui.theme.TypeIncidentRed
import com.example.grooveboard.ui.theme.TypeStoryContainer
import com.example.grooveboard.ui.theme.TypeStoryPurple
import com.example.grooveboard.ui.theme.TypeTaskContainer
import com.example.grooveboard.ui.theme.TypeTaskBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: TicketDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val ticket = uiState.ticket

    if (ticket == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(GrooveBackground),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = GrooveNavy)
        }
        return
    }

    // Dialogo de Reasignación (HU05)
    if (uiState.showReassignDialog) {
        AlertDialog(
            onDismissRequest = viewModel::closeReassignDialog,
            title = {
                Text("Reasignar Ticket", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Seleccione el agente de soporte responsable:",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    uiState.allAgents.forEach { agent ->
                        val isSelected = ticket.assignedAgent?.id == agent.id
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.assignToAgent(agent) },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) StatusInProgressContainer else Color.White
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(GrooveBlue),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = agent.avatarInitials,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(agent.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(agent.roleTitle, fontSize = 11.sp, color = TextSecondary)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = viewModel::closeReassignDialog) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = ticket.code,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFE2E8F0))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = ticket.client.code,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GrooveNavy
                            )
                        }
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
            // Header Card: Título + Estado + Prioridad SLA
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Estado Chip
                        val (statusBg, statusColor) = when (ticket.status) {
                            TicketStatus.TODO -> StatusTodoContainer to StatusTodo
                            TicketStatus.IN_PROGRESS -> StatusInProgressContainer to StatusInProgress
                            TicketStatus.RESOLVED -> StatusResolvedContainer to StatusResolved
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(statusBg)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = ticket.status.displayName.uppercase(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = statusColor
                            )
                        }

                        // Prioridad SLA Badge
                        val (prioColor, prioBg) = when (ticket.priority) {
                            Priority.HIGH -> PriorityHighRed to PriorityHighContainer
                            Priority.MEDIUM -> PriorityMediumAmber to PriorityMediumContainer
                            Priority.LOW -> PriorityLowTeal to PriorityLowContainer
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(prioBg)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "SLA: ${ticket.priority.displayName} (${ticket.priority.slaHours}h)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = prioColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = ticket.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = GrooveNavy
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Creado el ${ticket.createdAt}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        Text(
                            text = ticket.slaDeadline,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = if (ticket.priority == Priority.HIGH) PriorityHighRed else TextSecondary
                        )
                    }
                }
            }

            // Transición rápida de Estado Kanban
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Cambiar Estado en Tablero Kanban",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = GrooveNavy
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.updateStatus(TicketStatus.TODO) },
                            enabled = ticket.status != TicketStatus.TODO,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Por hacer", fontSize = 11.sp)
                        }

                        FilledTonalButton(
                            onClick = { viewModel.updateStatus(TicketStatus.IN_PROGRESS) },
                            enabled = ticket.status != TicketStatus.IN_PROGRESS,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("En proceso", fontSize = 11.sp)
                        }

                        Button(
                            onClick = { viewModel.updateStatus(TicketStatus.RESOLVED) },
                            enabled = ticket.status != TicketStatus.RESOLVED,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = StatusResolved),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Resuelto", fontSize = 11.sp)
                        }
                    }
                }
            }

            // Datos del Cliente y Entorno Cloud
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Business, contentDescription = null, tint = GrooveCyan)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Cliente y Entorno Cloud",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = GrooveNavy
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = ticket.client.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = GrooveNavy
                    )
                    Text(
                        text = "Infraestructura: ${ticket.client.environment}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Text(
                        text = "Acuerdo de Servicio: ${ticket.client.slaLevel}",
                        style = MaterialTheme.typography.bodySmall,
                        color = GrooveBlue,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Detalle o Historia Scrum
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val typeIcon = if (ticket.type == TicketType.USER_STORY) Icons.Default.AutoAwesome else Icons.Default.Assignment
                        val typeColor = if (ticket.type == TicketType.USER_STORY) TypeStoryPurple else GrooveCyan

                        Icon(typeIcon, contentDescription = null, tint = typeColor)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (ticket.type == TicketType.USER_STORY) "Historia de Usuario (Scrum)" else "Descripción Técnica",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = GrooveNavy
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (ticket.userStoryDetails != null) {
                        val story = ticket.userStoryDetails
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(TypeStoryContainer)
                                    .padding(12.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("COMO: ${story.asA}", fontWeight = FontWeight.Bold, color = TypeStoryPurple)
                                    Text("QUIERO: ${story.iWant}", fontWeight = FontWeight.Bold, color = GrooveNavy)
                                    Text("PARA: ${story.soThat}", fontWeight = FontWeight.Bold, color = GrooveBlue)
                                    Text("ESTIMACIÓN: ${story.storyPoints} Story Points", fontSize = 11.sp, color = TextSecondary)
                                }
                            }

                            if (story.acceptanceCriteria.isNotEmpty()) {
                                Text(
                                    text = "Criterios de Aceptación:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = GrooveNavy
                                )
                                story.acceptanceCriteria.forEach { crit ->
                                    val checked = remember { androidx.compose.runtime.mutableStateOf(false) }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Checkbox(
                                            checked = checked.value,
                                            onCheckedChange = { checked.value = it }
                                        )
                                        Text(crit, fontSize = 13.sp, color = GrooveNavy)
                                    }
                                }
                            }
                        }
                    } else {
                        Text(
                            text = ticket.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = GrooveNavy,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            // Asignación de Agente (HU05)
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
                            text = "Agente Asignado (HU05)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = GrooveNavy
                        )

                        // Administrador puede reasignar; Agente puede autoasignarse
                        val isAdmin = uiState.currentUser?.role == UserRole.ADMIN
                        if (isAdmin) {
                            TextButton(onClick = viewModel::openReassignDialog) {
                                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Reasignar")
                            }
                        } else if (ticket.assignedAgent?.id != uiState.currentUser?.id) {
                            TextButton(onClick = viewModel::selfAssign) {
                                Text("Asignarme")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (ticket.assignedAgent != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(GrooveBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = ticket.assignedAgent.avatarInitials,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = ticket.assignedAgent.name,
                                    fontWeight = FontWeight.Bold,
                                    color = GrooveNavy
                                )
                                Text(
                                    text = "${ticket.assignedAgent.roleTitle} (${ticket.assignedAgent.role.displayName})",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    } else {
                        Text("Ningún agente asignado todavía.", color = TextMuted)
                    }
                }
            }

            // Comentarios y Notas de Avance (HU06)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Comment, contentDescription = null, tint = GrooveCyan)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Comentarios y Avances (${ticket.comments.size})",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = GrooveNavy
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (ticket.comments.isEmpty()) {
                        Text(
                            text = "No hay notas o comentarios registrados aún.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            ticket.comments.forEach { comment ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(GrooveBackground)
                                        .padding(12.dp)
                                ) {
                                    Column {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(24.dp)
                                                        .clip(CircleShape)
                                                        .background(GrooveCyan),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = comment.authorInitials,
                                                        color = Color.White,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = comment.authorName,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = GrooveNavy
                                                )
                                            }
                                            Text(
                                                text = comment.timestamp,
                                                fontSize = 10.sp,
                                                color = TextSecondary
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = comment.content,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = GrooveNavy
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Input para agregar nuevo comentario
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = uiState.commentInput,
                            onValueChange = viewModel::onCommentInputChange,
                            placeholder = { Text("Escribir una nota o avance técnico...") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            maxLines = 3
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = viewModel::addComment,
                            enabled = uiState.commentInput.isNotBlank() && !uiState.isSendingComment,
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(if (uiState.commentInput.isNotBlank()) GrooveNavy else Color(0xFFCBD5E1))
                        ) {
                            Icon(
                                Icons.Default.Send,
                                contentDescription = "Enviar",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
