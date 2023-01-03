package br.com.zup.ezuppers.ui.zuppers.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.zup.ezuppers.data.model.State
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.domain.usecase.GetCitiesUseCase
import br.com.zup.ezuppers.domain.usecase.GetUfUseCase
import br.com.zup.ezuppers.domain.usecase.GetZuppersQuantityUseCase
import br.com.zup.ezuppers.domain.usecase.GetZuppersUseCase
import kotlinx.coroutines.launch

class ZuppersViewModel(
    private val getUfUseCase: GetUfUseCase,
    private val getCitiesUseCase: GetCitiesUseCase,
    private val getZuppersUseCase: GetZuppersUseCase,
    private val getZuppersQuantityUseCase: GetZuppersQuantityUseCase
) : ViewModel() {

    private var ufList = mutableListOf<State>()

    private val _ufResponse = MutableLiveData<MutableList<String>>()
    val ufResponse: LiveData<MutableList<String>> = _ufResponse

    private val _citiesResponse = MutableLiveData<MutableList<String>>()
    val citiesResponse: LiveData<MutableList<String>> = _citiesResponse

    private val _zuppersResponse = MutableLiveData<List<User>>()
    val zuppersResponse: LiveData<List<User>> = _zuppersResponse

    private val _errorListZuppers = MutableLiveData<Exception>()
    val errorListZuppers : LiveData<Exception> get() = _errorListZuppers

    private val _errorListStates = MutableLiveData<Exception>()
    val errorListStates : LiveData<Exception> get() = _errorListStates

    private val _errorListCities = MutableLiveData<Exception>()
    val errorListCities : LiveData<Exception> get() = _errorListCities

    fun getListZuppers(city: String) {
        viewModelScope.launch {
            try {
                val response = getZuppersUseCase.execute(city)

                _zuppersResponse.value = response
            } catch (ex: Exception) {
                _errorListZuppers.value = ex
            }
        }
    }

    fun getStates() {
        viewModelScope.launch {
            try {
                val response = getUfUseCase.execute()

                val stateList = mutableListOf<String>()
                response.forEach {
                    stateList.add(it.nome)
                }
                ufList = response

                _ufResponse.value = stateList
            } catch (ex: Exception) {
                _errorListStates.value = ex
            }
        }
    }

    fun getQuantityZuppers(cityName: String): Int {
        return getZuppersQuantityUseCase.execute(cityName)
    }

    fun getCities(ufName: String) {
        val selectedUf = ufList.find { state -> state.nome == ufName }
        selectedUf?.let { getCities(it.id) }
    }

    private fun getCities(ufId: Int) {
        viewModelScope.launch {
            try {
                val response = getCitiesUseCase.execute(ufId)
                val cityList = mutableListOf<String>()
                response.forEach {
                    cityList.add(it.nome)
                }

                _citiesResponse.value = cityList

            } catch (ex: Exception) {
//                Log.i("Error", "${ex.message}")
                _errorListCities.value = ex
            }
        }
    }
}