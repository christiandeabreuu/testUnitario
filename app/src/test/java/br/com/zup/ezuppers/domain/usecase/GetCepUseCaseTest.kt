package br.com.zup.ezuppers.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.data.model.CepResult
import br.com.zup.ezuppers.data.repository.CepRepository
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
internal class GetCepUseCaseTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var repository: CepRepository

    lateinit var getCepUseCase: GetCepUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        getCepUseCase = GetCepUseCase(repository)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `when execute() is called should to call repository and return CepResult`() =
        runTest {
            val expectedCep = "11040020"

            coEvery { repository.getCep(expectedCep) } returns CepResult(
                1, "11040020", "a",
                "SP", "sp", "ap 23", "Santos"
            )
            val result = getCepUseCase.execute(expectedCep)

            val expectedCepResult = CepResult(
                1,
                "11040020",
                "a",
                "SP",
                "sp",
                "ap 23",
                "Santos"
            )
            assertEquals(expectedCepResult, result)
            coVerify(exactly = 1) { repository.getCep("11040020") }
        }

    @Test
    fun ` when call fun execute() should to call the function on repository with to same value (2) `() =
        runTest {
            val expectedCep = "11040020"
            val mockkCepResult = mockk<CepResult>()

            coEvery { repository.getCep(expectedCep) } returns mockkCepResult
            val result = getCepUseCase.execute(expectedCep)

            assertEquals(mockkCepResult, result)
            coVerify(exactly = 1) { repository.getCep("11040020") }
        }
}