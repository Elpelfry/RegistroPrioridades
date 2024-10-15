package edu.ucne.registroprioridades.presentation.product

import edu.ucne.registroprioridades.data.local.entities.ProductEntity

data class UiState(
    val productId: Int? = null,
    val title: String = "",
    val errorTitle: String = "",
    val price: Double = 0.0,
    val errorPrice: String = "",
    val units: Int = 0,
    val errorUnits: String = "",
    val description: String = "",
    val errorDescription: String = "",
    val discount: Double = 0.0,
    val errorDiscount: String = "",
    var validation: Boolean = false,
    val products: List<ProductEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)