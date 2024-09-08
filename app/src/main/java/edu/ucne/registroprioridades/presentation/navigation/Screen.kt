package edu.ucne.registroprioridades.presentation.navigation

import edu.ucne.registroprioridades.data.local.entities.PrioridadEntity
import kotlinx.serialization.Serializable

sealed class Screen{
    @Serializable
    object PrioridadList: Screen()
    @Serializable
    data class Prioridad(val prioridadId: Int): Screen()
    @Serializable
    data class PrioridadDelete(val prioridadId: Int): Screen()
}