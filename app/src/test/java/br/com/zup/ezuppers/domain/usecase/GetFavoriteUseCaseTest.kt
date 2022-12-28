package br.com.zup.ezuppers.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.data.model.CitiesResult
import br.com.zup.ezuppers.data.repository.UfRepository
import br.com.zup.ezuppers.data.repository.ZuppersRepository
import br.com.zup.ezuppers.domain.model.User
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

internal class GetFavoriteUseCaseTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var repository: ZuppersRepository

    lateinit var getFavoriteUseCase: GetFavoriteUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        getFavoriteUseCase = GetFavoriteUseCase(repository)
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
            coEvery { repository.getFavorites() } returns mockkListUsers()
            val value = getFavoriteUseCase.execute()
            val expectedListUsers = mockkListUsers()

            assertEquals(value, expectedListUsers)
            coVerify(exactly = 1) { repository.getFavorites() }
        }

    private fun mockkListUsers() = listOf(
        User("1"),
        User("2"),
        User("3")
    )


}