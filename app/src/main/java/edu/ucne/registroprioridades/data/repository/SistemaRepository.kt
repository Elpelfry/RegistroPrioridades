package edu.ucne.registroprioridades.data.repository

import edu.ucne.registroprioridades.data.remote.api.TicketApi
import edu.ucne.registroprioridades.data.remote.dto.SistemaDto
import javax.inject.Inject

class SistemaRepository @Inject constructor(
    private val ticketApi: TicketApi
) {
    suspend fun getSistemas(): List<SistemaDto> = ticketApi.getSistemas()
    suspend fun addSistemas(sistemaDto: SistemaDto) = ticketApi.addSistemas(sistemaDto)
    suspend fun updateSistemas(id: Int, sistemaDto: SistemaDto) = ticketApi.updateSistemas(id, sistemaDto)
    suspend fun deleteSistemas(id: Int) = ticketApi.deleteSistemas(id)
}
