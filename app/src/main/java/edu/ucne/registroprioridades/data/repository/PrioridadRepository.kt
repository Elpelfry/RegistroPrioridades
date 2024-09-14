package edu.ucne.registroprioridades.data.repository

import edu.ucne.registroprioridades.data.local.dao.PrioridadDao
import edu.ucne.registroprioridades.data.local.entities.PrioridadEntity
import javax.inject.Inject

class PrioridadRepository @Inject constructor(
    private val prioridadDao: PrioridadDao)
 {
    suspend fun save(prioridad: PrioridadEntity) = prioridadDao.save(prioridad)
    suspend fun delete(prioridad: PrioridadEntity) = prioridadDao.delete(prioridad)
    fun getAll() = prioridadDao.getall()
    suspend fun find(id: Int) = prioridadDao.find(id)
}