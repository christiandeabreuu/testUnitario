package br.com.zup.ezuppers.datasource

import br.com.zup.ezuppers.data.datasource.AuthenticationDataSource
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class AuthenticationDataSourceTest {

    @RelaxedMockK
    private lateinit var auth: FirebaseAuth

    private lateinit var dataSource: AuthenticationDataSource

    @Before
    fun onBefore() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        dataSource = AuthenticationDataSource(auth)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `fun registerUser() should to return success`() {
        val expectedEmail = "chris@gmail.com"
        val expectedPassword = "123456"
        val expectedTaskAuthResult = mockk<Task<AuthResult>>()

        every {
            auth.createUserWithEmailAndPassword(
                expectedEmail,
                expectedPassword
            )
        } returns expectedTaskAuthResult
        val response = dataSource.registerUser(expectedEmail, expectedPassword)

        assert(expectedTaskAuthResult == response)
        verify(exactly = 1) { auth.createUserWithEmailAndPassword(expectedEmail, expectedPassword) }
    }

    @Test
    fun `fun updateUserProfile() should to return success`() {
        val expectedName = "Christian"
        val user = mockk<FirebaseUser>()
        val expectedProfile1 = UserProfileChangeRequest.Builder().setDisplayName("Christian").build()
        val expectedProfile = mockk<UserProfileChangeRequest>()
        val expectedTaskVoid = mockk<Task<Void>>()

        every { auth.currentUser } returns user
        every { user.updateProfile(expectedProfile) } returns expectedTaskVoid
//        every { auth.currentUser?.updateProfile(expectedProfile) } returns expectedTaskVoid
        val response = dataSource.updateUserProfile(expectedName)

//        assert(response == expectedTaskVoid)
//       assertEquals(expectedTaskVoid , response)
        verify { auth.currentUser?.updateProfile(expectedProfile) }
    }

    @Test
    fun `fun logoutUser() should to return success`() {
        every { dataSource.logOut() } just runs

        auth.signOut()

        verify(exactly = 1) { dataSource.logOut() }
    }

    @Test
    fun `fun loginUser() should to return success`() {
        val expectedEmail = "chris@gmail.com"
        val expectedPassword = "123456"
        val expectedTaskAuthResult = mockk<Task<AuthResult>>()

        every {
            auth.signInWithEmailAndPassword(
                expectedEmail,
                expectedPassword
            )
        } returns expectedTaskAuthResult
        val response = dataSource.loginUser(expectedEmail, expectedPassword)

        assert(expectedTaskAuthResult == response)
        verify(exactly = 1) {
            auth.signInWithEmailAndPassword(
                expectedEmail,
                expectedPassword
            )
        }
    }

    @Test
    fun `when call getUsersName() should to return success `() {
        val user = mockk<FirebaseUser>()
        val expectedName = "Chris"

        every { user.displayName } returns expectedName
        every { auth.currentUser } returns user

        val result = dataSource.getUsersName()

        assert(result == expectedName)
    }

    @Test
    fun `when call getCurrentUser() should to return success `() {
        val user = mockk<FirebaseUser>()

        every { auth.currentUser } returns user

        val result = dataSource.getCurrentUser()

        assert(result == user)
        coVerify(exactly = 1) { auth.currentUser }
    }

}