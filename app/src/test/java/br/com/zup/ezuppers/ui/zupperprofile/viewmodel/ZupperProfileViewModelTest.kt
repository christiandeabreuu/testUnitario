package br.com.zup.ezuppers.ui.zupperprofile.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.data.model.StatesResult
import br.com.zup.ezuppers.domain.usecase.UserUseCase
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
internal class ZupperProfileViewModelTest{

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private var userUseCase : UserUseCase = mockk(relaxed = true)

    private lateinit var viewModel: ZupperProfileViewModel

    @Before
    fun onBefore() {
        MockKAnnotations.init(this, relaxUnitFun = true)
//        userUseCase = UserUseCase(Application())
        viewModel = ZupperProfileViewModel(userUseCase)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `Dois mais dois nao é cinco `() {
        assert(2 + 2 != 5)
    }

    @Test
    fun `When call fun getCurrentUserRegister() should to call the same fun in useCase`() =
        runTest {

            coEvery { userUseCase.getCurrentUser()} returns mockk(relaxed = true)
            viewModel.getCurrentUserRegister()

            coVerify{ userUseCase.getCurrentUser() }
        }

}