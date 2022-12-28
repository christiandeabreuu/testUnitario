package br.com.zup.ezuppers.ui.userprofile.viewmodel

import android.app.Application
import android.app.DownloadManager.Query
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.data.repository.AuthenticationRepository
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.domain.usecase.UserUseCase
import br.com.zup.ezuppers.ui.register.viewmodel.RegisterOptionalViewModel
import br.com.zup.ezuppers.utilities.ERROR_REGISTER_DATA_LOCAL
import br.com.zup.ezuppers.utilities.ERROR_REGISTER_USER_DATA_LOCAL
import br.com.zup.ezuppers.viewstate.ViewState
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
internal class UserProfileViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private var userUseCase: UserUseCase = mockk(relaxed = true)

    @RelaxedMockK
    private var authenticationRepository: AuthenticationRepository = mockk(relaxed = true)

    private lateinit var viewModel: UserProfileViewModel

    @Before
    fun onBefore() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = UserProfileViewModel(authenticationRepository, userUseCase)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `Dois mais dois nao é cinco `() {
        assert(2 + 2 != 5)
    }

    @Test
    fun `When call fun getCurrentUserRegister() should to call the same fun in useCase`() =
        runTest {

            coEvery { userUseCase.getCurrentUser() } returns mockk(relaxed = true)
            viewModel.getCurrentUser()

            coVerify { userUseCase.getCurrentUser() }
        }

    @Test
    fun `When call fun logout() should to call the same fun in useCase`() =
        runTest {

            coEvery { authenticationRepository.logOutUser() } returns mockk(relaxed = true)
            viewModel.logOut()

            coVerify { authenticationRepository.logOutUser() }
        }

    @Test
    fun `When call fun getAllRegisterInformationOffline() should to call the same fun in useCase`() =
        runTest {
            val expectedEmail = "Chris@zup.com"

            coEvery { userUseCase.getAllRegisterInformationOffline(expectedEmail) } returns ViewState.Success(
                mockkListUsers()
            )
            viewModel.getAllRegisterInformationOffline(expectedEmail)
            testDispatcher.scheduler.advanceUntilIdle()
             val result = viewModel.userProfileListState.value

            assertEquals(3, result)

        }

    @Test
    fun `When call fun getAllRegisterInformationOffline() should to call error`() =
        runTest {
            val expectedEmail = "Chris@zup.com"

            coEvery { userUseCase.getAllRegisterInformationOffline(expectedEmail) } returns ViewState.Error(
                Throwable("message error")
            )
            viewModel.getAllRegisterInformationOffline(expectedEmail)
            testDispatcher.scheduler.advanceUntilIdle()
            val result = viewModel.userProfileListState.value

//            assertEquals(true, result is ViewState.Error)
            assertEquals("message error", viewModel.message.value)
//
        }

    @Test
    fun `When call fun getAllRegisterInformationOffline() should to call error `() =
        runTest {
            val expectedEmail = "Chris@zup.com"

            coEvery { userUseCase.getAllRegisterInformationOffline(expectedEmail) } throws NullPointerException()

            viewModel.getAllRegisterInformationOffline(expectedEmail)
            testDispatcher.scheduler.advanceUntilIdle()
            val result = viewModel.userProfileListState.value

            assertEquals(true, result is ViewState.Error)

        }
    private fun mockkListUsers() = listOf(
        User("1"),
        User("2"),
        User("3")
    )




}