package edu.ucne.registroprioridades.presentation.product

interface ProductEvent {
    data class TitleChange(val title: String): ProductEvent
    data class PriceChange(val price: String): ProductEvent
    data class UnitsChange(val units: String): ProductEvent
    data class DescriptionChange(val description: String): ProductEvent
    data class DiscountChange(val discount: String): ProductEvent
    data class Delete(val id: Int): ProductEvent
    data class SelectProduct(val id: Int): ProductEvent
    data object Save: ProductEvent
    data object GetProducts: ProductEvent
    data object New: ProductEvent
    data object Validation: ProductEvent
}
