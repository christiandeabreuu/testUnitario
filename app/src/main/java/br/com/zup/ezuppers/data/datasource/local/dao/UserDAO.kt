package br.com.zup.ezuppers.data.datasource.local.dao

import androidx.room.*
import br.com.zup.ezuppers.domain.model.User

@Dao
interface UserDAO {

    @Query("SELECT * FROM user")
    fun getRegisterLoginInformation(): User

    @Query("SELECT * FROM user WHERE userid =:userId ORDER BY userid ASC ")
    fun getAllRegisterInformationOfflineLocal(userId: String): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRegisterInformation(user: User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateInformationUser(user: User)
}