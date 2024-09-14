package edu.ucne.registroprioridades.data.repository

import edu.ucne.registroprioridades.data.local.dao.TicketDao
import edu.ucne.registroprioridades.data.local.entities.TicketEntity
import javax.inject.Inject

class TicketRepository @Inject constructor(
    private val ticketDao: TicketDao
) {
    suspend fun save(ticket: TicketEntity) = ticketDao.save(ticket)
    suspend fun delete(ticket: TicketEntity) = ticketDao.delete(ticket)
    fun getAll() = ticketDao.getAll()
    suspend fun find(id: Int) = ticketDao.find(id)
}