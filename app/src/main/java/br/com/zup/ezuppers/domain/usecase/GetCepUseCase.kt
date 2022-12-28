package br.com.zup.ezuppers.domain.usecase

import br.com.zup.ezuppers.data.model.CepResult
import br.com.zup.ezuppers.data.repository.CepRepository

class GetCepUseCase(private val repository: CepRepository) {

    suspend fun execute(cep: String): CepResult {
        return repository.getCep(cep)
    }
}