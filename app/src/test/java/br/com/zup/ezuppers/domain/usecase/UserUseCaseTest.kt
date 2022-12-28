package br.com.zup.ezuppers.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.data.repository.UserRepository
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.viewstate.ViewState
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.rules.TestRule

//@RunWith(AndroidJUnit4::class)
//@SmallTest
@ExperimentalCoroutinesApi
internal class UserUseCaseTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var repository: UserRepository

//    @RelaxedMockK
//    private lateinit var userDataBase : UserDataBase

//    @RelaxedMockK
//    private lateinit var application: Application

//    @RelaxedMockK
//    private lateinit var userDao : UserDAO

    private lateinit var userUseCase: UserUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        userUseCase = UserUseCase(repository)
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
    fun `fun databaseReference should to call the same fun on repository `() =
        runTest {

            coEvery { repository.databaseReference() } returns mockk(relaxed = true)

            userUseCase.databaseReference()

            coVerify { repository.databaseReference() }
        }

    @Test
    fun `fun getRegisterInformation() should to call the same fun on repository `() =
        runTest {

            coEvery { repository.getRegisterInformation() } returns mockk(relaxed = true)

            userUseCase.getRegisterInformation()

            coVerify { repository.getRegisterInformation() }
        }

    @Test
    fun `when getCurrentUser() return success`() {
        val expectedUser = mockk<FirebaseUser>()

        every { repository.getCurrentUser() }  returns expectedUser

        val response = userUseCase.getCurrentUser()

        assert(response is FirebaseUser)
        verify { repository.getCurrentUser()  }
    }

    @Test
    fun `when getRegisterLoginInformation() return success`() {
        val expectedUser = mockk<User>()

        every { repository.getRegisterLoginInformation() }  returns expectedUser
        val response = userUseCase.updateInformationUser(expectedUser)

        assert(response is ViewState.Success)
    }

    @Test
    fun `when getRegisterLoginInformation() return error`() {

        every { repository.getRegisterLoginInformation() }  throws NullPointerException()
        val response = userUseCase.getRegisterLoginInformation()

        assert(response is ViewState.Error)
    }

    @Test
    fun `when insertAllRegisterLogin() return success`() {
        val expectedUser = mockk<User>()

        every { repository.insertAllRegisterLogin(expectedUser) }  just runs
        val response = userUseCase.insertAllRegisterLogin(expectedUser)

        assert(response is ViewState.Success)
    }

    @Test
    fun `when insertAllRegisterLogin() return error`() {

        every { repository.getRegisterLoginInformation() }  throws NullPointerException()
        val response = userUseCase.getRegisterLoginInformation()

        assert(response is ViewState.Error)
    }

    @Test
    fun `when getAllRegisterInformationOffline() return success`() {
        val expectedId = "12345 "
        val expectedResponse = mockk<List<User>>()

        every { repository.getAllRegisterInformationOffline(expectedId) }  returns expectedResponse
        val response = userUseCase.getAllRegisterInformationOffline(expectedId)

        assert(response is ViewState.Success)
    }

    @Test
    fun `when getAllRegisterInformationOffline() return error`() {
        val expectedId = "12345"

        every { repository.getAllRegisterInformationOffline(expectedId) } throws NullPointerException()
        val response = userUseCase.getAllRegisterInformationOffline(expectedId)

        assert(response is ViewState.Error)
    }


    @Test
    fun `when updateInformationUser() return success`() {
        val expectedUser = mockk<User>()

         every { repository.updateInformationUser(expectedUser) }  just runs
        val response = userUseCase.updateInformationUser(expectedUser)

          assert(response is ViewState.Success)
    }



    @Test
    fun `when updateInformationUser() return error`() {
        val expectedUser = mockk<User>()
        every { repository.updateInformationUser(expectedUser) } throws NullPointerException()
        val response = userUseCase.updateInformationUser(expectedUser)

        assert(response is ViewState.Error)
    }

    @Test
    fun `when getCurrentUserID() return success`() {
        val expectedResponse = "String?"


        every { repository.getCurrentUserId() }  returns expectedResponse

        val response = userUseCase.getCurrentUserId()

        assert(response is String)
        verify (exactly = 1){ repository.getCurrentUserId()  }
    }

    @Test
    fun `when getAuthor() return success`() {
        val expectedAuthor = "String?"
        val expectedReturn = mockk<Task<DataSnapshot>>()

        every { repository.getAuthor(expectedAuthor) }  returns expectedReturn

        val response = userUseCase.getAuthor(expectedAuthor)

        assert(response == expectedReturn)
       verify (exactly = 1){ repository.getAuthor(expectedAuthor)  }
    }

}
