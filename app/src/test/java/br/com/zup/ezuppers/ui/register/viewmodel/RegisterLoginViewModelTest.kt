package br.com.zup.ezuppers.ui.register.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.data.repository.AuthenticationRepository
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.domain.usecase.UserUseCase
import br.com.zup.ezuppers.utilities.EMAIL_ERROR_MESSAGE
import br.com.zup.ezuppers.utilities.NAME_ERROR_MESSAGE
import br.com.zup.ezuppers.utilities.PASSWORD_ERROR_MESSAGE
import br.com.zup.ezuppers.viewstate.ViewState
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
internal class RegisterLoginViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private var userUseCase: UserUseCase = mockk(relaxed = true)

    @RelaxedMockK
    private var repository: AuthenticationRepository = mockk(relaxed = true)

    private lateinit var viewModel: RegisterLoginViewModel

    @Before
    fun onBefore() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = RegisterLoginViewModel(Application(), userUseCase, repository)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `when haveErrorsDateUserRegister() is called without NAME and should be to return NAME error msg`() {
        val userWithoutName = mockUserWithoutName()

        viewModel.haveErrorsDateUser(userWithoutName)

        assertEquals(NAME_ERROR_MESSAGE, viewModel.messageState.value)
    }

    @Test
    fun `when haveErrorsDateUserRegister is called without EMAIL and should be to return ERROR error msg`() {
        val userWithoutEmail = mockUserWithoutEmail()

        viewModel.haveErrorsDateUser(userWithoutEmail)

        assertEquals(EMAIL_ERROR_MESSAGE, viewModel.messageState.value)
    }

    @Test
    fun `when haveErrorsDateUserRegister is called without PASSWORD should be to return PASSWORD error msg`() {
        val userWithoutPassword = mockUserWithoutPassword()

        viewModel.haveErrorsDateUser(userWithoutPassword)

        assertEquals(PASSWORD_ERROR_MESSAGE, viewModel.messageState.value)
    }

    @Test
    fun `when haveErrorsDateUserRegister() is called with valid fields should create valid user`() {
        val expectedUser: User = mockUser()

        viewModel.haveErrorsDateUser(expectedUser)

        assertEquals(mockUser(), viewModel.userIsValid.value)
    }

    @Test
    fun `when insertRegisterLoginUserData() is called should to return success`() =
        runTest {
            val expectedViewStateUser = mockk<ViewState<User>>()
            val expectedUser: User = mockUser()

            coEvery { userUseCase.insertAllRegisterLogin(expectedUser) } returns ViewState.Success(
                expectedUser
            )

            viewModel.insertRegisterLoginUserData(expectedUser)
            val response = viewModel.registerUserLoginState.value

            assert(response is ViewState.Success)
            coVerify { userUseCase.insertAllRegisterLogin(expectedUser) }
        }

    @Test
    fun `When insertRegisterLoginUserData() is called should to return error`() =
        runTest {

            val expectedUser: User = mockUser()

            coEvery { userUseCase.insertAllRegisterLogin(expectedUser) } throws NullPointerException()

            viewModel.insertRegisterLoginUserData(expectedUser)
            val response = viewModel.registerUserLoginState.value

            assert(response is ViewState.Error)
        }

    @Test
    fun `When getRegisterLoginInformation() is called should to return success`() =
        runTest {
            val expectedUser: User = mockUser()

            coEvery { userUseCase.getRegisterLoginInformation() } returns ViewState.Success(expectedUser)

            viewModel.getRegisterLoginInformation()
            val response = viewModel.registerUserLoginState.value

            assert(response is ViewState.Success)
            coVerify { userUseCase.getRegisterLoginInformation() }
        }

    @Test
    fun `When getRegisterLoginInformation() is called should to return error`() =
        runTest {
            coEvery { userUseCase.getRegisterLoginInformation() } throws NullPointerException()

            viewModel.getRegisterLoginInformation()
            val response = viewModel.registerUserLoginState.value

            assert(response is ViewState.Error)
        }

    private fun mockUser(): User =
        User(
            "1", "christian", "chistian@zup.com.br", true, "chris", "17/01/1992",
            "11040-020", "Santos", "SP", "", "homem", "hetero",
            "Ch9206ch!", "Rua A", "123", "casa", "trabalho", "ele"
        )

    private fun mockUserWithoutName(): User =
        User(
            "1", "", "chistian@zup.com.br", true, "chris", "17/01/1992",
            "11040-020", "Santos", "SP", "", "homem", "hetero",
            "Ch9206ch!", "Rua A", "123", "casa", "trabalho", "ele"
        )

    private fun mockUserWithoutEmail(): User =
        User(
            "1", "christian", "", true, "chris", "17/01/1992",
            "11040-020", "Santos", "SP", "", "homem", "hetero",
            "Ch9206ch!", "Rua A", "123", "casa", "trabalho", "ele"
        )

    private fun mockUserWithoutPassword(): User =
        User(
            "1", "christian", "chistian@zup.com.br", true, "chris", "17/01/1992",
            "11040-020", "Santos", "SP", "", "homem", "hetero",
            "", "Rua A", "123", "casa", "trabalho", "ele"
        )

}