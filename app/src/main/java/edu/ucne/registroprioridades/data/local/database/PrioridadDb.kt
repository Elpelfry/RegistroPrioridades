package edu.ucne.registroprioridades.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registroprioridades.data.local.dao.PrioridadDao
import edu.ucne.registroprioridades.data.local.entities.PrioridadEntity

@Database(

    entities = [
        PrioridadEntity::class
    ],
    version = 1,
    exportSchema = false
)

abstract class PrioridadDb : RoomDatabase(){
    abstract fun prioridadDao(): PrioridadDao
}