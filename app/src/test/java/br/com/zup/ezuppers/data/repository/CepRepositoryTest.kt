package br.com.zup.ezuppers.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.data.datasource.remote.API
import br.com.zup.ezuppers.data.datasource.remote.RetrofitService
import br.com.zup.ezuppers.data.datasource.remote.RetrofitService.Companion.CEP_URL
import com.google.android.gms.common.api.Api
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@ExperimentalCoroutinesApi
internal class CepRepositoryTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var retrofitService: RetrofitService

    lateinit var api : API

    private lateinit var cepRepository: CepRepository

    @Before
    fun onBefore() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        cepRepository = CepRepository()
        api = Retrofit.Builder().baseUrl(CEP_URL)
            .addConverterFactory(GsonConverterFactory.create()).build().create(API::class.java)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `getCep() return Success`() = runTest {
        val expectedCep = "11040020"
        val expectedUrl = "abcde"

        val response = api.getAddress(expectedUrl)

//        coEvery { retrofitService.get() }
//        val response = cepRepository.getCep(expectedCep)
//
//        coVerify {  retrofitService. }
    }

//    suspend fun getCep(cep: String): CepResult {
//        return RetrofitService.apiService.getAddress(RetrofitService.CEP_URL.replace("{cep}", cep))
//    }
}



