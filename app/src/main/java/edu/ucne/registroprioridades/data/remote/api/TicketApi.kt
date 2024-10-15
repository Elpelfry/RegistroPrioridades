package edu.ucne.registroprioridades.data.remote.api

import edu.ucne.registroprioridades.data.remote.dto.ProductDto
import edu.ucne.registroprioridades.data.remote.dto.SistemaDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TicketApi {
    //Sistema
    @GET("api/Sistemas")
    suspend fun getSistemas(): List<SistemaDto>
    @POST("api/Sistemas")
    suspend fun addSistemas(@Body sistemaDto: SistemaDto): SistemaDto
    @PUT("api/Sistemas/{id}")
    suspend fun updateSistemas(@Path("id") id: Int,@Body sistemaDto: SistemaDto): SistemaDto
    @DELETE("api/Sistemas/{id}")
    suspend fun deleteSistemas(@Path("id") id: Int)

    //Product
    @GET("api/Products")
    suspend fun getProducts(): List<ProductDto>
    @POST("api/Products")
    suspend fun addProduct(@Body productDto: ProductDto): ProductDto
    @PUT("api/Products/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body productDto: ProductDto): ProductDto
    @DELETE("api/Products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int)
}