package br.com.zup.ezuppers.domain.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user")
data class User(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "userid")
    var userId: String = "",

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "email")
    var email: String? = null,

    var favorite: Boolean? = null,

    @ColumnInfo(name = "nickname")
    var nickname: String? = null,

    @ColumnInfo(name = "age")
    var age: String? = null,
    @ColumnInfo(name = "cep")
    var cep: String? = null,

    @ColumnInfo(name = "city")
    var city: String? = null,

    @ColumnInfo(name = "state")
    var state: String? = null,

    @ColumnInfo(name = "country")
    var country: String? = null,

    @ColumnInfo(name = "gender")
    var gender: String? = null,

    @ColumnInfo(name = "sexualOrientation")
    var sexualOrientation: String? = null,

    @ColumnInfo(name = "password")
    var password: String? = null,

    @ColumnInfo(name = "roadAv")
    var roadAv: String? = null,

    @ColumnInfo(name = "number")
    var number: String? = null,

    @ColumnInfo(name = "complement")
    var complement: String? = null,

    @ColumnInfo(name = "interests")
    var interests: String? = null,

    @ColumnInfo(name = "pronoun")
    var pronoun: String? = null,
    ) : Parcelable