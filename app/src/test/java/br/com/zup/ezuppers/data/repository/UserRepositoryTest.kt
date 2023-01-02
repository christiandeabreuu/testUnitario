package br.com.zup.ezuppers.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.data.datasource.UserDataSource
import br.com.zup.ezuppers.domain.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
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
internal class UserRepositoryTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var dataSource: UserDataSource

    private lateinit var repository: UserRepository

    @Before
    fun onBefore() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repository = UserRepository(dataSource)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `when databaseReference() is called should call function on dataSource`() {
        val expectedDatabaseReference = mockk<DatabaseReference>()
        every { dataSource.databaseReference() } returns expectedDatabaseReference

        val response = repository.databaseReference()

        assert(expectedDatabaseReference == response)
        verify(exactly = 1) { dataSource.databaseReference() }
    }

    @Test
    fun `when getRegisterInformation() is called should call function on dataSource and return Query`() {
        val mockkQuery = mockk<Query>()

        every { dataSource.getRegisterInformation() } returns mockkQuery

        val response = repository.getRegisterInformation()

        assert(mockkQuery == response)
        verify(exactly = 1) {
            dataSource.getRegisterInformation()
        }
    }

    @Test
    fun `when getCurrentUser() is called should call function on dataSource`() {
        val mockkUser = mockk<FirebaseUser>()

        every { dataSource.getCurrentUser() } returns mockkUser

        val response = repository.getCurrentUser()

        assert(mockkUser == response)
        verify(exactly = 1) {
            dataSource.getCurrentUser()
        }
    }

    @Test
    fun `when getRegisterLoginInformation() is called should call function on dataSource and return user`() {
        val mockkUser = mockk<User>()

        every { dataSource.getRegisterLoginInformation() } returns mockkUser

        val response = repository.getRegisterLoginInformation()

        assert(mockkUser == response)
        verify(exactly = 1) {
            dataSource.getRegisterLoginInformation()
        }
    }

    @Test
    fun `when getAllRegisterInformationOffline() is called should call function on dataSource and return listUser`() {
        val userId = "12345"
        val mockkListUser = mockk<List<User>>()

        every { dataSource.getAllRegisterInformationOffline(userId) } returns mockkListUser

        val response = repository.getAllRegisterInformationOffline("12345")

        assert(mockkListUser == response)
        verify(exactly = 1) {
            dataSource.getAllRegisterInformationOffline(userId)
        }
    }

    @Test
    fun `when insertAllRegisterLogin() is called should call function on dataSource`() {

        val user = mockk<User>()

        every { dataSource.insertAllRegisterLogin(user) } just runs

        val response = repository.insertAllRegisterLogin(user)

        assert(Unit == response)
        verify(exactly = 1) {
            dataSource.insertAllRegisterLogin(user)
        }
    }


    @Test
    fun `when updateInformationUser() is called should call function on dataSource`() {
        val user = mockk<User>()
        every { dataSource.updateInformationUser(user) } just runs

        val response = repository.updateInformationUser(user)

        assert(Unit == response)
        verify(exactly = 1) {
            dataSource.updateInformationUser(user)
        }
    }

    @Test
    fun `when getAuthor() is called should call function on dataSource and return TaskDataSnapshot`() {
        val authorId = "Chris"
        val expectedTaskDataSnapshot = mockk<Task<DataSnapshot>>()
        every { dataSource.getAuthor(authorId) } returns expectedTaskDataSnapshot

        val response = repository.getAuthor("Chris")

        assert(expectedTaskDataSnapshot == response)
        verify(exactly = 1) {
            dataSource.getAuthor(authorId)
        }
    }

    @Test
    fun `when getCurrentUserId() is called should call function on dataSource`() {
        val expectedString = "Chris"
        every { dataSource.getCurrentUserId() } returns expectedString

        val response = repository.getCurrentUserId()

        assert(expectedString == response)
        verify(exactly = 1) {
            dataSource.getCurrentUserId()
        }
    }

}