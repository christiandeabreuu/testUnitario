package br.com.zup.ezuppers.datasource

import br.com.zup.ezuppers.data.datasource.UserDataSource
import br.com.zup.ezuppers.data.datasource.local.dao.UserDAO
import br.com.zup.ezuppers.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.core.Path
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
internal class UserDataSourceTest {

    @RelaxedMockK
    private lateinit var auth: FirebaseAuth

    @RelaxedMockK
    private lateinit var userDao: UserDAO

    @RelaxedMockK
    private lateinit var database: DatabaseReference

    private lateinit var dataSource: UserDataSource

    @Before
    fun onBefore() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        dataSource = UserDataSource(userDao, auth, database)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `when databaseReference()`() {
        val mockk = mockk<DatabaseReference>()
        val expectedPath = mockk<Path>()
        every { database.database.getReference("a") } returns mockk
        val result = dataSource.databaseReference()

//        assert(mockk == result)
        verify { database.database.getReference("a") }
    }

    @Test
    fun `getRegisterInformation() `() {
        val mockk = mockk<DatabaseReference>()
        val mockkQuery = mockk<Query>()
        val expectedPath = mockk<Path>()
        every { database.database.reference.orderByKey() } returns mockkQuery
        val result = dataSource.getRegisterInformation()

//        assert(mockkQuery == result)
        verify {
            database.database.reference.orderByKey()
        }
    }

    @Test
    fun `when getCurrentUser() should to return success`() {
        val user = mockk<FirebaseUser>()

        every { auth.currentUser } returns user

        val result = dataSource.getCurrentUser()

        assert(result == user)
    }

    @Test
    fun `when getRegisterLoginInformation() should to return success`() {
        val user = mockk<User>()

        every { userDao.getRegisterLoginInformation() } returns user

        val result = dataSource.getRegisterLoginInformation()

        assert(result == user)
        verify { userDao.getRegisterLoginInformation() }
    }

    @Test
    fun `when insertAllRegisterLogin() should to return success`() {
        val user = mockk<User>()

        every { userDao.insertAllRegisterInformation(user) } just runs

        val result = dataSource.insertAllRegisterLogin(user)

        assert(result == Unit)
        verify { userDao.insertAllRegisterInformation(user) }
    }

    @Test
    fun `when getAllRegisterInformationOffline() should to return success`() {
        val userId = "12345"
        val listUser = mockk<List<User>>()

        every { userDao.getAllRegisterInformationOfflineLocal(userId) } returns listUser

        val result = dataSource.getAllRegisterInformationOffline(userId)

        assert(result == listUser)
        verify { userDao.getAllRegisterInformationOfflineLocal(userId) }
    }

    @Test
    fun `when updateInformationUser() should to return success`() {
        val user = mockk<User>()

        every { userDao.updateInformationUser(user) } just runs

        val result = dataSource.updateInformationUser(user)

        assert(result == Unit)
        verify { userDao.updateInformationUser(user) }
    }

    @Test
    fun `when getCurrentUserId() should to return success`() {
        val user = mockk<FirebaseUser>()
        val uid = "uid"

        every { auth.currentUser } returns user
        every { user.uid  } returns uid

        val result = dataSource.getCurrentUserId()

        assert(result == uid)
    }

}