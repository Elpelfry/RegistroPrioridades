package edu.ucne.registroprioridades.data.repository

import edu.ucne.registroprioridades.data.local.dao.PrioridadDao
import edu.ucne.registroprioridades.data.local.entities.PrioridadEntity

class PrioridadRepository(private val prioridadDao: PrioridadDao) {
    suspend fun savePrioridad(prioridad: PrioridadEntity) = prioridadDao.save(prioridad)
    suspend fun deletePrioridad(prioridad: PrioridadEntity) = prioridadDao.delete(prioridad)
    fun getPrioridades() = prioridadDao.getall()
    suspend fun getPrioridad(id: Int) = prioridadDao.find(id)
}