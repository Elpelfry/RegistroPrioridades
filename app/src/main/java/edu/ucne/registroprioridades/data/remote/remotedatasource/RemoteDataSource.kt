package edu.ucne.registroprioridades.data.remote.remotedatasource

import edu.ucne.registroprioridades.data.remote.api.TicketApi
import edu.ucne.registroprioridades.data.remote.dto.ProductDto
import edu.ucne.registroprioridades.data.remote.dto.SistemaDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val ticketApi: TicketApi
) {
    // Sistemas
    suspend fun getSistemas() = ticketApi.getSistemas()
    suspend fun addSistemas(sistemaDto: SistemaDto) = ticketApi.addSistemas(sistemaDto)
    suspend fun updateSistemas(id: Int, sistemaDto: SistemaDto) = ticketApi.updateSistemas(id, sistemaDto)
    suspend fun deleteSistemas(id: Int) = ticketApi.deleteSistemas(id)

    // Products
    suspend fun getProducts() = ticketApi.getProducts()
    suspend fun addProduct(productDto: ProductDto) = ticketApi.addProduct(productDto)
    suspend fun updateProduct(id: Int, productDto: ProductDto) = ticketApi.updateProduct(id, productDto)
    suspend fun deleteProduct(id: Int) = ticketApi.deleteProduct(id)
}