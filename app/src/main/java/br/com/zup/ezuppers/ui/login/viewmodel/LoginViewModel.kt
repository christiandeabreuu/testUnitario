package br.com.zup.ezuppers.ui.login.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.zup.ezuppers.data.repository.AuthenticationRepository
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.domain.usecase.UserUseCase
import br.com.zup.ezuppers.utilities.ERROR_EMAIL_MESSAGE
import br.com.zup.ezuppers.utilities.ERROR_INSERTING_REGISTER_USER_DATA_LOCAL
import br.com.zup.ezuppers.utilities.ERROR_LOGIN_MESSAGE
import br.com.zup.ezuppers.utilities.ERROR_PASSWORD_MESSAGE
import br.com.zup.ezuppers.viewstate.ViewState
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class LoginViewModel(
    application: Application,
    private val authenticationRepository: AuthenticationRepository,
    private val userUseCase: UserUseCase
) : AndroidViewModel(application) {

    private val _loginUserAddData = MutableLiveData<ViewState<User>>()
    val loginUserAddData = _loginUserAddData

    private var _errorState = MutableLiveData<String>()
    val errorState = _errorState

    private var _userIsValid = MutableLiveData<User>()
    val userIsValid: LiveData<User> get() = _userIsValid

    fun validateDataUser(user: User) {

        val emailPattern: Pattern =
            Pattern.compile(
                "^[a-z.]+@[zup]+.[com]+.[br]{2}\$"
            )

        val passwordPattern: Pattern =
            Pattern.compile(
                "^(?=.*[A-Z])(?=.*[!#@\$%&])(?=.*[0-9])(?=.*[a-z]).{6,15}"
            )
        when {
            user.email?.isEmpty() ?: true || !emailPattern.matcher(user.email).matches() -> {
                _errorState.value = ERROR_EMAIL_MESSAGE
            }
            user.password?.isEmpty() ?: true || !passwordPattern.matcher(user.password)
                .matches() -> {
                _errorState.value = ERROR_PASSWORD_MESSAGE
            }
            else -> {
                _userIsValid.value = user

            }
        }
    }

    fun loginUser(user: User?) {
        try {
            user?.email?.let {
                user.password?.let { it1 ->
                    authenticationRepository.loginUser(
                        it,
                        it1
                    ).addOnSuccessListener {
                        _loginUserAddData.value = ViewState.Success(user)
                    }.addOnFailureListener {
                        _errorState.value = ERROR_LOGIN_MESSAGE
                    }
                }
            }
        } catch (ex: Exception) {
            _errorState.value = "Error login"//ex.message
        }
    }

    fun getRegisterLoginInformation() {
        viewModelScope.launch {
            try {
                val response =
                    userUseCase.getRegisterLoginInformation()

                _loginUserAddData.value = response
            } catch (ex: Exception) {
                _loginUserAddData.value =
                    ViewState.Error(Throwable(ERROR_INSERTING_REGISTER_USER_DATA_LOCAL))
            }
        }
    }

    fun getCurrentUser() = authenticationRepository.getCurrentUser()
}

