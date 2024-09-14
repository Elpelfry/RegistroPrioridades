package edu.ucne.registroprioridades.presentation.prioridad

import edu.ucne.registroprioridades.data.local.entities.PrioridadEntity

data class UiState(
    val prioridadId: Int? = null,
    val descripcion: String = "",
    val errorDias: String = "",
    val diasCompromiso: Int? = null,
    val errorDescripcion: String = "",
    var validation : Boolean = false,
    val prioridades: List<PrioridadEntity> = emptyList()
)
