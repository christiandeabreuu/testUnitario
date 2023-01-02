package br.com.zup.ezuppers.ui.userprofile.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.data.repository.AuthenticationRepository
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.domain.usecase.UserUseCase
import br.com.zup.ezuppers.utilities.ERROR_REGISTER_USER_DATA_LOCAL
import br.com.zup.ezuppers.viewstate.ViewState
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
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
    fun `When getCurrentUserRegister() is called should to call the same fun in useCase`() =
        runTest {

            coEvery { userUseCase.getCurrentUser() } returns mockk(relaxed = true)
            viewModel.getCurrentUser()

            coVerify { userUseCase.getCurrentUser() }
        }

    @Test
    fun `When logout() is called should to call the same fun in useCase`() =
        runTest {

            coEvery { authenticationRepository.logOutUser() } returns mockk(relaxed = true)
            viewModel.logOut()

            coVerify { authenticationRepository.logOutUser() }
        }

    @Test
    fun `When getAllRegisterInformationOffline() is called should to call the same fun in useCase`() =
        runTest {
            val expectedUserId = "Chris@zup.com"
            val expectedViewStateListUsers = mockk<ViewState<List<User>>>()

            coEvery { userUseCase.getAllRegisterInformationOffline(expectedUserId) } returns expectedViewStateListUsers//ViewState.Success(mockkListUsers())
            val response = viewModel.getAllRegisterInformationOffline(expectedUserId)
            testDispatcher.scheduler.advanceUntilIdle()
            val result = viewModel.userProfileListState.value


            assert(response == Unit)
            assertEquals(false, result is ViewState.Success)
            assertThat(result).isEqualTo(expectedViewStateListUsers)

        }

    @Test
    fun `When getAllRegisterInformationOffline() is called should return error `() =
        runTest {
            val expectedEmail = "Chris@zup.com"
            val expectedResponse = mockk<ViewState<List<User>>>()

            coEvery { userUseCase.getAllRegisterInformationOffline(expectedEmail) } throws NullPointerException()

            viewModel.getAllRegisterInformationOffline(expectedEmail)
            testDispatcher.scheduler.advanceUntilIdle()
            val result = viewModel.userProfileListState.value
            val result2 = viewModel.message.value


            assertEquals(true, result is ViewState.Error)
            assert(result2 == ERROR_REGISTER_USER_DATA_LOCAL)
        }
}