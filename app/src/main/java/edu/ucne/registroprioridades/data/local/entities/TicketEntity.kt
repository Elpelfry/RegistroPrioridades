package edu.ucne.registroprioridades.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "Tickets")
data class TicketEntity(
    @PrimaryKey
    val ticketId: Int? = null,
    val fecha: String = "",
    val prioridadId: Int? = null,
    val cliente: String = "",
    val asunto: String = "",
    val descripcion: String = ""
)