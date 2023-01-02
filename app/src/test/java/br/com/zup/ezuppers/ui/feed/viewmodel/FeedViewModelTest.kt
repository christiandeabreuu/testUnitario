package br.com.zup.ezuppers.ui.feed.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.ezuppers.data.model.PostResponse
import br.com.zup.ezuppers.data.repository.AuthenticationRepository
import br.com.zup.ezuppers.data.repository.PostRepository
import br.com.zup.ezuppers.domain.usecase.UserUseCase
import br.com.zup.ezuppers.utilities.EMPTY_POST_ERROR_MESSAGE
import br.com.zup.ezuppers.utilities.POST_SIZE_ERROR_MESSAGE
import br.com.zup.ezuppers.utilities.POST_SUCESS_MESSAGE
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
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
internal class FeedViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private var authenticationRepository: AuthenticationRepository = mockk(relaxed = true)

    @RelaxedMockK
    private var postRepository: PostRepository = mockk(relaxed = true)

    @RelaxedMockK
    private var userUseCase: UserUseCase = mockk(relaxed = true)

    @RelaxedMockK
    private var application: Application = mockk(relaxed = true)

    private lateinit var viewModel: FeedViewModel

    @Before
    fun onBefore() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel =
            FeedViewModel(application, authenticationRepository, postRepository, userUseCase)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `getUserName() should call the same fun on repository`() {

        every { authenticationRepository.getUsersName() } returns "Christian"
        viewModel.getUserName()

        verify { authenticationRepository.getUsersName() }
    }

    @Test
    fun `getUserName should call the same fun on repository 2`() {
        val expectedUserName = "Christian"
        every { authenticationRepository.getUsersName() } returns expectedUserName

        val response = viewModel.getUserName()

        assert(expectedUserName == response)
        verify (exactly = 1) { authenticationRepository.getUsersName() }
    }

    @Test
    fun `getPostId should call the same fun on repository`() {

        every { postRepository.getPostId() } returns "String"

        viewModel.getPostId()

        verify { postRepository.getPostId() }
    }

    @Test
    fun `getPostId should call the same fun on repository 2`() {
        val expectedPostId = "123"
        every { postRepository.getPostId() } returns expectedPostId

        val response = viewModel.getPostId()

        assert(expectedPostId == response)
        verify (exactly = 1){ postRepository.getPostId() }
    }

    @Test
    fun `getCurrentUserId should call the same fun on repository`() {

        every { userUseCase.getCurrentUserId() } returns "String"

        viewModel.getCurrentUserId()

        verify { userUseCase.getCurrentUserId() }
    }

    @Test
    fun `getCurrentUserId should call the same fun on repository 2`() {
        val expectedUserIdId = "1"
        every { userUseCase.getCurrentUserId() } returns expectedUserIdId

        val response = viewModel.getCurrentUserId()

        assert(expectedUserIdId == response)
        verify (exactly = 1){ userUseCase.getCurrentUserId() }
    }


    @Test
    fun `updatePostFavoriteStatus() change Post the favorite status `() {
        val expectedPostResponse = PostResponse("1", "a", "oii", "Author", "25/12/2022", false)

        every { postRepository.postDatabaseReference() } returns mockk(relaxed = true)

        viewModel.updatePostFavoriteStatus(expectedPostResponse)

        verify { postRepository.postDatabaseReference() }
        //.child
    }

    @Test
    fun `When call fun savePost should to return `() {
        val expectedPostResponse = PostResponse("", "a", "oii", "Author", "25/12/2022", true)
        val expectedPostResponse1 = PostResponse("", "a", "oii", "Author", "25/12/2022", false)
        val expectedPostResponse2 = PostResponse()
        val expectedDatabaseReference = mockk<DatabaseReference>()
        val expectedDatabaseError = mockk<DatabaseError>()

        every { postRepository.postDatabaseReference().child(expectedPostResponse.id)
                .setValue(expectedPostResponse)
        } returns mockk(relaxed= true)

        viewModel.savePost(expectedPostResponse)
       val response = viewModel.message.value

        assertEquals(response, POST_SUCESS_MESSAGE)
        //.child
    }

    @Test
    fun `When call fun validatePost should to return the error size post`() {
        val expectedPostResponse = PostResponse(
            "1",
            "123",
            "sdkdgfdskfdgjfsgdfbcdjkfbjfdfbjfgbasdkdgfdskfdgjfsgdfbcd" +
                    "jkfbjfdfbjfgbasdkdgfdskfdgjfsgdfbcdjkfbjfdfbjfgbasdkdgfdskfdgjfsgdfbcdjkfbjfdfbj" +
                    "fgbasdkdgfdskfdgjfsgdfbcdjkfbjfdfbjfgbasdkdgfdskfdgjfsgdfbcdjkfbjfdfbjfgbasdkdgfdskfdg" +
                    "jfsgdfbcdjkfbjfdfbjfgbasdkdgfdskfdgjfsgdfbcdjkfbjfdfbjfgbasdkdgfdskfdgjfsgdfbcdjkfbjfdf" +
                    "bjfgbasdkdgfdskfdgjfsgdfbcdjkfbjfdfbjfgbasdkdgfdskfdgjfsgdfbcdjkfbjfdfbjfgbasdk" +
                    "dgfdskfdgjfsgdfbcdjkfbjfdfbjfgbasdkdgfdskfdgjfsgdfbcdjkfbjfdfbjfgbasdkdgfdskfdgjfs" +
                    "jfdfbjfgbasdkdgfdskfdgjfsgdfbcdjkfbjfdfbjfgbasdkdgfdskfdgjfsgdfbcdjkfbjfdfbjfgbasdkdgfdsk" +
                    "fdgjfsgdfbcdjkfbjfdfbjfgbasdkdgfdskfdgjfsgdfbcdjkfbjfdfbjfgbasdkdgfdskfdgjfsgdfbcdjkfbjfdfbj" +
                    "fgbasdkdgfdskfdgjfsgdfbcdjkfbjfdfbjfgbasdkdgfdskfdgjfsgdfbcdjkfbjfdfbjfgbasdkdgfdskfdgjfsgdfb" +
                    "djkfbjfdfbjfgbasdkdgfdskfdgjfsgdfbcdbcdjkfbjfdfbjfgb",
            "Christian",
            "25/12/2022",
            false
        )

        every {
            postRepository.postDatabaseReference().child(expectedPostResponse.id)
                .setValue(expectedPostResponse)
        } returns mockk(relaxed = true)

        viewModel.validatePost(expectedPostResponse)

        assertEquals(viewModel.message.value, POST_SIZE_ERROR_MESSAGE)
    }

    @Test
    fun `When call fun validatePost should to return the error`() {
        val expectedPostResponse = PostResponse("")

        every {
            postRepository.postDatabaseReference().child(expectedPostResponse.id)
                .setValue(expectedPostResponse)
        } returns mockk(relaxed = true)

        viewModel.validatePost(expectedPostResponse)

        assertEquals(viewModel.message.value, EMPTY_POST_ERROR_MESSAGE)
    }


}

