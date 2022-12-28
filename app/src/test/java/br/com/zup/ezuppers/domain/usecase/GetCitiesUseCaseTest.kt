package br.com.zup.ezuppers.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.data.model.CepResult
import br.com.zup.ezuppers.data.model.CitiesResult
import br.com.zup.ezuppers.data.model.City
import br.com.zup.ezuppers.data.repository.CepRepository
import br.com.zup.ezuppers.data.repository.UfRepository
import br.com.zup.ezuppers.domain.model.User
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
import org.mockito.internal.matchers.Null

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

            coEvery { repository.getCities(expectedUfid) } returns CitiesResult()

            val value = getCitiesUseCase.execute(expectedUfid)
            val expectedCityResult = CitiesResult()


            assertEquals(value, expectedCityResult)
            coVerify(exactly = 1) {
                repository.getCities(12345)
            }
        }

    private fun mockkListCities() = listOf(
        City(1, "Santos"),
        City(2, "Guaruja"),
        City(3, "Maresias")
    )


}