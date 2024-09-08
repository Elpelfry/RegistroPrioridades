package edu.ucne.registroprioridades.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "Prioridades")
data class PrioridadEntity (
    @PrimaryKey
    val prioridadId: Int? = null,
    val descripcion: String = "",
    val diasCompromiso: Int?
)