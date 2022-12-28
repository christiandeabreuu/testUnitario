package br.com.zup.ezuppers.domain.usecase

import br.com.zup.ezuppers.data.repository.ZuppersRepository
import br.com.zup.ezuppers.domain.model.User

class GetFavoriteUseCase(private val repository: ZuppersRepository) {

    fun execute(): List<User> {
        return repository.getFavorites()
    }
}