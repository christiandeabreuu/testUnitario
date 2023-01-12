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
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule


@ExperimentalCoroutinesApi
internal class UserUseCaseTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var repository: UserRepository

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
    fun `when databaseReference() is called should to call the same fun on repository`() =
        runTest {
            coEvery { repository.databaseReference() } returns mockk(relaxed = true)

            userUseCase.databaseReference()

            coVerify (exactly = 1){ repository.databaseReference() }
        }

    @Test
    fun `when getRegisterInformation() is called should to call the same fun on repository`() =
        runTest {
            coEvery { repository.getRegisterInformation() } returns mockk(relaxed = true)

            userUseCase.getRegisterInformation()

            coVerify (exactly = 1){ repository.getRegisterInformation() }
        }

    @Test
    fun `when getCurrentUser() is called should to call the same fun on repository`() {
        val expectedFirebaseUser = mockk<FirebaseUser>()

        every { repository.getCurrentUser() } returns expectedFirebaseUser

        val result = userUseCase.getCurrentUser()

        assert(result is FirebaseUser)
        verify (exactly = 1){ repository.getCurrentUser() }
    }

    @Test
    fun `when getRegisterLoginInformation() is called should return ViewState success`() {
        val expectedUser = mockk<User>()
        every { repository.getRegisterLoginInformation() } returns expectedUser

        val result = userUseCase.getRegisterLoginInformation()

        assert(result is ViewState.Success)
    }

    @Test
    fun `when getRegisterLoginInformation() is called should return ViewState error`() {
        every { repository.getRegisterLoginInformation() } throws NullPointerException()

        val result = userUseCase.getRegisterLoginInformation()

        assert(result is ViewState.Error)
    }

    @Test
    fun `when insertAllRegisterLogin() is called should return ViewState success`() {
        val expectedUser = mockk<User>()
        every { repository.insertAllRegisterLogin(expectedUser) } just runs

        val result = userUseCase.insertAllRegisterLogin(expectedUser)

        assert(result is ViewState.Success)
    }

    @Test
    fun `when insertAllRegisterLogin() is called should return ViewState error`() {
        every { repository.getRegisterLoginInformation() } throws NullPointerException()

        val result = userUseCase.getRegisterLoginInformation()

        assert(result is ViewState.Error)
    }

    @Test
    fun `when getAllRegisterInformationOffline() is called should return ViewState success`() {
        val expectedId = "12345 "
        val expectedResponse = mockk<List<User>>()
        every { repository.getAllRegisterInformationOffline(expectedId) } returns expectedResponse

        val result = userUseCase.getAllRegisterInformationOffline(expectedId)

        assert(result is ViewState.Success)
    }

    @Test
    fun `when getAllRegisterInformationOffline() is called should return ViewState error`() {
        val expectedId = "12345"
        every { repository.getAllRegisterInformationOffline(expectedId) } throws NullPointerException()

        val result = userUseCase.getAllRegisterInformationOffline(expectedId)

        assert(result is ViewState.Error)
    }


    @Test
    fun `when updateInformationUser() is called should return ViewState success`() {
        val expectedUser = mockk<User>()
        every { repository.updateInformationUser(expectedUser) } just runs

        val result = userUseCase.updateInformationUser(expectedUser)

        assert(result is ViewState.Success)
    }


    @Test
    fun `when updateInformationUser() is called should return ViewState error`() {
        val expectedUser = mockk<User>()
        every { repository.updateInformationUser(expectedUser) } throws NullPointerException()

        val result = userUseCase.updateInformationUser(expectedUser)

        assert(result is ViewState.Error)
    }

    @Test
    fun `when getCurrentUserID() is called should call the same fun on repository`() {
        val expectedResponse = "String?"
        every { repository.getCurrentUserId() } returns expectedResponse

        val result = userUseCase.getCurrentUserId()

        assert(result is String)
        verify(exactly = 1) { repository.getCurrentUserId() }
    }

    @Test
    fun `when getAuthor() is called should call the same fun on repository`() {
        val expectedAuthor = "String?"
        val expectedTaskDataSnapshot = mockk<Task<DataSnapshot>>()
        every { repository.getAuthor(expectedAuthor) } returns expectedTaskDataSnapshot

        val result = userUseCase.getAuthor(expectedAuthor)

        assert(result == expectedTaskDataSnapshot)
        verify(exactly = 1) { repository.getAuthor(expectedAuthor) }
    }
}
