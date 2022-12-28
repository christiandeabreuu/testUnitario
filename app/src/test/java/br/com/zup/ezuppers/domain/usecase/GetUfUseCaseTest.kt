package br.com.zup.ezuppers.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.data.model.State
import br.com.zup.ezuppers.data.model.StatesResult
import br.com.zup.ezuppers.data.repository.UfRepository
import br.com.zup.ezuppers.data.repository.ZuppersRepository
import br.com.zup.ezuppers.domain.model.User
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

internal class GetUfUseCaseTest{

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var repository: UfRepository

    lateinit var getUfUseCase : GetUfUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        getUfUseCase = GetUfUseCase(repository)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `When call fun execute should to call the fun on repository with to same value`() =
        runTest {
            
            coEvery { repository.getStates() } returns StatesResult()
            getUfUseCase.execute()

            coVerify(exactly = 1) { repository.getStates() }
    }

    private fun mockkListStates() = listOf(
        State(1,"São Paulo", "SP"),
        State(2,"Rio de Janeiro", "RJ")

    )

}