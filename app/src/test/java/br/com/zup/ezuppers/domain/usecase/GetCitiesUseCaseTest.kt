package br.com.zup.ezuppers.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.data.model.CitiesResult
import br.com.zup.ezuppers.data.repository.UfRepository
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
internal class GetCitiesUseCaseTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var repository: UfRepository

    lateinit var getCitiesUseCase: GetCitiesUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        getCitiesUseCase = GetCitiesUseCase(repository)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `When call fun execute() should to call the fun on repository with to same value`() =
        runTest {
            val expectedUfid = 12345
            val mockkCitiesResult = mockk<CitiesResult>()

            coEvery { repository.getCities(expectedUfid) } returns mockkCitiesResult

            val result = getCitiesUseCase.execute(expectedUfid)

            assertEquals(result, mockkCitiesResult)
            coVerify(exactly = 1) {
                repository.getCities(12345)
            }
        }
}