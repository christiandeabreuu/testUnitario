package br.com.zup.ezuppers.ui.zupperprofile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.domain.usecase.UserUseCase

class ZupperProfileViewModel(private val userUseCase : UserUseCase) : ViewModel() {

    private var _authorResponse : MutableLiveData<User> = MutableLiveData()
    val authorResponse : LiveData<User> = _authorResponse

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun getAuthor(authorId: String) {
        userUseCase.getAuthor(authorId).addOnSuccessListener { dataSnapshot ->
            val user = dataSnapshot.getValue(User::class.java)
            user?.let {
                _authorResponse.value = it
                _message.value = "ok"
            }
        }
    }

    fun getCurrentUserRegister() = userUseCase.getCurrentUser()
}