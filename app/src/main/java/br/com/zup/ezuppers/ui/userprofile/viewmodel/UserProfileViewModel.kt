package br.com.zup.ezuppers.ui.userprofile.viewmodel

import android.app.Application
import androidx.lifecycle.*
import br.com.zup.ezuppers.data.repository.AuthenticationRepository
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.domain.usecase.UserUseCase
import br.com.zup.ezuppers.utilities.ERROR_REGISTER_DATA_LOCAL
import br.com.zup.ezuppers.utilities.ERROR_REGISTER_USER_DATA_LOCAL
import br.com.zup.ezuppers.viewstate.ViewState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserProfileViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val userUseCase: UserUseCase
) :
    ViewModel() {


    private var _userProfileListState = MutableLiveData<ViewState<List<User>>>()
    val userProfileListState = _userProfileListState

    private var _userProfileDataListState = MutableLiveData<HashMap<String, User?>>()
    val userProfileDataListState = _userProfileDataListState

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun getCurrentUser() = userUseCase.getCurrentUser()

    fun logOut() = authenticationRepository.logOutUser()

    fun getAllRegisterInformationOffline(email: String) {
        viewModelScope.launch {
            try {
                val response =
                    userUseCase.getAllRegisterInformationOffline(email)

                _userProfileListState.value = response
            } catch (ex: Exception) {
                _userProfileListState.value =
                    ViewState.Error(Throwable(ERROR_REGISTER_DATA_LOCAL))
//                _message.value = ERROR_REGISTER_USER_DATA_LOCAL
            }
        }
    }

    fun getAllInformationRealTime() {
        userUseCase.getRegisterInformation().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val registerUser = hashMapOf<String, User?>()
                val registerResponse = snapshot.getValue(User::class.java)
                val key = snapshot.key ?: ""
                registerResponse?.let {
                    registerUser.put(key, it)
                }

                _userProfileDataListState.value = registerUser
            }

            override fun onCancelled(error: DatabaseError) {
                _message.value = ERROR_REGISTER_USER_DATA_LOCAL
            }
        })
    }
}
