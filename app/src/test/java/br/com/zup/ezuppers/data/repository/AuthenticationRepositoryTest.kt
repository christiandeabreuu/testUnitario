package br.com.zup.ezuppers.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.data.datasource.AuthenticationDataSource
import br.com.zup.ezuppers.domain.usecase.GetCepUseCase
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
internal class AuthenticationRepositoryTest{

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var dataSource: AuthenticationDataSource

    private lateinit var authRepository : AuthenticationRepository

    @Before
    fun onBefore() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        authRepository = AuthenticationRepository(dataSource)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `fun registerUser()should to return success`(){
        val expectedEmail = "chris@gmai.com"
        val expectedPassword = "123456"
        val expectedTaskAuthResult = mockk<Task<AuthResult>>()

        every { dataSource.registerUser(expectedEmail, expectedPassword) } returns expectedTaskAuthResult
        val response = authRepository.registerUser(expectedEmail, expectedPassword)

        assert(expectedTaskAuthResult == response)
    }

//    fun registerUser(email: String, password: String): Task<AuthResult> =
//        authenticationDataSource.registerUser(email, password)
}