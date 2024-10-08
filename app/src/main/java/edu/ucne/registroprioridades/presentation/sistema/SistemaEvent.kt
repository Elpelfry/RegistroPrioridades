package edu.ucne.registroprioridades.presentation.sistema

interface SistemaEvent {
    data class DescripcionChange(val des: String): SistemaEvent
    data class NombreChange(val nom: String): SistemaEvent
    data class Delete(val id: Int): SistemaEvent
    data class Edit(val id: Int, val uiState: UiState): SistemaEvent
    data class SelectSistema(val id: Int): SistemaEvent
    data object Save: SistemaEvent
    data object GetSistemas: SistemaEvent
    data object New: SistemaEvent
    data object Validation: SistemaEvent
}