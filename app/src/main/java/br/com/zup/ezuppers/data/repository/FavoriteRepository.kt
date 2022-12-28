package br.com.zup.ezuppers.data.repository

import br.com.zup.ezuppers.domain.model.User

class FavoriteRepository {
    fun getListFavorite(): List<User> {
        val listFavoriteZuppers = mutableListOf<User>()
        listFavoriteZuppers.add(
            User(
                name = "Henrique",
                email = "henrique.carvalho@zup.com.br",
                nickname = "Henry",
                interests = "----",
                state = "Acre"
            )
        )
        listFavoriteZuppers.add(User(
            name = "Diogo",
            email = "diogo.garcia@zup.com.br",
            nickname = "Di",
            interests = "----",
            state = "Rio de Janeiro"
        ))
        listFavoriteZuppers.add(User(
            name = "Amanda",
            email = "amanda.torres@zup.com.br",
            nickname = "Amandinha",
            interests = "----",
            state = "Rio de Janeiro"
        ))
        listFavoriteZuppers.add(User(
            name = "Kim Alex",
            email = "kim.alex@zup.com.br",
            nickname = "Amandinha",
            interests = "----",
            state = "Rio de Janeiro"
        ))
        listFavoriteZuppers.add(User(
            name = "Luciano",
            email = "luciano.yamarashi@zup.com.br",
            nickname = "Amandinha",
            interests = "----",
            state = "São Paulo"
        ))
        listFavoriteZuppers.add(User(
            name = "Thay",
            email = "thay.thayana@zup.com.br",
            nickname = "Thay",
            interests = "----",
            state = "Mato Grosso"
        ))
        listFavoriteZuppers.add(User(
            name = "Joana",
            email = "Joana.joana@zup.com.br",
            nickname = "------",
            interests = "----",
            state = "São Paulo"
        ))
        return listFavoriteZuppers
    }
}