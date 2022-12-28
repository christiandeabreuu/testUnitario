package br.com.zup.ezuppers.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class State(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("nome")
    val nome: String = "",
    @SerializedName("sigla")
    val sigla: String = ""
): Parcelable