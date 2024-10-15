package edu.ucne.registroprioridades.presentation.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registroprioridades.data.remote.dto.ProductDto
import edu.ucne.registroprioridades.data.repository.ProductRepository
import edu.ucne.registroprioridades.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        getProducts()
    }

    fun onEvent(event: ProductEvent) {
        when(event) {
            is ProductEvent.TitleChange -> onTitleChanged(event.title)
            is ProductEvent.PriceChange -> onPriceChanged(event.price)
            is ProductEvent.UnitsChange -> onUnitsChanged(event.units)
            is ProductEvent.DescriptionChange -> onDescriptionChanged(event.description)
            is ProductEvent.DiscountChange -> onDiscountChanged(event.discount)
            is ProductEvent.Delete -> deleteProduct(event.id)
            is ProductEvent.SelectProduct -> selectProduct(event.id)
            ProductEvent.Save -> saveProduct()
            ProductEvent.GetProducts -> getProducts()
            ProductEvent.New -> newProduct()
            ProductEvent.Validation -> uiState.value.validation = validation()
        }
    }

    private fun getProducts() {
        viewModelScope.launch {
            productRepository.getProducts().collectLatest { result ->
                when(result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                products = result.data ?: emptyList(),
                                isLoading = false,
                                errorMessage = ""
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = result.message ?: "Error desconocido",
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun selectProduct(id: Int) {
        viewModelScope.launch {
            productRepository.find(id).collectLatest { result ->
                when(result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                productId = result.data?.productId,
                                title = result.data?.title ?: "",
                                price = result.data?.price ?: 0.0,
                                description = result.data?.description ?: "",
                                units = result.data?.units ?: 0,
                                discount = result.data?.discount ?: 0.0
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(errorMessage = result.message ?: "Error desconocido")
                        }
                    }
                }
            }
        }
    }

    private fun deleteProduct(id: Int) {
        viewModelScope.launch {
            productRepository.deleteProduct(id).let { result ->
                when(result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                products = it.products.filterNot { product -> product.productId == id },
                                isLoading = false,
                                errorMessage = ""
                            )
                        }
                        getProducts()
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(errorMessage = result.message ?: "Error desconocido", isLoading = false)
                        }
                    }
                }
            }
        }
    }

    private fun onTitleChanged(title: String) {
        _uiState.update {
            it.copy(
                title = title,
                errorTitle = if(title.isEmpty()) "Campo título requerido" else ""
            )
        }
    }

    private fun onDescriptionChanged(description: String) {
        _uiState.update {
            it.copy(
                description = description,
                errorDescription = if(description.isEmpty()) "Campo descripción requerido" else ""
            )
        }
    }

    private fun onPriceChanged(priceS: String) {
        val price = priceS.toDoubleOrNull()

        _uiState.update {
            it.copy(
                price = price ?: 0.0,
                errorPrice = when {
                    price == null -> "Precio inválido"
                    price < 0.01 -> "El precio mínimo es 0.01"
                    price > 1_000_000_000 -> "El precio máximo es 1,000,000,000"
                    else -> ""
                }
            )
        }
    }

    private fun onUnitsChanged(unitsS: String) {
        val units = unitsS.toIntOrNull() ?: 0

        _uiState.update {
            it.copy(
                units = units,
                errorUnits = when {
                    units < 1 -> "El mínimo de unidades es 1"
                    units > 1000 -> "El máximo de unidades es 1000"
                    else -> ""
                }
            )
        }
    }

    private fun onDiscountChanged(discountS: String) {
        val discount = discountS.toDoubleOrNull()
        val price = uiState.value.price

        _uiState.update {
            it.copy(
                discount = discount ?: 0.0,
                errorDiscount = when {
                    discount == null -> "Descuento inválido"
                    discount < 0 -> "El descuento no puede ser menor a 0"
                    discount > price -> "El descuento no puede ser mayor que el precio"
                    else -> ""
                }
            )
        }
    }

    private fun saveProduct() {
        viewModelScope.launch {
            val product = uiState.value.toEntity()
            val result = if (uiState.value.productId != null && uiState.value.productId != 0) {
                productRepository.updateProduct(uiState.value.productId!!, product)
            } else {
                productRepository.addProduct(product)
            }

            result.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = "")
                        }
                        getProducts()
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = resource.message ?: "Error desconocido")
                        }
                    }
                }
            }
        }
    }

    private fun newProduct() {
        _uiState.update {
            it.copy(
                productId = null,
                title = "",
                price = 0.0,
                units = 0,
                description = "",
                discount = 0.0,
                errorTitle = "",
                errorPrice = "",
                errorUnits = "",
                errorDescription = "",
                errorDiscount = ""
            )
        }
    }

    private fun validation(): Boolean {
        var isValid = true
        getProducts()

        if (uiState.value.title.isEmpty()) {
            isValid = false
            _uiState.update {
                it.copy(errorTitle = "Campo título requerido")
            }
        }

        if (uiState.value.products.any {
                it.title.lowercase().trim() == uiState.value.title.lowercase().trim()
                        && it.productId != uiState.value.productId
            }) {
            isValid = false
            _uiState.update {
                it.copy(errorTitle = "Título ya existe")
            }
        }

        val price = uiState.value.price
        if (price < 0.01 || price > 1_000_000_000) {
            isValid = false
            _uiState.update {
                it.copy(errorPrice = "El precio debe estar entre 0.01 y 1,000,000,000")
            }
        }

        val units = uiState.value.units ?: 0
        if (units < 1 || units > 1000) {
            isValid = false
            _uiState.update {
                it.copy(errorUnits = "Las unidades deben estar entre 1 y 1000")
            }
        }

        val discount = uiState.value.discount
        if (discount < 0 || discount > price) {
            isValid = false
            _uiState.update {
                it.copy(errorDiscount = "El descuento debe ser 0 o mayor y menor o igual al precio")
            }
        }

        if (uiState.value.description.isEmpty()) {
            isValid = false
            _uiState.update {
                it.copy(errorDescription = "Campo descripción requerido")
            }
        }

        return isValid
    }
}

fun UiState.toEntity() = ProductDto(
    productId = productId,
    title = title,
    price = price,
    description = description,
    discount = discount,
    units = units
)
