package br.com.zup.ezuppers.data.repository

import br.com.zup.ezuppers.data.datasource.remote.RetrofitService
import br.com.zup.ezuppers.data.model.CitiesResult
import br.com.zup.ezuppers.data.model.StatesResult

class UfRepository {
    suspend fun getStates(): StatesResult {
        return RetrofitService.apiService.getStates()
    }

    suspend fun getCities(ufId: Int): CitiesResult {
        return RetrofitService.apiService.getCities(ufId)
    }
}