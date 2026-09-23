package com.example.grooveboard.data.datasource

import com.example.grooveboard.domain.model.Client
import com.example.grooveboard.domain.model.Comment
import com.example.grooveboard.domain.model.Priority
import com.example.grooveboard.domain.model.Ticket
import com.example.grooveboard.domain.model.TicketStatus
import com.example.grooveboard.domain.model.TicketType
import com.example.grooveboard.domain.model.User
import com.example.grooveboard.domain.model.UserRole
import com.example.grooveboard.domain.model.UserStoryDetails

object MockData {

    val adminUser = User(
        id = "usr-01",
        name = "Carlos Méndez",
        email = "admin@groove.com",
        role = UserRole.ADMIN,
        avatarInitials = "CM",
        roleTitle = "Líder de Soporte Cloud",
        activeTicketsCount = 3,
        resolvedTicketsCount = 14
    )

    val agent1 = User(
        id = "usr-02",
        name = "Ana Torres",
        email = "agente1@groove.com",
        role = UserRole.AGENT,
        avatarInitials = "AT",
        roleTitle = "Especialista AWS & Azure",
        activeTicketsCount = 4,
        resolvedTicketsCount = 9
    )

    val agent2 = User(
        id = "usr-03",
        name = "Luis Morales",
        email = "agente2@groove.com",
        role = UserRole.AGENT,
        avatarInitials = "LM",
        roleTitle = "Especialista GCP & DevOps",
        activeTicketsCount = 2,
        resolvedTicketsCount = 11
    )

    val allUsers = mutableListOf(adminUser, agent1, agent2)

    val passwords = mutableMapOf(
        "admin@groove.com" to "admin123",
        "agente1@groove.com" to "agent123",
        "agente2@groove.com" to "agent123"
    )

    val clients = listOf(
        Client(
            id = "cli-01",
            name = "BCP Cloud Services",
            code = "BCP",
            environment = "AWS Multi-Account (Prod/Stg)",
            slaLevel = "SLA Crítico (2h)"
        ),
        Client(
            id = "cli-02",
            name = "Rímac Seguros Cloud",
            code = "RIMAC",
            environment = "Azure Kubernetes & ExpressRoute",
            slaLevel = "SLA Alto (4h)"
        ),
        Client(
            id = "cli-03",
            name = "Interbank Digital",
            code = "IBK",
            environment = "GCP Anthos & Cloud SQL",
            slaLevel = "SLA Estándar (8h)"
        ),
        Client(
            id = "cli-04",
            name = "Alicorp Digital Hub",
            code = "ALICORP",
            environment = "AWS Serverless & DynamoDB",
            slaLevel = "SLA Flexible (24h)"
        ),
        Client(
            id = "cli-05",
            name = "Ferreyros Cloud Operations",
            code = "FERR",
            environment = "Azure Hybrid VM Infrastructure",
            slaLevel = "SLA Estándar (8h)"
        )
    )

