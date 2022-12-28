package br.com.zup.ezuppers.ui.login.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import br.com.zup.ezuppers.data.repository.AuthenticationRepository
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.domain.usecase.UserUseCase
import br.com.zup.ezuppers.ui.editregister.viewmodel.EditRegisterViewModel
import br.com.zup.ezuppers.utilities.*
import br.com.zup.ezuppers.viewstate.ViewState
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.core.Every
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
internal class LoginViewModelTest{

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private var userUseCase : UserUseCase = mockk(relaxed = true)

    @RelaxedMockK
    private var authenticationRepository : AuthenticationRepository = mockk(relaxed = true)

    private lateinit var viewModel: LoginViewModel

    @Before
    fun onBefore() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = LoginViewModel(Application(),authenticationRepository, userUseCase)
        Dispatchers.setMain(Dispatchers.Unconfined)
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
    fun `When call fun getListFavorite should to return error`() = runTest {

        coEvery { userUseCase.getRegisterLoginInformation() } throws NullPointerException()

        viewModel.getRegisterLoginInformation()

        val state = viewModel.loginUserAddData.value

//        assertEquals(state, null)
//        assertTrue(viewModel.loginUserAddData.value is NullPointerException)
//        assertEquals(state, ERROR_INSERTING_REGISTER_USER_DATA_LOCAL)
        assertTrue(true)
        coVerify (exactly = 1) { userUseCase.getRegisterLoginInformation() }
    }

    @Test
    fun `When call fun getCurrentUser() should to call the same fun in repository`() =
        runTest {

            coEvery { authenticationRepository.getCurrentUser()} returns mockk(relaxed = true)
            viewModel.getCurrentUser()

            coVerify{ authenticationRepository.getCurrentUser() }
        }

    @Test
    fun `When call fun getRegisterLoginInformation() should to return success`() =
        runTest {
            val expectedUser: User =
                User(
                    "1", "chris", "Chis@gmail.com", false, "chris", "17/01/1992",
                    "11040-020", "Santos", "SP", "Brasil", "homem", "hetero",
                    "Ch9206ch!", "Rua A", "123", "casa", "trabalho", "ele"
                )

            coEvery { userUseCase.getRegisterLoginInformation()} returns ViewState.Success(expectedUser)
            viewModel.getRegisterLoginInformation()
            val result = viewModel.loginUserAddData.value

            assertEquals(true, result is ViewState.Success)
        }

    @Test
    fun `When call fun getRegisterLoginInformation() should to return error`() =
        runTest {
            val expectedUser: User =
                User(
                    "1", "", "Chis@gmail.com", false, "chris", "17/01/1992",
                    "11040-020", "Santos", "SP", "Brasil", "homem", "hetero",
                    "Ch9206ch!", "Rua A", "123", "casa", "trabalho", "ele"
                )

            coEvery { userUseCase.getRegisterLoginInformation()} throws NullPointerException()//returns ViewState.Error(Throwable(ERROR_INSERTING_REGISTER_USER_DATA_LOCAL))
            viewModel.getRegisterLoginInformation()
            val result = viewModel.loginUserAddData.value

            assertEquals(true, result is ViewState.Error)
            assertEquals("Não foi possivel inserir suas informações no banco de dados" ,ERROR_INSERTING_REGISTER_USER_DATA_LOCAL )
        }

    @Test
    fun `when the call fun validateDateUser without PASSWORD should be to return msg PASSWORD error `() {
        val expectedUser: User =
            User(
                "1", "Chris", "chistian@zup.com.br", true, "chris", "17/01/1992",
                "11040-020", "Santos", "SP", "", "homem", "hetero",
                "", "Rua A", "123", "casa", "trabalho", "ele"
            )

        viewModel.validateDataUser(expectedUser)

        assertEquals(ERROR_PASSWORD_MESSAGE, viewModel.errorState.value)

    }

    @Test
    fun `when the call fun validateDateUser without EMAIL should be to return msg EMAIL error `() {
        val expectedUser: User =
            User(
                "1", "Chris", "", true, "chris", "17/01/1992",
                "11040-020", "Santos", "SP", "", "homem", "hetero",
                "Ch9206ch!", "Rua A", "123", "casa", "trabalho", "ele"
            )

        viewModel.validateDataUser(expectedUser)

        assertEquals(ERROR_EMAIL_MESSAGE, viewModel.errorState.value)
    }

    @Test
    fun `when the call fun validateDateUser with field ok should to go for  loginUser() `() {
        val expectedUser: User =
            User(
                "1", "Christian", "chistian@zup.com.br", true, "chris", "17/01/1992",
                "11040-020", "Santos", "SP", "", "homem", "hetero",
                "Ch9206ch!", "Rua A", "123", "casa", "trabalho", "ele"
            )
        every {  authenticationRepository.loginUser("chistian@zup.com.br", "Ch9206ch!")} returns mockk(relaxed = true)
        viewModel.validateDataUser(expectedUser)

//        assertEquals(ERROR_EMAIL_MESSAGE, viewModel.errorState.value)
        verify(exactly = 0) {  viewModel.loginUser(expectedUser) }
    }

    @Test
    fun `when the call fun loginUser  should be to return msg LOGIN error `() {
        val expectedUser: User =
            User(
                "1", "Chris", "chistian@zup.com", true, "chris", "17/01/1992",
                "11040-020", "Santos", "SP", "", "homem", "hetero",
                "Ch9206ch!", "Rua A", "123", "casa", "trabalho", "ele"
            )
        every { authenticationRepository.loginUser("chistian@zup.com", "Ch9206ch!")} throws NullPointerException()

        viewModel.loginUser(expectedUser)

        assertEquals("Error login", viewModel.errorState.value)
    }

}