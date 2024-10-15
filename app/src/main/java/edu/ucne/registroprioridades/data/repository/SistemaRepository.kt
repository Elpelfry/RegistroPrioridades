package edu.ucne.registroprioridades.data.repository

import edu.ucne.registroprioridades.data.local.dao.SistemaDao
import edu.ucne.registroprioridades.data.local.entities.SistemaEntity
import edu.ucne.registroprioridades.data.remote.dto.SistemaDto
import edu.ucne.registroprioridades.data.remote.dto.toEntity
import edu.ucne.registroprioridades.data.remote.remotedatasource.RemoteDataSource
import edu.ucne.registroprioridades.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class SistemaRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val sistemaDao: SistemaDao
) {
    fun getSistemas(): Flow<Resource<List<SistemaEntity>>> = flow {
        try {
            emit(Resource.Loading())
            val sistemasR = remoteDataSource.getSistemas()

            val sistemasL = sistemasR.map { it.toEntity() }
            sistemasL.forEach { sistemaDao.save(it) }

            emit(Resource.Success(sistemasL))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión ${e.message()}"))
        } catch (e: Exception) {

            val sistemasL = sistemaDao.getAll().firstOrNull()
            if (sistemasL.isNullOrEmpty()) {
                emit(Resource.Error("No se encontraron datos locales"))
            } else {
                emit(Resource.Success(sistemasL))
            }
        }
    }

    fun addSistemas(sistemaDto: SistemaDto): Flow<Resource<SistemaEntity>> = flow {
        try {
            emit(Resource.Loading())
            val sistemaR = remoteDataSource.addSistemas(sistemaDto)
            val sistemaL = sistemaR.toEntity()
            sistemaDao.save(sistemaL)
            emit(Resource.Success(sistemaL))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión ${e.message()}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error ${e.message}"))
        }
    }


    fun updateSistemas(id: Int, sistemaDto: SistemaDto): Flow<Resource<SistemaEntity>> = flow {
        try {
            emit(Resource.Loading())
            val sistemaR = remoteDataSource.updateSistemas(id, sistemaDto)
            val sistemaL = sistemaR.toEntity()
            sistemaDao.save(sistemaL)
            emit(Resource.Success(sistemaL))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión ${e.message()}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error ${e.message}"))
        }
    }

    suspend fun deleteSistemas(id: Int): Resource<Unit> {
        return try {
            remoteDataSource.deleteSistemas(id)
            val sistemaL = sistemaDao.find(id)
            if (sistemaL != null) {
                sistemaDao.delete(sistemaL)
            }
            Resource.Success(Unit)
        } catch (e: HttpException) {
            Resource.Error("Error de conexión ${e.message()}")
        } catch (e: Exception) {
            val sistemaL = sistemaDao.find(id)
            if (sistemaL != null) {
                sistemaDao.delete(sistemaL)
            }
            Resource.Success(Unit)
        }
    }

    fun find(id: Int): Flow<Resource<SistemaEntity>> = flow {
        try {
            emit(Resource.Loading())
            val sistema = sistemaDao.find(id)
            emit(Resource.Success(sistema!!))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión ${e.message()}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error ${e.message}"))
        }
    }
}
