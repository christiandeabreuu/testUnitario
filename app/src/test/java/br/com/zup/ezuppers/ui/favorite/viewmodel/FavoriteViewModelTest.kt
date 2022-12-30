package br.com.zup.ezuppers.ui.favorite.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.data.repository.ZuppersRepository
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.domain.usecase.GetFavoriteUseCase
import br.com.zup.ezuppers.domain.usecase.UserUseCase
import br.com.zup.ezuppers.ui.editregister.viewmodel.EditRegisterViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.any
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule


@ExperimentalCoroutinesApi
internal class FavoriteViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private var getFavoritesUseCase: GetFavoriteUseCase = mockk(relaxed = true)

    @RelaxedMockK
    private var repository: ZuppersRepository = mockk(relaxed = true)

    private lateinit var viewModel: FavoriteViewModel

    @Before
    fun onBefore() {
        MockKAnnotations.init(this, relaxUnitFun = true)
//        getFavoritesUseCase = GetFavoriteUseCase(repository)  //mockk(relaxed = true)
        viewModel = FavoriteViewModel(Application(), getFavoritesUseCase)
        Dispatchers.setMain(Dispatchers.Unconfined) //TestDispatcher
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
        unmockkAll()
    }


    @Test
    fun `When call fun getListFavorite should to return success`() = runTest {

        coEvery { getFavoritesUseCase.execute() } returns mockkListUsers()

        viewModel.getListFavorite()

        val expectedListUsers = mockkListUsers()
        val state = viewModel.favoriteZuppers.value

        assertEquals(state, expectedListUsers)
//        verifyListUser(state!!)
        coVerify(exactly = 1) { getFavoritesUseCase.execute() }
    }

    private fun mockkListUsers() = listOf(
        User(
            name = "Amanda",
            email = "amanda.torres@zup.com.br",
            nickname = "Amandinha",
            interests = "----",
            state = "Rio de Janeiro",
            favorite = true
        ),
        User(
            name = "Diogo",
            email = "diogo.garcia@zup.com.br",
            nickname = "Di",
            interests = "----",
            state = "Rio de Janeiro",
            favorite = true
        ),
        User(
            name = "Henrique",
            email = "henrique.carvalho@zup.com.br",
            nickname = "Henry",
            interests = "----",
            state = "Acre",
            favorite = true
        )
    )

    @Test
    fun `When call fun getListFavorite should to return success 2 `() = runTest {
        val expectedMockkListUser = mockk<List<User>>()
        coEvery { getFavoritesUseCase.execute() } returns expectedMockkListUser

        viewModel.getListFavorite()

        val response = viewModel.favoriteZuppers.value

        assertEquals(expectedMockkListUser, response )
        coVerify(exactly = 1) { getFavoritesUseCase.execute() }
    }

    @Test
    fun `When call fun getListFavorite should to return error`() = runBlocking {

        coEvery { viewModel.getListFavorite() } throws NullPointerException()

        viewModel.getListFavorite()

        val result = viewModel.errorState.value

        assertEquals(result , "error Exception")
    }
}
