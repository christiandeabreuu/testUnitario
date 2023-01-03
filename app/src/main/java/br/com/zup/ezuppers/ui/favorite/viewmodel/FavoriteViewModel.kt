package br.com.zup.ezuppers.ui.favorite.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.domain.usecase.GetFavoriteUseCase
import kotlinx.coroutines.launch

class FavoriteViewModel(
    application: Application,
    private val getFavoritesUseCase: GetFavoriteUseCase
) : AndroidViewModel(application) {

    private var _favoriteZuppers = MutableLiveData<List<User>>()
    val favoriteZuppers: LiveData<List<User>> = _favoriteZuppers

    private var _errorState = MutableLiveData<String>()
    val errorState: LiveData<String> = _errorState

    fun getListFavorite() {
        viewModelScope.launch {
            try {
                val response =
                    getFavoritesUseCase.execute()
                _favoriteZuppers.value = response
            } catch (ex: Exception) {
                _errorState.value = "error Exception"
            }
        }
    }

}