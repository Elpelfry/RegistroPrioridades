package edu.ucne.registroprioridades.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.registroprioridades.data.local.entities.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Upsert
    suspend fun save(product: ProductEntity)

    @Query(
        """
        SELECT * 
        FROM Products
        WHERE productId = :id
        LIMIT 1
        """
    )
    suspend fun find(id: Int): ProductEntity?

    @Query("SELECT * FROM Products")
    fun getAll(): Flow<List<ProductEntity>>

    @Delete
    suspend fun delete(product: ProductEntity)
}