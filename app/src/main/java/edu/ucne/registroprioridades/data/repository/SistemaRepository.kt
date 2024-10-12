package edu.ucne.registroprioridades.data.repository

import edu.ucne.registroprioridades.data.remote.api.TicketApi
import edu.ucne.registroprioridades.data.remote.dto.SistemaDto
import edu.ucne.registroprioridades.data.remote.remotedatasource.RemoteDataSource
import javax.inject.Inject

class SistemaRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {
    suspend fun getSistemas(): List<SistemaDto> = remoteDataSource.getSistemas()
    suspend fun addSistemas(sistemaDto: SistemaDto) = remoteDataSource.addSistemas(sistemaDto)
    suspend fun updateSistemas(id: Int, sistemaDto: SistemaDto) = remoteDataSource.updateSistemas(id, sistemaDto)
    suspend fun deleteSistemas(id: Int) = remoteDataSource.deleteSistemas(id)
}
