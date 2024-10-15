package edu.ucne.registroprioridades.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth
import edu.ucne.registroprioridades.data.local.dao.SistemaDao
import edu.ucne.registroprioridades.data.local.entities.SistemaEntity
import edu.ucne.registroprioridades.data.remote.dto.SistemaDto
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

class SistemaRepositoryTest {

    @Test
    fun `should return a flow of Sistemas`() = runTest {
        // Given
        val remoteSistemas = listOf(
            SistemaDto(1, "Sistema 1", "Descripción 1"),
            SistemaDto(2, "Sistema 2", "Descripción 2")
        )

        val localSistemas = remoteSistemas.map { it.toEntity() }

        val remoteDataSource = mockk<RemoteDataSource>()
        val sistemaDao = mockk<SistemaDao>()
        val repository = SistemaRepository(remoteDataSource, sistemaDao)

        coEvery { remoteDataSource.getSistemas() } returns remoteSistemas
        coEvery { sistemaDao.save(any()) } just Runs

        // When
        repository.getSistemas().test {
            // Then
            Truth.assertThat(awaitItem() is Resource.Loading).isTrue()
            val successResource = awaitItem() as Resource.Success
            Truth.assertThat(successResource.data).isEqualTo(localSistemas)

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { sistemaDao.save(any()) }
    }

    @Test
    fun `getSistemas should return local data when remote fails`() = runTest {
        // Given
        val localSistemas = listOf(
            SistemaEntity(1, "Sistema Local 1", "Descripción Local 1"),
            SistemaEntity(2, "Sistema Local 2", "Descripción Local 2")
        )

        val remoteDataSource = mockk<RemoteDataSource>()
        val sistemaDao = mockk<SistemaDao>()
        val repository = SistemaRepository(remoteDataSource, sistemaDao)

        coEvery { remoteDataSource.getSistemas() } throws HttpException(mockk(relaxed = true))
        coEvery { sistemaDao.getAll() } returns flowOf(localSistemas)

        // When
        repository.getSistemas().test {
            // Then
            Truth.assertThat(awaitItem() is Resource.Loading).isTrue()
            val successResource = awaitItem() as Resource.Success
            Truth.assertThat(successResource.data).isEqualTo(localSistemas)
            cancelAndIgnoreRemainingEvents()
        }
    }
}