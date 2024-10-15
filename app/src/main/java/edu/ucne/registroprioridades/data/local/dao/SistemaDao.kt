package edu.ucne.registroprioridades.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.registroprioridades.data.local.entities.SistemaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SistemaDao {
    @Upsert
    suspend fun save(sistema: SistemaEntity)
    @Query(
        """
        SELECT * 
        FROM Sistemas
        WHERE sistemaId = :id
        LIMIT 1
        """
    )
    suspend fun find(id: Int): SistemaEntity?

    @Query("SELECT * FROM Sistemas")
    fun getAll(): Flow<List<SistemaEntity>>

    @Delete
    suspend fun delete(sistema: SistemaEntity)
}