package edu.ucne.registroprioridades.data.remote.dto

import edu.ucne.registroprioridades.data.local.entities.SistemaEntity

data class SistemaDto(
    val sistemaId: Int? = null,
    val nombre: String= "",
    val descripcion: String = ""
)

fun SistemaDto.toEntity(): SistemaEntity {
    return SistemaEntity(
        sistemaId = this.sistemaId,
        nombre = this.nombre,
        descripcion = this.descripcion
    )
}