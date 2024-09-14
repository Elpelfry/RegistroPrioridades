package edu.ucne.registroprioridades.presentation.ticket

import edu.ucne.registroprioridades.data.local.entities.PrioridadEntity
import edu.ucne.registroprioridades.data.local.entities.TicketEntity

data class UiState(
    val ticketId: Int? = null,
    val fecha: String = "",
    val prioridadId: Int? = null,
    val cliente: String = "",
    val asunto: String = "",
    val descripcion: String = "",
    val errorFecha: String = "",
    val errorPrioridad: String = "",
    val errorCliente: String = "",
    val errorAsunto: String = "",
    val errorDescripcion: String = "",
    var validation: Boolean = false,
    val prioridades: List<PrioridadEntity> = emptyList(),
    val tickets : List<TicketEntity> = emptyList()
)
