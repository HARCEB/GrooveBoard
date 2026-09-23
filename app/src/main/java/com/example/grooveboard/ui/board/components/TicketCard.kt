package com.example.grooveboard.ui.board.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grooveboard.domain.model.Priority
import com.example.grooveboard.domain.model.Ticket
import com.example.grooveboard.domain.model.TicketStatus
import com.example.grooveboard.domain.model.TicketType
import com.example.grooveboard.ui.theme.GrooveBlue
import com.example.grooveboard.ui.theme.GrooveCardBorder
import com.example.grooveboard.ui.theme.GrooveCyan
import com.example.grooveboard.ui.theme.GrooveNavy
import com.example.grooveboard.ui.theme.PriorityHighContainer
import com.example.grooveboard.ui.theme.PriorityHighRed
import com.example.grooveboard.ui.theme.PriorityLowContainer
import com.example.grooveboard.ui.theme.PriorityLowTeal
import com.example.grooveboard.ui.theme.PriorityMediumAmber
import com.example.grooveboard.ui.theme.PriorityMediumContainer
import com.example.grooveboard.ui.theme.TextMuted
import com.example.grooveboard.ui.theme.TextSecondary
import com.example.grooveboard.ui.theme.TypeIncidentContainer
import com.example.grooveboard.ui.theme.TypeIncidentRed
import com.example.grooveboard.ui.theme.TypeStoryContainer
import com.example.grooveboard.ui.theme.TypeStoryPurple
import com.example.grooveboard.ui.theme.TypeTaskContainer
import com.example.grooveboard.ui.theme.TypeTaskBlue

@Composable
fun TicketCard(
    ticket: Ticket,
    onClick: () -> Unit,
    onMoveStatus: (TicketStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    val isHighPriority = ticket.priority == Priority.HIGH
    val cardBorder = if (isHighPriority) {
        BorderStroke(1.5.dp, PriorityHighRed.copy(alpha = 0.7f))
    } else {
        BorderStroke(1.dp, GrooveCardBorder)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = cardBorder,
        elevation = CardDefaults.cardElevation(defaultElevation = if (isHighPriority) 4.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header: Code + Client + Priority Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = ticket.code,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = GrooveCyan
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFE2E8F0))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = ticket.client.code,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GrooveNavy
                        )
                    }
                }

                // Priority Badge (HU08)
                val (prioColor, prioBg) = when (ticket.priority) {
                    Priority.HIGH -> PriorityHighRed to PriorityHighContainer
                    Priority.MEDIUM -> PriorityMediumAmber to PriorityMediumContainer
                    Priority.LOW -> PriorityLowTeal to PriorityLowContainer
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(prioBg)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isHighPriority) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = prioColor,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(
                            text = "${ticket.priority.displayName} (${ticket.priority.slaHours}h SLA)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = prioColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = ticket.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = GrooveNavy,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Description snippet
            Text(
                text = ticket.description,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Tags row: Type badge + Story Points if applicable
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val (typeBg, typeColor, typeIcon) = when (ticket.type) {
                    TicketType.INCIDENT -> Triple(TypeIncidentContainer, TypeIncidentRed, Icons.Default.Warning)
                    TicketType.TASK -> Triple(TypeTaskContainer, TypeTaskBlue, Icons.Default.Assignment)
                    TicketType.USER_STORY -> Triple(TypeStoryContainer, TypeStoryPurple, Icons.Default.AutoAwesome)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(typeBg)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = typeIcon,
                            contentDescription = null,
                            tint = typeColor,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = ticket.type.displayName,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = typeColor
                        )
                    }
                }

                if (ticket.userStoryDetails != null) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFEDE9FE))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${ticket.userStoryDetails.storyPoints} SP",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TypeStoryPurple
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Assigned Agent avatar & name (HU05)
                if (ticket.assignedAgent != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(GrooveBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = ticket.assignedAgent.avatarInitials,
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = ticket.assignedAgent.name.split(" ").first(),
                            fontSize = 11.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    Text(
                        text = "Sin asignar",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Status Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (ticket.status) {
                    TicketStatus.TODO -> {
                        FilledTonalButton(
                            onClick = { onMoveStatus(TicketStatus.IN_PROGRESS) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("Iniciar tarea", fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                Icons.Default.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                    TicketStatus.IN_PROGRESS -> {
                        FilledTonalButton(
                            onClick = { onMoveStatus(TicketStatus.RESOLVED) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("Resolver", fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                    TicketStatus.RESOLVED -> {
                        FilledTonalButton(
                            onClick = { onMoveStatus(TicketStatus.IN_PROGRESS) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("Reabrir", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}
