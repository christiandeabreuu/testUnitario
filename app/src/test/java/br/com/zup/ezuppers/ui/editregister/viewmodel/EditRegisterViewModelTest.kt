package br.com.zup.ezuppers.ui.editregister.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModel
import br.com.zup.ezuppers.data.model.CepResult
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.domain.usecase.GetCepUseCase
import br.com.zup.ezuppers.domain.usecase.UserUseCase
import br.com.zup.ezuppers.utilities.*
import br.com.zup.ezuppers.viewstate.ViewState
import com.google.firebase.auth.FirebaseUser
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
internal class EditRegisterViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private var editUserUseCase: UserUseCase = mockk(relaxed = true)

    @RelaxedMockK
    private var getCepUseCase : GetCepUseCase = mockk(relaxed = true)

    private lateinit var viewModel: EditRegisterViewModel

    @Before
    fun onBefore() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = EditRegisterViewModel(application = Application(), editUserUseCase, getCepUseCase)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
        unmockkAll()
    }



    @Test
    fun `when the call the fun getCEP should be success`() = runTest{
        val expectedCep = "11040020"
        val expectedMockkCepResult = mockk<CepResult>()

        coEvery { getCepUseCase.execute(expectedCep) } returns CepResult(1, "11040020", "a", "SP", "sp", "ap 23", "Santos") //expectedMockkCepResult//

            viewModel.getCep(expectedCep)
        val response = viewModel.cepResult.value

        assertEquals(viewModel.cepResult.value is ViewState.Success, true)
        assertTrue(response is ViewState.Success)
        assertEquals("11040020",(response as ViewState.Success).data.cep)
        coVerify (exactly = 1){ getCepUseCase.execute("11040020") }
    }

    @Test
    fun `when the call the fun getCEP should be Error`() = runTest {
        val expectedCep = "000"

        coEvery { getCepUseCase.execute(expectedCep) } throws NullPointerException()
        viewModel.getCep(expectedCep)
        val response = viewModel.cepResult.value

        assertEquals(response is ViewState.Error, true)
        assertTrue(response is ViewState.Error)
    }

    @Test
    fun `when the call the fun haveErrorsDateUserRegister without NICKNAME and should be to return msg NICKNAME error `() {
        val expectedUser: User =
            User(
                "1", "chris", "Chis@gmail.com", true, "", "39",
                "11040-020", "Santos", "SP", "Brasil", "homem", "hetero",
                "Ch9206ch!", "rua A", "1", "casa", "trabalho", "ele"
            )

        viewModel.validateDateUserRegister(expectedUser)

        assertEquals(NICKNAME_ERROR_MESSAGE, viewModel.messageState.value)

    }

    @Test
    fun `when the call the fun haveErrorsDateUserRegister without CEP and should be to return msg CEP error `() {
        val expectedUser: User =
            User(
                "1", "chris", "Chis@gmail.com", true, "chris", "39",
                "", "Santos", "SP", "Brasil", "homem", "hetero",
                "Ch9206ch!", "rua A", "1", "casa", "trabalho", "ele"
            )

        viewModel.validateDateUserRegister(expectedUser)

        assertEquals(CEP_ERROR_MESSAGE, viewModel.messageState.value)
    }

    @Test
    fun `when the call the fun haveErrorsDateUserRegister without COMPLEMENT and should be to return msg COMPLEMENT error `() {
        val expectedUser: User =
            User(
                "1", "chris", "Chis@gmail.com", false, "chris", "39",
                "11040-020", "Santos", "SP", "Brasil", "homem", "hetero",
                "Ch9206ch!", "rua A", "1", "", "trabalho", "ele"
            )

        viewModel.validateDateUserRegister(expectedUser)

        assertEquals(COMPLEMENT_ERROR_MESSAGE, viewModel.messageState.value)
    }

    @Test
    fun `when the call the fun haveErrorsDateUserRegister without NUMBER and should be to return msg NUMBER error `() {
        val expectedUser: User =
            User(
                "1", "chris", "Chis@gmail.com", false, "chris", "17/01/1992",
                "11040-020", "Santos", "Sp", "Brasil", "homem", "hetero",
                "Ch9206ch!", "rua A", "", "casa", "trabalho", "ele"
            )

        viewModel.validateDateUserRegister(expectedUser)

        assertEquals(NUMBER_ERROR_MESSAGE, viewModel.messageState.value)
    }

    @Test
    fun `when the call the fun haveErrorsDateUserRegister without State and should be to return msg State error `() {
        val expectedUser: User =
            User(
                "1", "chris", "Chis@gmail.com", false, "chris", "17/01/1992",
                "11040-020", "Santos", "", "Brasil", "homem", "hetero",
                "Ch9206ch!", "rua A", "123", "casa", "trabalho", "ele"
            )

        viewModel.validateDateUserRegister(expectedUser)

        assertEquals(STATE_ERROR_MESSAGE, viewModel.messageState.value)
    }

    @Test
    fun `when the call the fun haveErrorsDateUserRegister without RoadAv and should be to return msg RoadAv error `() {
        val expectedUser: User =
            User(
                "1", "chris", "Chis@gmail.com", false, "chris", "17/01/1992",
                "11040-020", "Santos", "SP", "Brasil", "homem", "hetero",
                "Ch9206ch!", "", "123", "casa", "trabalho", "ele"
            )

        viewModel.validateDateUserRegister(expectedUser)

        assertEquals(ROAD_ERROR_MESSAGE, viewModel.messageState.value)
    }

    @Test
    fun `when the call the fun haveErrorsDateUserRegister without AGE and should be to return msg AGE error`() {
        val expectedUser: User =
            User(
                "1", "chris", "Chis@gmail.com", false, "chris", "",
                "11040-020", "Santos", "", "Brasil", "homem", "hetero",
                "Ch9206ch!", "Rua A", "123", "casa", "trabalho", "ele"
            )

        viewModel.validateDateUserRegister(expectedUser)

        assertEquals(AGE_ERROR_MESSAGE, viewModel.messageState.value)
    }

    @Test
    fun `when the call the fun haveErrorsDateUserRegister without CITY and should be to return msg CITY error `() {
        val expectedUser: User =
            User(
                "1", "chris", "Chis@gmail.com", false, "chris", "17/01/1992",
                "11040-020", "", "SP", "Brasil", "homem", "hetero",
                "Ch9206ch!", "Rua A", "123", "casa", "trabalho", "ele"
            )

        viewModel.validateDateUserRegister(expectedUser)

        assertEquals(CITY_ERROR_MESSAGE, viewModel.messageState.value)
    }

    @Test
    fun `when the call the fun haveErrorsDateUserRegister without Country and should be to return msg Country error `() {
        val expectedUser: User =
            User(
                "1", "chris", "Chis@gmail.com", false, "chris", "17/01/1992",
                "11040-020", "Santos", "SP", "", "homem", "hetero",
                "Ch9206ch!", "Rua A", "123", "casa", "trabalho", "ele"
            )

        viewModel.validateDateUserRegister(expectedUser)

        assertEquals(COUNTRY_ERROR_MESSAGE, viewModel.messageState.value)
    }

    @Test
    fun `when the call the fun haveErrorsDateUserRegister without INTEREST and should be to return msg INTEREST error `() {
        val expectedUser: User =
            User(
                "1", "chris", "Chis@gmail.com", false, "chris", "17/01/1992",
                "11040-020", "Santos", "SP", "Brasil", "homem", "hetero",
                "Ch9206ch!", "Rua A", "123", "casa", "", "ele"
            )

        viewModel.validateDateUserRegister(expectedUser)

        assertEquals(INTEREST_ERROR_MESSAGE, viewModel.messageState.value)

    }
    @Test
    fun `when to call fun haveErrorsDateUserRegister with valid fields dont should be to return error msg`() {
        val expectedUser: User =
            User(
                "1", "chris", "Chis@gmail.com", false, "chris", "17/01/1992",
                "11040-020", "Santos", "SP", "Brasil", "homem", "hetero",
                "Ch9206ch!", "Rua A", "123", "casa", "", "ele"
            )

        viewModel.validateDateUserRegister(expectedUser)

        assertEquals(INTEREST_ERROR_MESSAGE, viewModel.messageState.value)
    }

    @Test
    fun `When call fun getCurrentUserRegister() should to call the same fun in useCase`() =
        runTest {
            coEvery { editUserUseCase.getCurrentUser()} returns mockk(relaxed = true)

            viewModel.getCurrentUserRegister()

            coVerify{ editUserUseCase.getCurrentUser() }
        }

    @Test
    fun `When call fun getCurrentUserRegister() should to call the same fun in useCase 2`() =
        runTest {
            val expectedFirebaseUser = mockk<FirebaseUser>()
            coEvery { editUserUseCase.getCurrentUser()} returns expectedFirebaseUser

            val response = viewModel.getCurrentUserRegister()

            assert(expectedFirebaseUser == response)
            coVerify(exactly = 1){ editUserUseCase.getCurrentUser() }
        }


}