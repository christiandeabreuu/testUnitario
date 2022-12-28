package br.com.zup.ezuppers.data.model

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "State")
data class UfResult(
    @SerializedName("uf")
    var uf: String?
) : Parcelable