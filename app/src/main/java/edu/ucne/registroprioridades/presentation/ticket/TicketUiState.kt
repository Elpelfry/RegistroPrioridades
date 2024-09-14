package edu.ucne.registroprioridades.presentation.ticket

sealed interface TicketUiState {
    data class FechaChange(val fecha: String): TicketUiState
    data class PrioridadChange(val prioridadId: String): TicketUiState
    data class ClienteChange(val cliente: String): TicketUiState
    data class AsuntoChange(val asunto: String): TicketUiState
    data class DescripcionChange(val descripcion: String): TicketUiState
    data class SelectTicket(val ticketId: Int): TicketUiState
    data object Save: TicketUiState
    data class Delete(val ticketId: Int?): TicketUiState
    data object New: TicketUiState
    data object Validation: TicketUiState
}