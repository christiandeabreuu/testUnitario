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
    fun `when the call the fun haveErrorsDateUserRegister without NAME and should be to return msg NAME error `() {
        val expectedUser: User =
            User(
                "1", "", "Chis@zup.com.br", false, "chris", "17/01/1992",
                "11040-020", "Santos", "SP", "", "homem", "hetero",
                "Ch9206ch!", "Rua A", "123", "casa", "trabalho", "ele"
            )

        viewModel.haveErrorsDateUser(expectedUser)

        assertEquals(NAME_ERROR_MESSAGE, viewModel.messageState.value)

    }

    @Test
    fun `when the call the fun haveErrorsDateUserRegister without EMAIL and should be to return msg ERROR error `() {
        val expectedUser: User =
            User(
                "1", "Chris", "", false, "chris", "17/01/1992",
                "11040-020", "Santos", "SP", "", "homem", "hetero",
                "Ch9206ch!", "Rua A", "123", "casa", "trabalho", "ele"
            )

        viewModel.haveErrorsDateUser(expectedUser)

        assertEquals(EMAIL_ERROR_MESSAGE, viewModel.messageState.value)

    }

    @Test
    fun `when the call fun haveErrorsDateUserRegister without PASSWORD should be to return msg PASSWORD error `() {
        val expectedUser: User =
            User(
                "1", "Chris", "chistian@zup.com.br", true, "chris", "17/01/1992",
                "11040-020", "Santos", "SP", "", "homem", "hetero",
                "", "Rua A", "123", "casa", "trabalho", "ele"
            )

        viewModel.haveErrorsDateUser(expectedUser)

        assertEquals(PASSWORD_ERROR_MESSAGE, viewModel.messageState.value)

    }

    @Test
    fun `when haveErrorsDateUserRegister() is called with valid fields should create valid user`() {
        val expectedUser: User =
            User(
                "1", "christian", "chistian@zup.com.br", true, "chris", "17/01/1992",
                "11040-020", "Santos", "SP", "", "homem", "hetero",
                "Ch9206ch!", "Rua A", "123", "casa", "trabalho", "ele"
            )

        viewModel.haveErrorsDateUser(expectedUser)

        assertEquals(expectedUser, viewModel.userIsValid.value)
    }

    @Test
    fun `insertRegisterLoginUserData() should to return success`() =
        runTest {
            val expectedViewStateUser = mockk<ViewState<User>>()
            val expectedUser: User =
                User(
                    "1",
                    "chris",
                    "christian@zup.com.br",
                    false,
                    "chris",
                    "17/01/1992",
                    "11040-020",
                    "Santos",
                    "SP",
                    "Brasil",
                    "homem",
                    "hetero",
                    "Ch9206ch!",
                    "Rua A",
                    "123",
                    "casa",
                    "trabalho",
                    "ele"
                )
            coEvery { userUseCase.insertAllRegisterLogin(expectedUser) } returns ViewState.Success(
                expectedUser
            )

            viewModel.insertRegisterLoginUserData(expectedUser)
            val response = viewModel.registerUserLoginState.value

            assert(response is ViewState.Success)
            coVerify { userUseCase.insertAllRegisterLogin(expectedUser) }
        }

    @Test
    fun `When call fun insertRegisterLoginUserData() should to return error`() =
        runTest {

            val expectedUser: User =
                User(
                    "1",
                    "chris",
                    "christian@zup.com.br",
                    false,
                    "chris",
                    "17/01/1992",
                    "11040-020",
                    "Santos",
                    "SP",
                    "Brasil",
                    "homem",
                    "hetero",
                    "Ch9206ch!",
                    "Rua A",
                    "123",
                    "casa",
                    "trabalho",
                    "ele"
                )
            coEvery { userUseCase.insertAllRegisterLogin(expectedUser) } throws NullPointerException()

            viewModel.insertRegisterLoginUserData(expectedUser)
            val response = viewModel.registerUserLoginState.value

            assert(response is ViewState.Error)
        }

    @Test
    fun `When call fun getRegisterLoginInformation() should to return success`() =
        runTest {
            val expectedUser: User =
                User(
                    "1",
                    "chris",
                    "christian@zup.com.br",
                    false,
                    "chris",
                    "17/01/1992",
                    "11040-020",
                    "Santos",
                    "SP",
                    "Brasil",
                    "homem",
                    "hetero",
                    "Ch9206ch!",
                    "Rua A",
                    "123",
                    "casa",
                    "trabalho",
                    "ele"
                )
            coEvery { userUseCase.getRegisterLoginInformation() } returns ViewState.Success(
                expectedUser
            )

            viewModel.getRegisterLoginInformation()
            val response = viewModel.registerUserLoginState.value

            assert(response is ViewState.Success)
            coVerify { userUseCase.getRegisterLoginInformation() }
        }

    @Test
    fun `When call fun getRegisterLoginInformation() should to return error`() =
        runTest {

            val expectedUser: User =
                User(
                    "1",
                    "chris",
                    "christian@zup.com.br",
                    false,
                    "chris",
                    "17/01/1992",
                    "11040-020",
                    "Santos",
                    "SP",
                    "Brasil",
                    "homem",
                    "hetero",
                    "Ch9206ch!",
                    "Rua A",
                    "123",
                    "casa",
                    "trabalho",
                    "ele"
                )
            coEvery { userUseCase.getRegisterLoginInformation() } throws NullPointerException()

            viewModel.getRegisterLoginInformation()
            val response = viewModel.registerUserLoginState.value

            assert(response is ViewState.Error)
        }


}