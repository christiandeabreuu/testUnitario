package br.com.zup.ezuppers.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.domain.model.User
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
internal class FavoriteRepositoryTest{

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

        private lateinit var repository : FavoriteRepository

        @Before
        fun onBefore() {
            MockKAnnotations.init(this, relaxUnitFun = true)
            repository = FavoriteRepository()
            Dispatchers.setMain(Dispatchers.Unconfined)
        }

        @After
        fun onAfter() {
            Dispatchers.resetMain()
            unmockkAll()
        }

    @Test
    fun `when getListFavorite() is called should return listUser`() {

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

        val response = repository.getListFavorite()

        assert (response == listFavoriteZuppers)

    }
}
