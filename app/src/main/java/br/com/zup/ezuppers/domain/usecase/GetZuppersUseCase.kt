package br.com.zup.ezuppers.domain.usecase

import br.com.zup.ezuppers.data.repository.ZuppersRepository
import br.com.zup.ezuppers.domain.model.User

class GetZuppersUseCase(private val repository: ZuppersRepository) {

    fun execute(city: String): List<User> {
        return repository.getZuppers(city)
    }
}

