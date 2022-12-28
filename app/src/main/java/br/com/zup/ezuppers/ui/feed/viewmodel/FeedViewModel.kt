package br.com.zup.ezuppers.ui.feed.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.zup.ezuppers.data.model.PostResponse
import br.com.zup.ezuppers.data.repository.AuthenticationRepository
import br.com.zup.ezuppers.data.repository.PostRepository
import br.com.zup.ezuppers.domain.usecase.UserUseCase
import br.com.zup.ezuppers.utilities.EMPTY_POST_ERROR_MESSAGE
import br.com.zup.ezuppers.utilities.POST_ERROR_MESSAGE
import br.com.zup.ezuppers.utilities.POST_SIZE_ERROR_MESSAGE
import br.com.zup.ezuppers.utilities.POST_SUCESS_MESSAGE
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class FeedViewModel(
    application: Application,
    private val authenticationRepository: AuthenticationRepository,
    private val postRepository : PostRepository,
    private val userUseCase : UserUseCase
) : AndroidViewModel(application) {

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _postListResponse = MutableLiveData<MutableList<PostResponse>>()
    val postListResponse: LiveData<MutableList<PostResponse>> = _postListResponse

    fun getUserName() = authenticationRepository.getUsersName()

    fun getPostId() = postRepository.getPostId()

    fun getCurrentUserId() = userUseCase.getCurrentUserId()

    fun getPostList() {
        _loading.value = true

        postRepository.getPostList()
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val postList = mutableListOf<PostResponse>()
                    val listByDate = mutableListOf<PostResponse>()

                    for (resultSnapshot in snapshot.children) {
                        val postListResponse = resultSnapshot.getValue(PostResponse::class.java)
                        postListResponse?.let {
                            postList.add(it)
                        }
                    }
                    postList.sortByDescending {
                        listByDate.add(it)
                    }
                    _postListResponse.value = postList
                    _loading.value = false
                }

                override fun onCancelled(error: DatabaseError) {
                    _message.value = error.message
                    _loading.value = false
                }
            })
    }

    fun updatePostFavoriteStatus(post: PostResponse) {
        postRepository.postDatabaseReference()
            .child(post.id)
            .child("favorite")
            .setValue(post.isFavorite)
    }

    fun savePost(post: PostResponse) {
        postRepository.postDatabaseReference().child(post.id)
            .setValue(post) { error, _ ->
                if (error != null) {
                    _message.value = POST_ERROR_MESSAGE
                } else {
                    _message.value = POST_SUCESS_MESSAGE
                    getPostList()
                }
            }
    }

    fun validatePost(post: PostResponse): Boolean {

        return if (post.postMessage.isEmpty()) {
            _message.value = EMPTY_POST_ERROR_MESSAGE
            false
        } else if (post.postMessage.length > 280) {
            _message.value = POST_SIZE_ERROR_MESSAGE
            false
        } else {
            true
        }
    }

    fun getDate(): String {
        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())
        val formattedMonth = monthFormat.format(c.time)

        return "$day $formattedMonth"
    }

}
