package edu.ucne.registroprioridades.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registroprioridades.data.local.dao.PrioridadDao
import edu.ucne.registroprioridades.data.local.dao.ProductDao
import edu.ucne.registroprioridades.data.local.dao.SistemaDao
import edu.ucne.registroprioridades.data.local.dao.TicketDao
import edu.ucne.registroprioridades.data.local.entities.PrioridadEntity
import edu.ucne.registroprioridades.data.local.entities.ProductEntity
import edu.ucne.registroprioridades.data.local.entities.SistemaEntity
import edu.ucne.registroprioridades.data.local.entities.TicketEntity

@Database(

    entities = [
        PrioridadEntity::class,
        TicketEntity::class,
        SistemaEntity::class,
        ProductEntity::class
    ],
    version = 5,
    exportSchema = false
)

abstract class TicketDb : RoomDatabase(){
    abstract fun prioridadDao(): PrioridadDao
    abstract fun ticketDao(): TicketDao
    abstract fun sistemaDao(): SistemaDao
    abstract fun productDao(): ProductDao
}