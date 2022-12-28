package br.com.zup.ezuppers.domain.usecase

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.data.model.CepResult
import br.com.zup.ezuppers.data.repository.CepRepository
import br.com.zup.ezuppers.data.repository.UfRepository
import br.com.zup.ezuppers.data.repository.ZuppersRepository
import br.com.zup.ezuppers.ui.zuppers.viewmodel.ZuppersViewModel
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
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
    fun `When call fun execute() should to call the fun on repository with to same value`() =
        runTest {
            val expectedCep = "11040020"

            coEvery { repository.getCep(expectedCep) } returns CepResult(
                1, "11040020", "a",
                "SP", "sp", "ap 23", "Santos"
            )
            val value = getCepUseCase.execute(expectedCep)

            val expectedCepResult = CepResult(
                        1, 
                        "11040020", 
                        "a",
                        "SP", 
                        "sp", 
                        "ap 23",
                         "Santos"
                    )
            assertEquals(expectedCepResult, value)
            coVerify(exactly = 1) { repository.getCep("11040020") }
        }


    @Test
    fun `When call fun execute() dont should to call the fun on repository `() =
        runTest {
            val expectedCep = "11040020"

            coEvery { repository.getCep(expectedCep) } throws NullPointerException()

            coVerify(exactly = 0) { repository.getCep(expectedCep) }
        }
}