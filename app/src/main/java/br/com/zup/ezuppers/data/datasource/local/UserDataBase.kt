package br.com.zup.ezuppers.data.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.zup.ezuppers.data.datasource.local.dao.UserDAO
import br.com.zup.ezuppers.data.model.CepResult
import br.com.zup.ezuppers.domain.model.User

@Database(entities = [User::class, CepResult::class], version = 7)
abstract class UserDataBase : RoomDatabase() {
    abstract fun userDao(): UserDAO

    companion object {
        @Volatile
        private var INSTANCE: UserDataBase?= null

        fun getDatabase(context: Context): UserDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDataBase::class.java,
                    "infoRegister_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}