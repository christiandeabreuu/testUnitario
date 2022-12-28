package br.com.zup.ezuppers.data.repository

import br.com.zup.ezuppers.data.datasource.remote.RetrofitService
import br.com.zup.ezuppers.data.datasource.remote.RetrofitService.Companion.CEP_URL
import br.com.zup.ezuppers.data.model.CepResult

class CepRepository {
    suspend fun getCep(cep: String): CepResult {
        return RetrofitService.apiService.getAddress(CEP_URL.replace("{cep}", cep))
    }
}