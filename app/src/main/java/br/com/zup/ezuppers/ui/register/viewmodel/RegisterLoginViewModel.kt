package br.com.zup.ezuppers.ui.register.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.zup.ezuppers.data.repository.AuthenticationRepository
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.domain.usecase.UserUseCase
import br.com.zup.ezuppers.utilities.*
import br.com.zup.ezuppers.viewstate.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.Pattern

class RegisterLoginViewModel(
    application: Application,
    private val userUseCase: UserUseCase,
    private val authenticationRepository: AuthenticationRepository
) :
    AndroidViewModel(application) {


    private val _registerUserLoginState = MutableStateFlow<ViewState<User>>(ViewState.Loading(true))
    val registerUserLoginState : StateFlow<ViewState<User>> get() = _registerUserLoginState
//    val registerUserLoginState = _registerUserLoginState

    private var _messageState = MutableLiveData<String>()
    val messageState: LiveData<String> = _messageState

    fun validateDateUserLogin(user: User): Boolean {
        if (!haveErrorsDateUser(user)) {
            registerUserLogin(user)
            return true
        }
        return false
    }

     fun haveErrorsDateUser(user: User): Boolean {
        val emailPattern: Pattern =
            Pattern.compile(
                "^[a-z.]+@[zup]+.[com]+.[br]{2}\$"
            )
        val passwordPattern: Pattern =
            Pattern.compile(
                "^(?=.*[A-Z])(?=.*[!#@$%&])(?=.*[0-9])(?=.*[a-z]).{6,15}"
            )
        val namePattern: Pattern =
            Pattern.compile(
                "(.*[a-z]){3}"
            )

        when {
            user.name?.isEmpty() ?: true || !namePattern.matcher(user.name).matches() -> {
                _messageState.value = NAME_ERROR_MESSAGE
                return true
            }
            user.email?.isEmpty() ?: true || !emailPattern.matcher(user.email).matches() -> {
                _messageState.value = EMAIL_ERROR_MESSAGE
                return true
            }
            user.password?.isEmpty() ?: true || !passwordPattern.matcher(user.password)
                .matches() -> {
                _messageState.value = PASSWORD_ERROR_MESSAGE
                return true
            }
            else -> {
                return false
            }
        }
    }

    private fun registerUserLogin(user: User) {
        try {
            user.email?.let {
                user.password?.let { it1 ->
                    authenticationRepository.registerUser(
                        it,
                        it1
                    ).addOnSuccessListener {
                        user.name?.let { it2 ->
                            authenticationRepository.updateUserProfile(it2)?.addOnSuccessListener {
                                _registerUserLoginState.value = ViewState.Success(user)
                            }
                        }
                    }.addOnFailureListener {
                        _messageState.value = CREATE_USER_ERROR_MESSAGE
                    }
                }
            }
        } catch (ex: Exception) {
            _messageState.value = ex.message
        }
    }

    fun insertRegisterLoginUserData(user: User) {
        viewModelScope.launch {
            try {
                val response = userUseCase.insertAllRegisterLogin(user)
                _registerUserLoginState.value = response
            } catch (ex: Exception) {
                _registerUserLoginState.value =
                    ViewState.Error(Throwable(ERROR_INSERTING_REGISTER_USER_DATA_LOCAL))
            }
        }
    }

    fun getRegisterLoginInformation() {
        viewModelScope.launch {
            try {
                val response = userUseCase.getRegisterLoginInformation()

                _registerUserLoginState.value = response
            } catch (ex: Exception) {
                _registerUserLoginState.value =
                    ViewState.Error(Throwable(ERROR_INSERTING_REGISTER_USER_DATA_LOCAL))
            }
        }
    }
}