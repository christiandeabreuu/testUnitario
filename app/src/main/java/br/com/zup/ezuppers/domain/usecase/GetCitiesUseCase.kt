package br.com.zup.ezuppers.domain.usecase

import br.com.zup.ezuppers.data.model.CitiesResult
import br.com.zup.ezuppers.data.repository.UfRepository

class GetCitiesUseCase(private var repository: UfRepository) {

    suspend fun execute(ufId: Int): CitiesResult {
        return repository.getCities(ufId)
    }
}