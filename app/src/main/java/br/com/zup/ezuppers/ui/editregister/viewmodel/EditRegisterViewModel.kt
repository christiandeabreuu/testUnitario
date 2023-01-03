package br.com.zup.ezuppers.ui.editregister.viewmodel

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.Pattern

class EditRegisterViewModel(application: Application, private val editUserUseCase: UserUseCase, private val getCepUseCase : GetCepUseCase) :
    AndroidViewModel(application) {

    private val _editRegisterState = MutableLiveData<ViewState<User>>()
    val editRegisterState = _editRegisterState

    private val _cepResult = MutableLiveData<ViewState<CepResult>>()
    val cepResult: LiveData<ViewState<CepResult>> = _cepResult

    private var _messageState = MutableLiveData<String>()
    val messageState: LiveData<String> = _messageState

    fun getCep(cep: String) {
        viewModelScope.launch {
            try {
                val response = getCepUseCase.execute(cep)
                _cepResult.value = ViewState.Success(response)
            } catch (ex: Exception) {
               _cepResult.value = ViewState.Error(Throwable("Error getCep"))
            }
        }
    }

    fun validateDateUserRegister(user: User): Boolean {
        if (!haveErrorsDateUserEditRegister(user)) {
            updateInformationUserDbLocal(user)
            updateRealtimeDbRemote(user)
            return true
        }
        return false
    }

     fun haveErrorsDateUserEditRegister(user: User): Boolean {

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
            user.age?.isEmpty() ?: true || !agePattern.matcher(user.age).matches() -> {
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

    private fun updateInformationUserDbLocal(user: User) {
        viewModelScope.launch {
            try {
                val editRegisterResponse = withContext(Dispatchers.IO) {
                    editUserUseCase.updateInformationUser(user)
                }
                _editRegisterState.value = editRegisterResponse
            } catch (ex: Exception) {
                _editRegisterState.value = ViewState.Error(
                    Throwable(
                        ERROR_UPDATE_INFORMATION
                    )
                )
            }
        }
    }

    private fun updateRealtimeDbRemote(user: User) {
        editUserUseCase.databaseReference().setValue(user) { error, _ ->
            if (error != null) {
                _editRegisterState.value =
                    ViewState.Error(Throwable(ERROR_UPDATE_INFORMATION))
            }
            _editRegisterState.value = ViewState.Success(user)
        }
    }

    fun getCurrentUserRegister() = editUserUseCase.getCurrentUser()
}

