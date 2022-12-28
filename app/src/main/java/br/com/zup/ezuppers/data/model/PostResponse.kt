package br.com.zup.ezuppers.data.model

import com.google.gson.annotations.SerializedName

data class PostResponse(
    @SerializedName("post")
    val id: String = "",
    val authorId: String = "",
    val postMessage: String = "",
    val author: String = "",
    val date: String = "",
    var isFavorite: Boolean = false
)
