package br.com.zup.ezuppers.ui.editregister.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.data.model.CepResult
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.domain.usecase.GetCepUseCase
import br.com.zup.ezuppers.domain.usecase.UserUseCase
import br.com.zup.ezuppers.utilities.*
import br.com.zup.ezuppers.viewstate.ViewState
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
import org.junit.Assert.assertTrue
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
    private var getCepUseCase: GetCepUseCase = mockk(relaxed = true)

    private lateinit var viewModel: EditRegisterViewModel

    @Before
    fun onBefore() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel =
            EditRegisterViewModel(application = Application(), editUserUseCase, getCepUseCase)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `when getCEP() is called should return CepResult`() = runTest {
        val expectedCep = "11040020"

        coEvery { getCepUseCase.execute(expectedCep) } returns CepResult(
            1,
            "11040020",
            "a",
            "SP",
            "sp",
            "ap 23",
            "Santos"
        )

        viewModel.getCep(expectedCep)
        val response = viewModel.cepResult.value

        assertTrue(response is ViewState.Success)
        assertEquals("11040020", (response as ViewState.Success).data.cep)
        coVerify(exactly = 1) { getCepUseCase.execute("11040020") }
    }

    @Test
    fun `when getCEP() is called should return Error`() = runTest {
        val expectedCep = "000"
        coEvery { getCepUseCase.execute(expectedCep) } throws NullPointerException()

        viewModel.getCep(expectedCep)
        val response = viewModel.cepResult.value

        assertTrue(response is ViewState.Error)
    }

    @Test
    fun `when haveErrorsDateUserRegister() is called without NICKNAME should return NICKNAME error msg`() {
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
    fun `when haveErrorsDateUserRegister() is called without NUMBER should be to return NUMBER error msg`() {
        val expectedUser: User = mockUserWithoutNumber()

        viewModel.validateDateUserRegister(expectedUser)

        assertEquals(NUMBER_ERROR_MESSAGE, viewModel.messageState.value)
    }

    @Test
    fun `when haveErrorsDateUserRegister is called without State should return State error msg`() {
        val expectedUser: User = mockUserWithoutState()

        viewModel.validateDateUserRegister(expectedUser)

        assertEquals(STATE_ERROR_MESSAGE, viewModel.messageState.value)
    }

    @Test
    fun `when haveErrorsDateUserRegister is called without RoadAv should return RoadAv error msg`() {
        val expectedUser: User = mockUserWithoutRoadAv()

        viewModel.validateDateUserRegister(expectedUser)

        assertEquals(ROAD_ERROR_MESSAGE, viewModel.messageState.value)
    }

    @Test
    fun `when haveErrorsDateUserRegister() without AGE should return AGE error msg`() {
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

    @Test
    fun `when getCurrentUserRegister() is callled should to call the same fun in useCase`() =
        runTest {
            coEvery { editUserUseCase.getCurrentUser() } returns mockk(relaxed = true)

            viewModel.getCurrentUserRegister()

            coVerify { editUserUseCase.getCurrentUser() }
        }

    @Test
    fun `when getCurrentUserRegister() is called should to call the same fun in useCase 2 and return FirebaseUser`() =
        runTest {
            val expectedFirebaseUser = mockk<FirebaseUser>()
            coEvery { editUserUseCase.getCurrentUser() } returns expectedFirebaseUser

            val response = viewModel.getCurrentUserRegister()

            assert(expectedFirebaseUser == response)
            coVerify(exactly = 1) { editUserUseCase.getCurrentUser() }
        }

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