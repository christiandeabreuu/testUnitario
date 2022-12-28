package br.com.zup.ezuppers.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.data.model.StatesResult
import br.com.zup.ezuppers.data.repository.UfRepository
import br.com.zup.ezuppers.data.repository.ZuppersRepository
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

internal class GetZuppersQuantityUseCaseTest{

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var repository: ZuppersRepository

    lateinit var getZuppersQuantityUseCase : GetZuppersQuantityUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        getZuppersQuantityUseCase = GetZuppersQuantityUseCase(repository)
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
            val expectedCity = "Santos"

            coEvery { repository.getZuppersQuantity(expectedCity) } returns 10 
            getZuppersQuantityUseCase.execute(expectedCity)

            assertEquals(getZuppersQuantityUseCase.execute("Santos"), 10)
            coVerify{ repository.getZuppersQuantity(expectedCity) }
        }


}