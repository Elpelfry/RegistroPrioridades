package edu.ucne.registroprioridades.presentation.ticket

sealed interface TicketEvent {
    data class FechaChange(val fecha: String): TicketEvent
    data class PrioridadChange(val prioridadId: String): TicketEvent
    data class ClienteChange(val cliente: String): TicketEvent
    data class AsuntoChange(val asunto: String): TicketEvent
    data class DescripcionChange(val descripcion: String): TicketEvent
    data class SelectTicket(val ticketId: Int): TicketEvent
    data object Save: TicketEvent
    data class Delete(val ticketId: Int?): TicketEvent
    data object New: TicketEvent
    data object Validation: TicketEvent
}