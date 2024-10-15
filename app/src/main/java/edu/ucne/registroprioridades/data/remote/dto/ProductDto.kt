package edu.ucne.registroprioridades.data.remote.dto

import edu.ucne.registroprioridades.data.local.entities.ProductEntity

data class ProductDto(
    val productId: Int? = 0,
    val title: String= "",
    val price: Double = 0.0,
    val units: Int = 0,
    val description: String = "",
    val discount: Double = 0.0
)

fun ProductDto.toEntity(): ProductEntity {
    return ProductEntity(
        productId = this.productId,
        title = this.title,
        price = this.price,
        units = this.units,
        description = this.description,
        discount = this.discount
    )
}