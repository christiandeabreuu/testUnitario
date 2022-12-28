package br.com.zup.ezuppers.data.repository

import br.com.zup.ezuppers.utilities.POST_PATH
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import java.util.*

class PostRepository {
    private val database = FirebaseDatabase.getInstance()
    private val reference = database.getReference("$POST_PATH/")

    fun postDatabaseReference() = reference

    fun getPostList(): Query {
        return reference.orderByValue()
    }

    fun getPostId(): String {
        val uuid = UUID.randomUUID()
        return uuid.toString()
    }


}