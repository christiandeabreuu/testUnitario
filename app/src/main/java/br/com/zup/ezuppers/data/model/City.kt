package br.com.zup.ezuppers.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    @SerializedName("id")
    val id: Int,
     @SerializedName("nome")
    val nome: String,
    ): Parcelable