package br.com.zup.ezuppers.ui.zuppers.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.data.model.CitiesResult
import br.com.zup.ezuppers.data.model.State
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.domain.usecase.GetCitiesUseCase
import br.com.zup.ezuppers.domain.usecase.GetUfUseCase
import br.com.zup.ezuppers.domain.usecase.GetZuppersQuantityUseCase
import br.com.zup.ezuppers.domain.usecase.GetZuppersUseCase
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
internal class ZuppersViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private var getUfUseCase: GetUfUseCase = mockk(relaxed = true)

    @RelaxedMockK
    private var getCitiesUseCase: GetCitiesUseCase = mockk(relaxed = true)

    @RelaxedMockK
    private var getZuppersUseCase: GetZuppersUseCase = mockk(relaxed = true)

    @RelaxedMockK
    private var getZuppersQuantityUseCase: GetZuppersQuantityUseCase = mockk(relaxed = true)

    private lateinit var viewModel: ZuppersViewModel

    @Before
    fun onBefore() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = ZuppersViewModel(
            getUfUseCase,
            getCitiesUseCase,
            getZuppersUseCase,
            getZuppersQuantityUseCase
        )
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `When call fun getQuantityCity() should to call the same fun in useCase and to return zuppers quantity `() =
        runTest {
            val expectedCity = "Ubatuba"

            coEvery { getZuppersQuantityUseCase.execute("Ubatuba") } returns 7
            viewModel.getQuantityZuppers(expectedCity)

            assertEquals(getZuppersQuantityUseCase.execute("Ubatuba"), 7)
            coVerify { getZuppersQuantityUseCase.execute(expectedCity) }
        }


    private fun mockkListUsers() = listOf(
        User("1"),
        User("2"),
        User("3")
    )

    @Test
    fun `When call fun getListZuppers() should to call the same fun in useCase and to return zuppers quantity `() =
        runTest {
            val expectedCity = "Ubatuba"

            coEvery { getZuppersUseCase.execute("Ubatuba") } returns mockkListUsers()
            viewModel.getListZuppers(expectedCity)

            assertEquals(getZuppersUseCase.execute("Ubatuba"), mockkListUsers())
            coVerify { getZuppersUseCase.execute(expectedCity) }
        }

    @Test
    fun `When call fun getListZuppers() should  to return msg Exception `() =
        runTest {
            val expectedCity = "Ubatuba"

            coEvery { getZuppersUseCase.execute("Ubatuba") } throws NullPointerException()
            viewModel.getListZuppers(expectedCity)
            val value = viewModel.errorListZuppers.value

            assertTrue(value is NullPointerException)

        }

    @Test
    fun `When call fun getStates() should to call the same fun in useCase `() = runTest {
        val nameStates = arrayListOf("Sao Paulo", "Rio de Janeiro", "Minas Gerais")

        coEvery { getUfUseCase.execute() } returns mockkListStates()
        viewModel.getStates()

        coVerify { getUfUseCase.execute() }
        assertEquals(nameStates, viewModel.ufResponse.value) // falta verificar valor
    }

    private fun mockkListStates() = arrayListOf(
        State(0, "Sao Paulo", "SP"),
        State(1, "Rio de Janeiro", "RJ"),
        State(2, "Minas Gerais", "MG"),
    )


    @Test
    fun `When call fun getStates() should  to return msg Exception `() = runTest {

        coEvery { getUfUseCase.execute() } throws NullPointerException()
        viewModel.getStates()
        val value = viewModel.errorListStates.value

        assert(value is NullPointerException)
    }

    @Test
    fun `When call fun getQuantityZuppers() should to call the same fun in useCase `() = runTest {
        val expectedCity = "Ubatuba"

        coEvery { getZuppersQuantityUseCase.execute("Ubatuba") } returns 3
        val value = viewModel.getQuantityZuppers(expectedCity)

        coVerify { getZuppersQuantityUseCase.execute(expectedCity) }
        assertEquals(value, 3)
    }

    @Test
    fun `When call fun getCities() should to call the same fun in useCase `() = runTest {
        val expectedUfid = 123
        val expectedCitiesResult = mockk<CitiesResult>()

        coEvery { getCitiesUseCase.execute(expectedUfid) } returns expectedCitiesResult
         viewModel.getCities("SP")

        coVerify { getCitiesUseCase.execute(any()) }

    }

    @Test
    fun `When call fun getCities() should to return an Exception `() = runTest {
        val expectedUfid = 123

        coEvery { getCitiesUseCase.execute(expectedUfid) } throws NullPointerException()
        viewModel.getCities("SP")
        val value = viewModel.errorListCities.value

        assertTrue(value is NullPointerException)
    }
}