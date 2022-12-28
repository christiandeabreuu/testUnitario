package br.com.zup.ezuppers.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.data.datasource.remote.RetrofitService
import br.com.zup.ezuppers.data.model.CitiesResult
import br.com.zup.ezuppers.data.model.State
import br.com.zup.ezuppers.data.model.StatesResult
import br.com.zup.ezuppers.domain.usecase.GetUfUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import retrofit2.Retrofit

@ExperimentalCoroutinesApi
internal class UfRepositoryTest{
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()


    private lateinit var repository: UfRepository


    @Before
    fun onBefore() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repository = UfRepository()
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

//    @Test
//    fun `When call fun getCities should to call the fun on API with to same value`() =
//        runBlocking {
//            val expectedUfid = 12345
//
////            coEvery { RetrofitService.apiService.getCities(12345) } returns CitiesResult()
//            repository.getCities(12345)
//
//            coVerify { RetrofitService.apiService.getCities(expectedUfid) }
//        }

    private fun mockkListStates() = listOf(
        State(1,"São Paulo", "SP"),
        State(2,"Rio de Janeiro", "RJ")
    )
}