package br.com.zup.ezuppers.ui.register.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.zup.ezuppers.data.model.CepResult
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.domain.usecase.GetCepUseCase
import br.com.zup.ezuppers.domain.usecase.UserUseCase
import br.com.zup.ezuppers.utilities.*
import br.com.zup.ezuppers.viewstate.ViewState
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class RegisterOptionalViewModel(
    application: Application,
    private val userUseCase: UserUseCase,
    private val getCepUseCase: GetCepUseCase
) :
    AndroidViewModel(application) {

    private val _registerOptionalState = MutableLiveData<ViewState<User>>()
    val registerOptionalState = _registerOptionalState

    private var _messageState = MutableLiveData<String>()
    val messageState: LiveData<String> = _messageState

    private val _cepResult = MutableLiveData<CepResult>()
    val cepResult: LiveData<CepResult> = _cepResult

    fun validateDateUserRegister(user: User): Boolean {
        if (!haveErrorsDateUserRegister(user)) {
            insertAllRegister(user)
            savedRegisterInfoRealTime(user)
            return true
        }
        return false
    }

     fun haveErrorsDateUserRegister(user: User): Boolean {
        val cepPattern: Pattern =
            Pattern.compile(
                "[0-9]{5}-[0-9]{3}"
            )
        val agePattern: Pattern =
            Pattern.compile("[0-9]{2}/[0-9]{2}/[0-9]{4}")
        when {
            user.nickname?.isEmpty() == true -> {
                _messageState.value = NICKNAME_ERROR_MESSAGE
                return true
            }
            user.cep?.isEmpty() ?: true || !cepPattern.matcher(user.cep).matches() -> {
                _messageState.value = CEP_ERROR_MESSAGE
                return true
            }
            user.roadAv?.isEmpty() == true -> {
                _messageState.value = ROAD_ERROR_MESSAGE
                return true
            }
            user.complement?.isEmpty() == true -> {
                _messageState.value = COMPLEMENT_ERROR_MESSAGE
                return true
            }
            user.age?.isEmpty() == true || !agePattern.matcher(user.age).matches() -> {
                _messageState.value = AGE_ERROR_MESSAGE
                return true
            }
            user.city?.isEmpty() == true -> {
                _messageState.value = CITY_ERROR_MESSAGE
                return true
            }
            user.state?.isEmpty() == true -> {
                _messageState.value = STATE_ERROR_MESSAGE
                return true
            }
            user.country?.isEmpty() == true -> {
                _messageState.value = COUNTRY_ERROR_MESSAGE
                return true
            }
            user.number?.isEmpty() == true -> {
                _messageState.value = NUMBER_ERROR_MESSAGE
                return true
            }
            user.interests?.isEmpty() == true -> {
                _messageState.value = INTEREST_ERROR_MESSAGE
                return true
            }
            else -> {
                return false
            }
        }
    }

    fun getCep(cep: String) {
        viewModelScope.launch {
            try {
                val response =
                    getCepUseCase.execute(cep)
                _cepResult.value = response
            } catch (ex: Exception) {
                _messageState.value = "Error"
            }
        }
    }

    fun insertAllRegister(user: User) {
        viewModelScope.launch {
            try {
                val response =
                    userUseCase.insertAllRegisterLogin(user)

                _registerOptionalState.value = response
            } catch (ex: Exception) {
                _registerOptionalState.value =
                    ViewState.Error(Throwable(ERROR_INSERTING_REGISTER_USER_DATA_LOCAL))
            }
        }
    }

    fun savedRegisterInfoRealTime(user: User) {     //p
        userUseCase.databaseReference()
            .setValue(user) { error, _ ->
                if (error != null) {
                    _messageState.value = error.message
                }
                _messageState.value = SUCCESS_ADD_REGISTER_USER_DATA
            }
    }
}