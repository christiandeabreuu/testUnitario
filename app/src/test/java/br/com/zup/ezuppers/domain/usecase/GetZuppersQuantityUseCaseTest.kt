package br.com.zup.ezuppers.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.data.repository.ZuppersRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
internal class GetZuppersQuantityUseCaseTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var repository: ZuppersRepository

    lateinit var getZuppersQuantityUseCase: GetZuppersQuantityUseCase

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
    fun `execute() should to call repository and return QuantityZuppers`() {
        val expectedCity = "Santos"

        every { repository.getZuppersQuantity(expectedCity) } returns 10
        val result = getZuppersQuantityUseCase.execute(expectedCity)

        assertEquals(result, 10)
        verify(exactly = 1) { repository.getZuppersQuantity(expectedCity) }
    }
}