package edu.ucne.registroprioridades.data.remote.api

import edu.ucne.registroprioridades.data.remote.dto.SistemaDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TicketApi {
    @GET("api/Sistemas")
    suspend fun getSistemas(): List<SistemaDto>
    @POST("api/Sistemas")
    suspend fun addSistemas(@Body sistemaDto: SistemaDto): Response<SistemaDto>
    @PUT("api/Sistemas/{id}")
    suspend fun updateSistemas(@Path("id") id: Int,@Body sistemaDto: SistemaDto): Response<SistemaDto>
    @DELETE("api/Sistemas/{id}")
    suspend fun deleteSistemas(@Path("id") id: Int): Response<SistemaDto>
}