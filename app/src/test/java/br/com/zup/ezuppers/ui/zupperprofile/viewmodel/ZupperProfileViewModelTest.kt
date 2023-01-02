package br.com.zup.ezuppers.ui.zupperprofile.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.domain.usecase.UserUseCase
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
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
        viewModel = ZupperProfileViewModel(userUseCase)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `When call fun getCurrentUserRegister() should to call the same fun in useCase`() =
        runTest {

            coEvery { userUseCase.getCurrentUser()} returns mockk(relaxed = true)
            viewModel.getCurrentUserRegister()

            coVerify{ userUseCase.getCurrentUser() }
        }

    @Test
    fun `When call fun getCurrentUserRegister() should to call the same fun in useCase2`() =
        runTest {
            val expectedFirebaseUser = mockk<FirebaseUser>()
            coEvery { userUseCase.getCurrentUser()} returns expectedFirebaseUser

            val response = viewModel.getCurrentUserRegister()

            assert(expectedFirebaseUser == response)
            coVerify(exactly = 1){ userUseCase.getCurrentUser() }
        }

    @Test
    fun `When call fun getAuthor() should to call the same fun in useCase`() =
        runTest {
            val expectedTaskDAtaSnapshot = mockk<Task<DataSnapshot>>()
            val expectedUser = mockk<User>()
            val expectedAuthorId = "123"

            coEvery { userUseCase.getAuthor(expectedAuthorId).addOnSuccessListener {} } returns expectedTaskDAtaSnapshot

            val response = viewModel.getAuthor("123")

            assert(response == Unit)
            coVerify(exactly = 1){ userUseCase.getAuthor(expectedAuthorId) }
        }

}