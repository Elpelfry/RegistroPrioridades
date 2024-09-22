package edu.ucne.registroprioridades.presentation.prioridad

sealed interface PrioridadEvent {
    data class DescripcionChange(val des: String): PrioridadEvent
    data class DiasChange(val dia: String): PrioridadEvent
    data class SelectPrioridad(val prioridadId: Int): PrioridadEvent
    data object Save: PrioridadEvent
    data class Delete(val prioridadId: Int?): PrioridadEvent
    data object New: PrioridadEvent
    data object Validation: PrioridadEvent
}