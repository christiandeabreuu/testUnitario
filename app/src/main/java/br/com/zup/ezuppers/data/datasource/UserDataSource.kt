package br.com.zup.ezuppers.data.datasource

import br.com.zup.ezuppers.data.datasource.local.dao.UserDAO
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.utilities.REGISTER_INFORMATION_USER
import br.com.zup.ezuppers.utilities.USER
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query

class UserDataSource(private val userDao: UserDAO, private val auth: FirebaseAuth, private val database: DatabaseReference) {

    private val reference =
        database.database.getReference("$REGISTER_INFORMATION_USER/${auth.currentUser?.uid}/$USER")

    fun databaseReference() = reference

    fun getRegisterInformation(): Query = reference.orderByKey()

    fun getCurrentUser() = auth.currentUser

    fun getRegisterLoginInformation(): User = userDao.getRegisterLoginInformation()

    fun insertAllRegisterLogin(user: User) =
        userDao.insertAllRegisterInformation(user)

    fun getAllRegisterInformationOffline(userId: String): List<User> =
        userDao.getAllRegisterInformationOfflineLocal(userId)

    fun updateInformationUser(user: User) = userDao.updateInformationUser(user)

    private fun pathReference() = database.database.getReference("$REGISTER_INFORMATION_USER/")

    fun getAuthor(authorId: String): Task<DataSnapshot> =
        pathReference().child(authorId).child(USER).get()

    fun getCurrentUserId() = auth.currentUser?.uid
}