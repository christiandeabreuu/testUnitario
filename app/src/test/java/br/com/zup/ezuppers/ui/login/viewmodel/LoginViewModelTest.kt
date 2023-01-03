package br.com.zup.ezuppers.ui.login.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.data.repository.AuthenticationRepository
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.domain.usecase.UserUseCase
import br.com.zup.ezuppers.utilities.ERROR_EMAIL_MESSAGE
import br.com.zup.ezuppers.utilities.ERROR_INSERTING_REGISTER_USER_DATA_LOCAL
import br.com.zup.ezuppers.utilities.ERROR_PASSWORD_MESSAGE
import br.com.zup.ezuppers.viewstate.ViewState
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
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
    fun `when validateDateUser() is called without PASSWORD should be return PASSWORD error msg`() {
        val expectedUser: User = mockkUserWithoutPassword()

        viewModel.validateDataUser(expectedUser)

        assertEquals(ERROR_PASSWORD_MESSAGE, viewModel.errorState.value)
    }

    @Test
    fun `when validateDateUser() is calles without EMAIL should be return EMAIL error msg`() {
        val expectedUser: User = mockkUserWithoutEmail()

        viewModel.validateDataUser(expectedUser)

        assertEquals(ERROR_EMAIL_MESSAGE, viewModel.errorState.value)
    }

    @Test
    fun `when validateDateUser() is called with field ok should call loginUser() `() {
        val expectedUser: User = mockUser()

        viewModel.validateDataUser(expectedUser)

        assertEquals(expectedUser, viewModel.userIsValid.value)
    }

    @Test
    fun `when loginUser() is called should be return success `() {
        val expectedTaskAuthResult = mockk<Task<AuthResult>>()
        val expectedUser: User = mockUser()

        every { authenticationRepository.loginUser("chistian@zup.com.br", "Ch9206ch!")} returns expectedTaskAuthResult
        val result = viewModel.loginUserAddData.value as ViewState.Success

        viewModel.loginUser(expectedUser)

//        assertEquals(ViewState.Success(expectedUser), viewModel.loginUserAddData.value)
  //      assertThat(result is ViewState.Success ) ???
    }

    @Test
    fun `when loginUser() is called should be return msg LOGIN error`() {
        val expectedUser: User = mockUser()

        every { authenticationRepository.loginUser("chistian@zup.com.br", "Ch9206ch!")} throws NullPointerException()

        viewModel.loginUser(expectedUser)

        assertEquals("Error login", viewModel.errorState.value)
    }

    @Test
    fun `when getRegisterLoginInformation() is called should return success`() =
        runTest {
            val expectedUser: User = mockUser()

            coEvery { userUseCase.getRegisterLoginInformation()} returns ViewState.Success(expectedUser)
            viewModel.getRegisterLoginInformation()
            val result = viewModel.loginUserAddData.value

            assertEquals(true, result is ViewState.Success)
        }

    @Test
    fun `when getRegisterLoginInformation() is called should return error`() =
        runTest {

            coEvery { userUseCase.getRegisterLoginInformation()} throws NullPointerException()
            viewModel.getRegisterLoginInformation()
            val result = viewModel.loginUserAddData.value

            assertEquals(true, result is ViewState.Error)
            assertEquals("Não foi possivel inserir suas informações no banco de dados" ,ERROR_INSERTING_REGISTER_USER_DATA_LOCAL )
        }

    @Test
    fun `when getCurrentUser() is called should call the same fun on repository`() =
        runTest {
            coEvery { authenticationRepository.getCurrentUser()} returns mockk(relaxed = true)

            viewModel.getCurrentUser()

            coVerify{ authenticationRepository.getCurrentUser() }
        }

    @Test
    fun `getCurrentUser() is called should call the same fun on repository 2`() =
        runTest {
            val expectedFirebaseUser = mockk<FirebaseUser>()
            coEvery { authenticationRepository.getCurrentUser()} returns expectedFirebaseUser

            val response = viewModel.getCurrentUser()

            assert(expectedFirebaseUser == response)
            coVerify(exactly = 1){ authenticationRepository.getCurrentUser() }
        }

    private fun mockUser() = User(
    "1", "Chris", "chistian@zup.com.br", true, "chris", "17/01/1992",
    "11040-020", "Santos", "SP", "", "homem", "hetero",
    "Ch9206ch!", "Rua A", "123", "casa", "trabalho", "ele"
    )

    private fun mockkUserWithoutEmail() =  User(
        "1", "Chris", "", true, "chris", "17/01/1992",
        "11040-020", "Santos", "SP", "", "homem", "hetero",
        "Ch9206ch!", "Rua A", "123", "casa", "trabalho", "ele"
    )

    private fun mockkUserWithoutPassword() = User(
        "1", "Chris", "chistian@zup.com.br", true, "chris", "17/01/1992",
        "11040-020", "Santos", "SP", "", "homem", "hetero",
        "", "Rua A", "123", "casa", "trabalho", "ele"
    )
}