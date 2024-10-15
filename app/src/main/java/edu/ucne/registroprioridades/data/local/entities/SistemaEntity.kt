package edu.ucne.registroprioridades.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "Sistemas")
data class SistemaEntity(
    @PrimaryKey
    val sistemaId: Int? = 0,
    val nombre: String= "",
    val descripcion: String = ""
)
