package edu.ucne.registroprioridades.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registroprioridades.data.local.dao.PrioridadDao
import edu.ucne.registroprioridades.data.local.dao.TicketDao
import edu.ucne.registroprioridades.data.local.entities.PrioridadEntity
import edu.ucne.registroprioridades.data.local.entities.TicketEntity

@Database(

    entities = [
        PrioridadEntity::class,
        TicketEntity::class
    ],
    version = 2,
    exportSchema = false
)

abstract class TicketDb : RoomDatabase(){
    abstract fun prioridadDao(): PrioridadDao
    abstract fun ticketDao(): TicketDao
}