package br.com.zup.ezuppers.domain.usecase

import br.com.zup.ezuppers.data.model.State
import br.com.zup.ezuppers.data.model.StatesResult
import br.com.zup.ezuppers.data.repository.UfRepository

class GetUfUseCase(private var repository: UfRepository) {

    suspend fun execute(): ArrayList<State> {
        return repository.getStates()
    }
}