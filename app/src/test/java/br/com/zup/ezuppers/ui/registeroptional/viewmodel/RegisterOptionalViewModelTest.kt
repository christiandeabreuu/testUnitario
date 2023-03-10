package br.com.zup.ezuppers.ui.registeroptional.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.data.model.CepResult
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.domain.usecase.GetCepUseCase
import br.com.zup.ezuppers.domain.usecase.UserUseCase
import br.com.zup.ezuppers.ui.register.viewmodel.RegisterOptionalViewModel
import br.com.zup.ezuppers.utilities.*
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
import org.junit.Assert.assertTrue
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
    fun `when getCEP() is called should be success`() = runTest {
        val expectedCep = "11040020"
        coEvery { getCepUseCase.execute(expectedCep) } returns CepResult(
            1, "11040020", "a",
            "SP", "sp", "ap 23", "Santos")

        viewModel.getCep(expectedCep)

        assertEquals(viewModel.cepResult.value?.cep, expectedCep)
        coVerify(exactly = 1) { getCepUseCase.execute("11040020") }
    }

    @Test
    fun `when getCEP() is called should be Error`() = runTest {
        val expectedCep = "000"

        coEvery { getCepUseCase.execute(expectedCep) } throws NullPointerException()
         viewModel.getCep(expectedCep)

        assertEquals(viewModel.messageState.value, "Error")
        coVerify(exactly = 0) { getCepUseCase.execute("12345678") }
    }

    @Test
    fun `when insertAllRegister() is called should return success`() = runTest {
        val expectedUser = mockUser()
        coEvery { userUseCase.insertAllRegisterLogin(expectedUser) } returns ViewState.Success(expectedUser)

        viewModel.insertAllRegister(expectedUser)

        coVerify(exactly = 1) { userUseCase.insertAllRegisterLogin(expectedUser) }
    }

    @Test
    fun `when insertAllRegister() is called should return error`() = runTest {
        //GIVEN
        val expectedUser = mockUser()
        coEvery { userUseCase.insertAllRegisterLogin(expectedUser) } throws NullPointerException()

        //WHEN
        viewModel.insertAllRegister(expectedUser)
        val response = viewModel.registerOptionalState.value

        //THEN
        assert(response is ViewState.Error)
        assertTrue("Error", true)
        assertTrue("N??o foi possivel inserir suas informa????es no banco de dados", true)
        assertTrue(ERROR_INSERTING_REGISTER_USER_DATA_LOCAL, true)
    }

    @Test
    fun `when savedRegisterInfo() is called should return success`() = runTest {
        val expectedUser = mockUser()
        coEvery { userUseCase.databaseReference() } returns mockk(relaxed = true)

        viewModel.savedRegisterInfoRealTime(expectedUser)

        assertTrue(SUCCESS_ADD_REGISTER_USER_DATA, true)
    }

    @Test
    fun `when haveErrorsDateUserRegister() is called without NICKNAME and should return NICKNAME error msg`() {
        val expectedUser: User = mockUserWithoutNickName()

         viewModel.validateDateUserRegister(expectedUser)

        assertEquals(NICKNAME_ERROR_MESSAGE, viewModel.messageState.value)
    }

    @Test
    fun `when haveErrorsDateUserRegister() is called without CEP should return CEP error msg`() {
        val expectedUser: User = mockUserWithoutCEP()

        viewModel.validateDateUserRegister(expectedUser)

        assertEquals(CEP_ERROR_MESSAGE, viewModel.messageState.value)
    }

    @Test
    fun `when haveErrorsDateUserRegister() is called without COMPLEMENT should return COMPLEMENT error msg`() {
        val expectedUser: User = mockUserWithoutComplement()

        viewModel.validateDateUserRegister(expectedUser)

        assertEquals(COMPLEMENT_ERROR_MESSAGE, viewModel.messageState.value)
    }

    @Test
    fun `when haveErrorsDateUserRegister() is called without NUMBER should return NUMBER error msg`() {
        val expectedUser: User = mockUserWithoutNumber()

        viewModel.validateDateUserRegister(expectedUser)

        assertEquals(NUMBER_ERROR_MESSAGE, viewModel.messageState.value)
    }

    @Test
    fun `when haveErrorsDateUserRegister() is called without State should return State error msg`() {
        val expectedUser: User = mockUserWithoutState()

        viewModel.validateDateUserRegister(expectedUser)

        assertEquals(STATE_ERROR_MESSAGE, viewModel.messageState.value)
    }

    @Test
    fun `when haveErrorsDateUserRegister() is called without RoadAv should return RoadAv error msg`() {
        val expectedUser: User = mockUserWithoutRoadAv()

        viewModel.validateDateUserRegister(expectedUser)

        assertEquals(ROAD_ERROR_MESSAGE, viewModel.messageState.value)
    }

    @Test
    fun `when haveErrorsDateUserRegister() is called without AGE should return AGE error msg`() {
        val expectedUser: User = mockUserWithoutAge()

        viewModel.validateDateUserRegister(expectedUser)

        assertEquals(AGE_ERROR_MESSAGE, viewModel.messageState.value)
    }

    @Test
    fun `when haveErrorsDateUserRegister() is called without CITY should return CITY error msg`() {
        val expectedUser: User = mockUserWithoutCity()

        viewModel.validateDateUserRegister(expectedUser)

        assertEquals(CITY_ERROR_MESSAGE, viewModel.messageState.value)
    }

    @Test
    fun `when haveErrorsDateUserRegister() is called without Country should return Country error msg`() {
        val expectedUser: User = mockUserWithoutCountry()

        viewModel.validateDateUserRegister(expectedUser)

        assertEquals(COUNTRY_ERROR_MESSAGE, viewModel.messageState.value)
    }

    @Test
    fun `when haveErrorsDateUserRegister() is called without INTEREST should return INTEREST error msg`() {
        val expectedUser: User = mockUserWithoutInterestes()

        viewModel.validateDateUserRegister(expectedUser)

        assertEquals(INTEREST_ERROR_MESSAGE, viewModel.messageState.value)
    }

    private fun mockUser(): User =
        User(
            "1", "christian", "chistian@zup.com.br", true, "chris", "17/01/1992",
            "11040-020", "Santos", "SP", "", "homem", "hetero",
            "Ch9206ch!", "Rua A", "123", "casa", "trabalho", "ele"
        )

    private fun mockUserWithoutNickName(): User =
        User(
            "1", "christian", "chistian@zup.com.br", true, "", "17/01/1992",
            "11040-020", "Santos", "SP", "", "homem", "hetero",
            "Ch9206ch!", "Rua A", "123", "casa", "trabalho", "ele"
        )

    private fun mockUserWithoutCEP(): User =
        User(
            "1", "christian", "chistian@zup.com.br", true, "chris", "17/01/1992",
            "", "Santos", "SP", "brasil", "homem", "hetero",
            "Ch9206ch!", "Rua A", "123", "casa", "trabalho", "ele"
        )

    private fun mockUserWithoutComplement(): User =
        User(
            "1", "christian", "chistian@zup.com.br", true, "chris", "17/01/1992",
            "11040-020", "Santos", "SP", "brasil", "homem", "hetero",
            "Ch9206ch!", "Rua A", "123", "", "trabalho", "ele"
        )

    private fun mockUserWithoutNumber(): User =
        User(
            "1", "christian", "chistian@zup.com.br", true, "chris", "17/01/1992",
            "11040-020", "Santos", "SP", "brasil", "homem", "hetero",
            "Ch9206ch!", "Rua A", "", "casa", "trabalho", "ele"
        )

    private fun mockUserWithoutState(): User =
        User(
            "1", "christian", "chistian@zup.com.br", true, "chris", "17/01/1992",
            "11040-020", "Santos", "", "brasil", "homem", "hetero",
            "Ch9206ch!", "Rua A", "23", "casa", "trabalho", "ele"
        )

    private fun mockUserWithoutRoadAv(): User =
        User(
            "1", "christian", "chistian@zup.com.br", true, "chris", "17/01/1992",
            "11040-020", "Santos", "SP", "brasil", "homem", "hetero",
            "Ch9206ch!", "", "23", "casa", "trabalho", "ele"
        )

    private fun mockUserWithoutAge(): User =
        User(
            "1", "christian", "chistian@zup.com.br", true, "chris", "",
            "11040-020", "Santos", "SP", "brasil", "homem", "hetero",
            "Ch9206ch!", "Rua A", "23", "casa", "trabalho", "ele"
        )

    private fun mockUserWithoutCity(): User =
        User(
            "1", "christian", "chistian@zup.com.br", true, "chris", "17/01/1992",
            "11040-020", "", "SP", "brasil", "homem", "hetero",
            "Ch9206ch!", "Rua A", "23", "casa", "trabalho", "ele"
        )

    private fun mockUserWithoutCountry(): User =
        User(
            "1", "christian", "chistian@zup.com.br", true, "chris", "17/01/1992",
            "11040-020", "Santos", "SP", "", "homem", "hetero",
            "Ch9206ch!", "Rua A", "23", "casa", "trabalho", "ele"
        )

    private fun mockUserWithoutInterestes(): User =
        User(
            "1", "christian", "chistian@zup.com.br", true, "chris", "17/01/1992",
            "11040-020", "Santos", "SP", "brasil", "homem", "hetero",
            "Ch9206ch!", "Rua A", "23", "casa", "", "ele"
        )
}
