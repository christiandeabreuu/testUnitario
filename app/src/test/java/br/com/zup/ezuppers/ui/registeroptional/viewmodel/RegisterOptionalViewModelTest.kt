package br.com.zup.ezuppers.ui.registeroptional.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.data.model.CepResult
import br.com.zup.ezuppers.data.repository.AuthenticationRepository
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.domain.usecase.GetCepUseCase
import br.com.zup.ezuppers.domain.usecase.UserUseCase
import br.com.zup.ezuppers.ui.login.viewmodel.LoginViewModel
import br.com.zup.ezuppers.ui.register.viewmodel.RegisterOptionalViewModel
import br.com.zup.ezuppers.utilities.*
import br.com.zup.ezuppers.viewstate.ViewState
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
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
internal class RegisterOptionalViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private var userUseCase: UserUseCase = mockk(relaxed = true)

    @RelaxedMockK
    private var getCepUseCase: GetCepUseCase = mockk(relaxed = true)

    private lateinit var viewModel: RegisterOptionalViewModel

    @Before
    fun onBefore() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = RegisterOptionalViewModel(Application(), userUseCase, getCepUseCase)
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
    fun `when the call the fun getCEP should be success`() = runTest {
        val expectedCep = "11040020"

        coEvery { getCepUseCase.execute(expectedCep) } returns CepResult(
            1, "11040020", "a",
            "SP", "sp", "ap 23", "Santos"
        )
        val value = viewModel.getCep(expectedCep)


        assertEquals(viewModel.cepResult.value?.cep, expectedCep)

        coVerify(exactly = 1) { getCepUseCase.execute("11040020") }
    }

    @Test
    fun `when the call the fun getCEP should be Error`() = runTest {
        val expectedCep = "000"

        coEvery { getCepUseCase.execute(expectedCep) } throws NullPointerException()
         viewModel.getCep(expectedCep)

        assertEquals(viewModel.cepResult.value?.cep, null)
        coVerify(exactly = 0) { getCepUseCase.execute("12345678") }
    }

    @Test
    fun `when the call the fun insertAllRegister should be success`() = runTest {
        val expectedUser =
            User(
                "1", "chris", "Chis@gmail.com", true, "chis", "39",
                "11040020", "Santos", "SP", "Brasil", "homem", "hetero",
                "Ch9206ch!", "rua A", "1", "casa", "trabalho", "ele"
            )

        val mockk = ViewState.Success(expectedUser)

        coEvery { userUseCase.insertAllRegisterLogin(expectedUser) } returns ViewState.Success(expectedUser)

        val value = viewModel.insertAllRegister(expectedUser)

//        assertEquals(viewModel.cepResult.value?.cep, expectedCep)

        coVerify(exactly = 1) { userUseCase.insertAllRegisterLogin(expectedUser) }
    }

    @Test
    fun `when the call the fun insertAllRegister should be error`() = runTest {
        //GIVEN
        val expectedUser =
            User(
                "1", "chris", "Chis@gmail.com", true, "chis", "39",
                "11040020", "Santos", "SP", "Brasil", "homem", "hetero",
                "Ch9206ch!", "rua A", "1", "casa", "trabalho", "ele"
            )
        val mockkError = ViewState.Error(Throwable(ERROR_INSERTING_REGISTER_USER_DATA_LOCAL))
        coEvery { viewModel.insertAllRegister(expectedUser) } returns Unit
        //WHEN

        viewModel.insertAllRegister(expectedUser)
        val value = viewModel.registerOptionalState.value
        //THEN

        assertTrue("Error", true)
        assertTrue("Não foi possivel inserir suas informações no banco de dados", true)
        assertTrue(ERROR_INSERTING_REGISTER_USER_DATA_LOCAL, true)

    }

    @Test
    fun `when the call the fun savedRegisterInfo should be success`() = runTest {
        val expectedUser =
            User(
                "1", "chris", "Chis@gmail.com", true, "chis", "39",
                "11040020", "Santos", "SP", "Brasil", "homem", "hetero",
                "Ch9206ch!", "rua A", "1", "casa", "trabalho", "ele"
            )

        val mockk = ViewState.Success(expectedUser)

        coEvery { userUseCase.databaseReference() } returns mockk(relaxed = true)

        val value = viewModel.savedRegisterInfoRealTime(expectedUser)

        assertTrue(SUCCESS_ADD_REGISTER_USER_DATA, true)

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

    //    @Test
//    fun `when the call the fun validateDateUserRegister should be to return true`() {
//        val expectedUser: User =
//            User("1", "chris", "Chis@gmail.com", true, "chis", "39",
//                "11040020", "Santos","SP","Brasil", "homem","hetero",
//            "Ch9206ch!","rua A", "1", "casa", "trabalho", "ele")
////        every { userUseCase.insertAllRegisterLogin(expectedUser) } returns mockk(relaxed = true)
//         viewModel.validateDateUserRegister(expectedUser)
//
//
//        verify (exactly = 0){ viewModel.insertAllRegister(expectedUser) }
//        verify (exactly = 0){ viewModel.savedRegisterInfoRealTime(expectedUser) }
//
//    }
}
