package br.com.zup.ezuppers.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.data.datasource.AuthenticationDataSource
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
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
    fun `fun registerUser() should to return success`(){
        val expectedEmail = "chris@gmail.com"
        val expectedPassword = "123456"
        val expectedTaskAuthResult = mockk<Task<AuthResult>>()

        every { dataSource.registerUser(expectedEmail, expectedPassword) } returns expectedTaskAuthResult
        val response = authRepository.registerUser(expectedEmail, expectedPassword)

        assert(expectedTaskAuthResult == response)
        verify (exactly = 1){ dataSource.registerUser(expectedEmail, expectedPassword)}
    }

    @Test
    fun `fun updateUserProfile() should to return success`(){
        val expectedEmail = "chris@gmail.com"
        val expectedTaskVoid = mockk<Task<Void>>()

        every { dataSource.updateUserProfile(expectedEmail) } returns expectedTaskVoid
        val response = authRepository.updateUserProfile(expectedEmail)

        assert(expectedTaskVoid == response)
        verify (exactly = 1){ dataSource.updateUserProfile(expectedEmail) }

    }

    @Test
    fun `fun logoutUser() should to return success`(){
        every { dataSource.logOut() } just runs

         authRepository.logOutUser()

        verify (exactly = 1){ dataSource.logOut() }
    }

    @Test
    fun `fun loginUser() should to return success`(){
        val expectedEmail = "chris@gmail.com"
        val expectedPassword = "123456"
        val expectedTaskAuthResult = mockk<Task<AuthResult>>()

        every { dataSource.loginUser(expectedEmail, expectedPassword) } returns expectedTaskAuthResult
        val response = authRepository.loginUser(expectedEmail, expectedPassword)

        assert(expectedTaskAuthResult == response)
        verify (exactly = 1){ dataSource.loginUser(expectedEmail, expectedPassword)}
    }

    @Test
    fun `fun getUserName() should to return success`(){
        val expectedResult = "posso usar essa variavel tbm"
        every { dataSource.getUsersName() } returns "Christian"

       val response =  authRepository.getUsersName()

        assert("Christian" == response)
        verify (exactly = 1){ dataSource.getUsersName() }
    }

    @Test
    fun `fun getCurrentUser() should to return success`(){
        val expectedUser = mockk<FirebaseUser>()
        every { dataSource.getCurrentUser() } returns expectedUser

        val response =  authRepository.getCurrentUser()

        assert(expectedUser == response)
        verify (exactly = 1){ dataSource.getCurrentUser() }
    }
}