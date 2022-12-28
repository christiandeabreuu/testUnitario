package br.com.zup.ezuppers.ui.register.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.data.repository.AuthenticationRepository
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.domain.usecase.UserUseCase
import br.com.zup.ezuppers.utilities.COUNTRY_ERROR_MESSAGE
import br.com.zup.ezuppers.utilities.EMAIL_ERROR_MESSAGE
import br.com.zup.ezuppers.utilities.NAME_ERROR_MESSAGE
import br.com.zup.ezuppers.utilities.PASSWORD_ERROR_MESSAGE
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.Assert
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.internal.matchers.Null

@ExperimentalCoroutinesApi
internal class RegisterLoginViewModelTest{

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private var userUseCase : UserUseCase = mockk(relaxed = true)

    @RelaxedMockK
    private var authenticationRepository : AuthenticationRepository = mockk(relaxed = true)

    private lateinit var viewModel: RegisterLoginViewModel

    @Before
    fun onBefore() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = RegisterLoginViewModel(Application(), userUseCase, authenticationRepository )
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
    fun `when the call the fun haveErrorsDateUserRegister without NAME and should be to return msg NAME error `() {
        val expectedUser: User =
            User(
                "1", "", "Chis@zup.com.br", false, "chris", "17/01/1992",
                "11040-020", "Santos", "SP", "", "homem", "hetero",
                "Ch9206ch!", "Rua A", "123", "casa", "trabalho", "ele"
            )

        viewModel.haveErrorsDateUser(expectedUser)

        Assert.assertEquals(NAME_ERROR_MESSAGE, viewModel.messageState.value)

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

        Assert.assertEquals(EMAIL_ERROR_MESSAGE, viewModel.messageState.value)

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

        Assert.assertEquals(PASSWORD_ERROR_MESSAGE, viewModel.messageState.value)

    }

    @Test
    fun `when to call fun haveErrorsDate UserRegister with valid fields dont should be to return error msg `() {
        val expectedUser: User =
            User(
                "1", "christian", "chistian@zup.com.br", true, "chris", "17/01/1992",
                "11040-020", "Santos", "SP", "", "homem", "hetero",
                "Ch9206ch!", "Rua A", "123", "casa", "trabalho", "ele"
            )

        viewModel.haveErrorsDateUser(expectedUser)

        Assert.assertEquals(viewModel.messageState.value, null)


    }
}