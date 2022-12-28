package br.com.zup.ezuppers.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "address")
data class CepResult(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idUser")
    var idUser: Long = 1,
    @SerializedName("cep")
    var cep: String?,
    @SerializedName("logradouro")
    var road: String?,
    @SerializedName("bairro")
    var district: String?,
    @SerializedName("uf")
    var uf: String?,
    @SerializedName("complemento")
    var complement: String?,
    @SerializedName("localidade")
    var city: String?
) : Parcelable