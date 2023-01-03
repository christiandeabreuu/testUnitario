package br.com.zup.ezuppers.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.data.repository.ZuppersRepository
import br.com.zup.ezuppers.domain.model.User
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
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
    fun `when execute() is called should to call repository and return ListUsers`() {
        every { repository.getFavorites() } returns mockkListUsers()
        val result = getFavoriteUseCase.execute()
        val expectedListUsers = mockkListUsers()

        assertEquals(result, expectedListUsers)
        verify(exactly = 1) { repository.getFavorites() }
    }

    @Test
    fun `execute() should to call repository and return ListUsers (2)`() {
        val listUsers = mockk<List<User>>()
        every { repository.getFavorites() } returns listUsers

        val result = getFavoriteUseCase.execute()

        assertEquals(result, listUsers)
        verify(exactly = 1) { repository.getFavorites() }
    }

    private fun mockkListUsers() = listOf(
        User("1"),
        User("2"),
        User("3")
    )
}