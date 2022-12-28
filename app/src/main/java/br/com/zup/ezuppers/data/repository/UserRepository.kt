package br.com.zup.ezuppers.data.repository

import br.com.zup.ezuppers.data.datasource.UserDataSource
import br.com.zup.ezuppers.data.datasource.local.dao.UserDAO
import br.com.zup.ezuppers.domain.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Query

class UserRepository(private val userDataSource: UserDataSource) {

//    private val userDataSource: UserDataSource by lazy {
//        UserDataSource(userDao)
//    }

    fun databaseReference() = userDataSource.databaseReference()

    fun getRegisterInformation(): Query = userDataSource.getRegisterInformation()

    fun getCurrentUser() = userDataSource.getCurrentUser()

    fun getRegisterLoginInformation(): User =
        userDataSource.getRegisterLoginInformation()

    fun getAllRegisterInformationOffline(userId: String): List<User> =
        userDataSource.getAllRegisterInformationOffline(userId)

    fun insertAllRegisterLogin(user: User) =
        userDataSource.insertAllRegisterLogin(user)

    fun updateInformationUser(user: User) = userDataSource.updateInformationUser(user)

    fun getAuthor(authorId: String): Task<DataSnapshot> = userDataSource.getAuthor(authorId)

    fun getCurrentUserId() = userDataSource.getCurrentUserId()

}