    fun getInitialTickets(): MutableList<Ticket> = mutableListOf(
        Ticket(
            id = "tck-001",
            code = "GRV-101",
            title = "Caída de servicio en clúster EKS de producción",
            description = "El clúster EKS en us-east-1 presenta saturación de pods en el nodo worker c5.4xlarge, elevando la latencia del API Gateway sobre el umbral SLA de 300ms.",
            status = TicketStatus.TODO,
            priority = Priority.HIGH,
            type = TicketType.INCIDENT,
            client = clients[0],
            assignedAgent = agent1,
            createdAt = "22/09/2026 14:30",
            slaDeadline = "Quedan 45 min",
            comments = listOf(
                Comment(
                    id = "c-101",
                    authorName = "Carlos Méndez",
                    authorRole = "Líder de Soporte Cloud",
                    authorInitials = "CM",
                    content = "Ticket escalado de alerta de Datadog. Prioridad máxima para BCP.",
                    timestamp = "22/09/2026 14:35"
                )
            )
        ),
        Ticket(
            id = "tck-002",
            code = "GRV-102",
            title = "Optimización de costos y auto-scaling en Azure VMSS",
            description = "Configurar reglas de escalado horizontal automático basadas en métricas de CPU (>75%) y uso de memoria en el ambiente de pre-producción.",
            status = TicketStatus.TODO,
            priority = Priority.MEDIUM,
            type = TicketType.TASK,
            client = clients[1],
            assignedAgent = agent1,
            createdAt = "22/09/2026 11:15",
            slaDeadline = "Quedan 5h",
            comments = emptyList()
        ),
        Ticket(
            id = "tck-003",
            code = "GRV-103",
            title = "Migración de base de datos a Cloud SQL con réplica de lectura",
            description = "Requerimiento de mejora para la arquitectura de alta disponibilidad en GCP. Se definen requerimientos según plantilla de historia de usuario Scrum.",
            status = TicketStatus.IN_PROGRESS,
            priority = Priority.HIGH,
            type = TicketType.USER_STORY,
            client = clients[2],
            assignedAgent = agent2,
            createdAt = "21/09/2026 16:40",
            slaDeadline = "Quedan 1h 30m",
            userStoryDetails = UserStoryDetails(
                asA = "Arquitecto Cloud de Interbank",
                iWant = "desplegar una réplica de lectura para Cloud SQL Postgres en la región us-central1",
                soThat = "se desacoplen las consultas de reportes pesados y no afecten el rendimiento transaccional",
                acceptanceCriteria = listOf(
                    "Replicación asíncrona con lag menor a 5 segundos",
                    "Configuración de failover automático habilitada",
                    "Monitoreo de conexiones concurrentes en Cloud Monitoring"
                ),
                storyPoints = 5
            ),
            comments = listOf(
                Comment(
                    id = "c-102",
                    authorName = "Luis Morales",
                    authorRole = "Especialista GCP",
                    authorInitials = "LM",
                    content = "Se realizó el snapshot inicial y estamos probando la replicación en Staging.",
                    timestamp = "22/09/2026 09:20"
                )
            )
        ),
        Ticket(
            id = "tck-004",
            code = "GRV-104",
            title = "Renovación de certificados SSL wildcard en Application Gateway",
            description = "Reemplazo de certificados *.rimac.com.pe en los Key Vaults de Azure antes de su fecha de vencimiento.",
            status = TicketStatus.IN_PROGRESS,
            priority = Priority.MEDIUM,
            type = TicketType.TASK,
            client = clients[1],
            assignedAgent = adminUser,
            createdAt = "20/09/2026 10:00",
            slaDeadline = "En plazo SLA",
            comments = listOf(
                Comment(
                    id = "c-103",
                    authorName = "Ana Torres",
                    authorRole = "Especialista Azure",
                    authorInitials = "AT",
                    content = "Certificados cargados en Azure Key Vault. Falta el binding en el Gateway.",
                    timestamp = "21/09/2026 15:10"
                )
            )
        ),
        Ticket(
            id = "tck-005",
            code = "GRV-105",
            title = "Pipeline CI/CD para lambdas en AWS Serverless",
            description = "Implementación del pipeline de entrega continua con GitHub Actions y Terraform para el backend de Alicorp.",
            status = TicketStatus.RESOLVED,
            priority = Priority.LOW,
            type = TicketType.USER_STORY,
            client = clients[3],
            assignedAgent = agent2,
            createdAt = "18/09/2026 12:00",
            slaDeadline = "Cumplido exitosamente",
            userStoryDetails = UserStoryDetails(
                asA = "Líder Técnico de Alicorp",
                iWant = "un pipeline automatizado de validación y despliegue para funciones AWS Lambda",
                soThat = "los despliegues se realicen sin intervención manual y con rollback ante fallos",
                acceptanceCriteria = listOf(
                    "Ejecución de pruebas unitarias automáticas antes del deploy",
                    "Notificación a canal de Slack ante éxito o fallo",
                    "Uso de variables y secretos protegidos en AWS Secrets Manager"
                ),
                storyPoints = 8
            ),
            comments = listOf(
                Comment(
                    id = "c-104",
                    authorName = "Luis Morales",
                    authorRole = "Especialista DevOps",
                    authorInitials = "LM",
                    content = "Pipeline completado y probado en producción con éxito.",
                    timestamp = "19/09/2026 17:00"
                )
            )
        ),
        Ticket(
            id = "tck-006",
            code = "GRV-106",
            title = "Falla de conectividad VPN Site-to-Site con datacenter local",
            description = "Túnel IPsec entre el router de Ferreyros y el Virtual Network Gateway de Azure intermitente.",
            status = TicketStatus.RESOLVED,
            priority = Priority.HIGH,
            type = TicketType.INCIDENT,
            client = clients[4],
            assignedAgent = agent1,
            createdAt = "19/09/2026 08:30",
            slaDeadline = "Resuelto en 1h 15m (Dentro de SLA)",
            comments = listOf(
                Comment(
                    id = "c-105",
                    authorName = "Ana Torres",
                    authorRole = "Especialista Azure",
                    authorInitials = "AT",
                    content = "Se detectó desfase de claves IKEv2 en el firewall perimetral. Túnel restablecido.",
                    timestamp = "19/09/2026 09:45"
                )
            )
        )
    )
}
