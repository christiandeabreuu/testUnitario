package br.com.zup.ezuppers.domain.usecase

import br.com.zup.ezuppers.data.repository.ZuppersRepository

class GetZuppersQuantityUseCase(private val repository: ZuppersRepository) {

    fun execute(city:String): Int {
        return repository.getZuppersQuantity(city)
    }
}