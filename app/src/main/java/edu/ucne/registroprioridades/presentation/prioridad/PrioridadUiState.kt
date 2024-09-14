package edu.ucne.registroprioridades.presentation.prioridad

sealed interface PrioridadUiState {
    data class DescripcionChange(val des: String): PrioridadUiState
    data class DiasChange(val dia: String): PrioridadUiState
    data class SelectPrioridad(val prioridadId: Int): PrioridadUiState
    data object Save: PrioridadUiState
    data class Delete(val prioridadId: Int?): PrioridadUiState
    data object New: PrioridadUiState
    data object Validation: PrioridadUiState
}