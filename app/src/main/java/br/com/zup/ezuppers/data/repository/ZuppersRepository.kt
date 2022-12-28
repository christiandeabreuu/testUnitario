package br.com.zup.ezuppers.data.repository

import br.com.zup.ezuppers.domain.model.User

class ZuppersRepository {

    fun getZuppers(cityName: String): List<User> {
        return when (cityName) {
            "Rio de Janeiro" -> getRJZuppers()
            "Acrelândia" -> getACZuppers()
            else -> mutableListOf()
        }
    }

    internal fun getZuppersQuantity(cityName: String): Int {
        return when (cityName) {
            "Rio de Janeiro" -> getRJZuppers().size
            "Acrelândia" -> getACZuppers().size
            else -> 0
        }
    }

    private fun getRJZuppers(): List<User> {
        val zuppersList = mutableListOf<User>()

        zuppersList.add(
            User(
                name = "Samira",
                email = "samira.teixeira@zup.com.br",
                nickname = "Sammie",
                interests = "Gosto de festas e socializar ^^",
                state = "Rio de Janeiro"
            )
        )

        zuppersList.add(
            User(
                name = "Amanda",
                email = "amanda.torres@zup.com.br",
                nickname = "Amandinha",
                interests = "----",
                state = "Rio de Janeiro",
                favorite = true
            )
        )

        zuppersList.add(
            User(
                name = "Diogo",
                email = "diogo.garcia@zup.com.br",
                nickname = "Di",
                interests = "----",
                state = "Rio de Janeiro",
                favorite = true
            )
        )

        zuppersList.add(User(
            name = "Kim Alex",
            email = "kim.teixeira@zup.com.br",
            nickname = "K.A",
            interests = "----",
            state = "Rio de Janeiro"
        ))
        return zuppersList
    }

    private fun getACZuppers(): List<User> {
        val zuppersList = mutableListOf<User>()

        zuppersList.add(
            User(
                name = "Henrique",
                email = "henrique.carvalho@zup.com.br",
                nickname = "Henry",
                interests = "----",
                state = "Acre",
                favorite = true
            )
        )
        return zuppersList
    }

    fun getFavorites():List<User> {
        return (getRJZuppers() + getACZuppers()).filter { it.favorite == true }
    }
}