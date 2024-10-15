package edu.ucne.registroprioridades.data.repository

import edu.ucne.registroprioridades.data.local.dao.ProductDao
import edu.ucne.registroprioridades.data.local.entities.ProductEntity
import edu.ucne.registroprioridades.data.remote.dto.ProductDto
import edu.ucne.registroprioridades.data.remote.dto.toEntity
import edu.ucne.registroprioridades.data.remote.remotedatasource.RemoteDataSource
import edu.ucne.registroprioridades.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productDao: ProductDao,
    private val remoteDataSource: RemoteDataSource
) {
    fun getProducts(): Flow<Resource<List<ProductEntity>>> = flow {
        try {
            emit(Resource.Loading())
            val productsR = remoteDataSource.getProducts()

            val productsL = productsR.map { it.toEntity() }
            productsL.forEach { productDao.save(it) }

            emit(Resource.Success(productsL))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: Exception) {

            val productsL = productDao.getAll().firstOrNull()
            if (productsL.isNullOrEmpty()) {
                emit(Resource.Error("No se encontraron datos locales"))
            } else {
                emit(Resource.Success(productsL))
            }
        }
    }

    fun addProduct(productDto: ProductDto): Flow<Resource<ProductEntity>> = flow {
        try {
            emit(Resource.Loading())
            val productR = remoteDataSource.addProduct(productDto)
            val productL = productR.toEntity()
            productDao.save(productL)
            emit(Resource.Success(productL))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión ${e.message()}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error ${e.message}"))
        }
    }

    fun updateProduct(id: Int, productDto: ProductDto): Flow<Resource<ProductEntity>> = flow {
        try {
            emit(Resource.Loading())
            val productR = remoteDataSource.updateProduct(id, productDto)
            val productL = productR.toEntity()
            productDao.save(productL)
            emit(Resource.Success(productL))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión ${e.message()}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error ${e.message}"))
        }
    }

    suspend fun deleteProduct(id: Int): Resource<Unit> {
        return try {
            remoteDataSource.deleteProduct(id)
            val product = productDao.find(id)
            if (product != null) {
                productDao.delete(product)
            }
            Resource.Success(Unit)
        } catch (e: HttpException) {
            Resource.Error("Error de conexión ${e.message()}")
        } catch (e: Exception) {
            val product = productDao.find(id)
            if (product != null) {
                productDao.delete(product)
            }
            Resource.Success(Unit)
        }
    }

    fun find(id: Int): Flow<Resource<ProductEntity>> = flow{
        try {
            emit(Resource.Loading())
            val product = productDao.find(id)
            if (product != null) {
                emit(Resource.Success(product))
            } else {
                emit(Resource.Error("No se encontró el producto"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error ${e.message}"))
        }
    }
}