package edu.ucne.registroprioridades.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "Products")
data class ProductEntity(
    @PrimaryKey
    val productId: Int? = 0,
    val title: String= "",
    val price: Double = 0.0,
    val units: Int = 0,
    val description: String = "",
    val discount: Double = 0.0
)
