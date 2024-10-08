package edu.ucne.registroprioridades.presentation.sistema

import edu.ucne.registroprioridades.data.remote.dto.SistemaDto

data class UiState(
    val sistemaId: Int? = null,
    val nombreSistema: String = "",
    val errorNombre: String = "",
    val descripcionSistema: String = "",
    val errorDescripcion: String = "",
    var validation: Boolean = false,
    val sistemas: List<SistemaDto> = emptyList()
)
