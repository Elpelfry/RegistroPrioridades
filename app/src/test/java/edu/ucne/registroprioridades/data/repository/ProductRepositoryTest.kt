package edu.ucne.registroprioridades.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth
import edu.ucne.registroprioridades.data.local.dao.ProductDao
import edu.ucne.registroprioridades.data.local.entities.ProductEntity
import edu.ucne.registroprioridades.data.remote.dto.ProductDto
import edu.ucne.registroprioridades.data.remote.dto.toEntity
import edu.ucne.registroprioridades.data.remote.remotedatasource.RemoteDataSource
import edu.ucne.registroprioridades.utils.Resource
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.HttpException

class ProductRepositoryTest {

    @Test
    fun `should return a flow of Products`() = runTest {
        // Given
        val remoteProducts = listOf(
            ProductDto(1, "Remote Product 1", 100.0, 1, "Category 1", 2.0),
            ProductDto(2, "Remote Product 2", 200.0, 1, "Category 2", 2.0)
        )

        val localProducts = remoteProducts.map { it.toEntity() }

        val remoteDataSource = mockk<RemoteDataSource>()
        val productDao = mockk<ProductDao>()
        val repository = ProductRepository(productDao, remoteDataSource)

        coEvery { remoteDataSource.getProducts() } returns remoteProducts
        coEvery { productDao.save(any()) } just Runs

        // When
        repository.getProducts().test {
            // Then
            Truth.assertThat(awaitItem() is Resource.Loading).isTrue()
            val successResource = awaitItem() as Resource.Success
            Truth.assertThat(successResource.data).isEqualTo(localProducts)

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { productDao.save(any()) }
    }

    @Test
    fun `getProducts should return local data when remote fails`() = runTest {
        // Given
        val localProducts = listOf(
            ProductEntity(1, "Local Product 1", 100.0, 1, "Category 1", 2.0),
            ProductEntity(2, "Local Product 2", 200.0, 1, "Category 2", 2.0),
        )

        val remoteDataSource = mockk<RemoteDataSource>()
        val productDao = mockk<ProductDao>()
        val repository = ProductRepository(productDao, remoteDataSource)

        coEvery { remoteDataSource.getProducts() } throws HttpException(mockk(relaxed = true))
        coEvery { productDao.getAll() } returns flowOf(localProducts)

        // When
        repository.getProducts().test {
            // Then
            Truth.assertThat(awaitItem() is Resource.Loading).isTrue()
            val successResource = awaitItem() as Resource.Success
            Truth.assertThat(successResource.data).isEqualTo(localProducts)
            cancelAndIgnoreRemainingEvents()
        }
    }
}