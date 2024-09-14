package edu.ucne.registroprioridades.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen{
    @Serializable
    object PrioridadList: Screen()
    @Serializable
    data class Prioridad(val prioridadId: Int): Screen()
    @Serializable
    object TicketList: Screen()
    @Serializable
    data class Ticket(val ticketId: Int): Screen()
}