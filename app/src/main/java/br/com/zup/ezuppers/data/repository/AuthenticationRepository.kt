package br.com.zup.ezuppers.data.repository

import br.com.zup.ezuppers.data.datasource.AuthenticationDataSource
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

class AuthenticationRepository (private val authenticationDataSource: AuthenticationDataSource){

    fun registerUser(email: String, password: String): Task<AuthResult> =
        authenticationDataSource.registerUser(email, password)

    fun updateUserProfile(name: String): Task<Void>? =
        authenticationDataSource.updateUserProfile(name)

    fun logOutUser() = authenticationDataSource.logOut()

    fun loginUser(email: String, password: String): Task<AuthResult> =
        authenticationDataSource.loginUser(email, password)

    fun getUsersName(): String = authenticationDataSource.getUsersName()

    fun getCurrentUser() = authenticationDataSource.getCurrentUser()
}