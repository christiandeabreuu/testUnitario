package br.com.zup.ezuppers.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.data.repository.UfRepository
import br.com.zup.ezuppers.data.repository.ZuppersRepository
import br.com.zup.ezuppers.domain.model.User
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

internal class GetZuppersUseCaseTest{


    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var repository: ZuppersRepository

    lateinit var getZuppersUseCase : GetZuppersUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        getZuppersUseCase = GetZuppersUseCase(repository)
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

            coEvery { repository.getZuppers(expectedCity) } returns mockkListUsers()

            val value = getZuppersUseCase.execute(expectedCity)
            val expectedListUsers = mockkListUsers()

            assertEquals(value, expectedListUsers)
            coVerify(exactly = 1) { repository.getZuppers(expectedCity) }
        }

    private fun mockkListUsers() = listOf(
        User("1"),
        User("2"),
        User("3")
    )

}