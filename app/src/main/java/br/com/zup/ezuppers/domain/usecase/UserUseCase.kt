package br.com.zup.ezuppers.domain.usecase

import br.com.zup.ezuppers.data.repository.UserRepository
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.utilities.ERROR_LIST_REGISTER_DATA_LOCAL
import br.com.zup.ezuppers.utilities.ERROR_REGISTER_USER_DATA_LOCAL
import br.com.zup.ezuppers.utilities.ERROR_UPDATE_INFORMATION
import br.com.zup.ezuppers.viewstate.ViewState
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Query

class UserUseCase(private val userRepository: UserRepository) {

    fun databaseReference() = this.userRepository.databaseReference()

    fun getRegisterInformation(): Query = this.userRepository.getRegisterInformation()

    fun getCurrentUser() = this.userRepository.getCurrentUser()

    fun getRegisterLoginInformation(): ViewState<User> {
        return try {
            val user = this.userRepository.getRegisterLoginInformation()
            ViewState.Success(user)
        } catch (ex: Exception) {
            ViewState.Error(Exception(ERROR_LIST_REGISTER_DATA_LOCAL))
        }
    }

    fun insertAllRegisterLogin(user: User): ViewState<User> {
        return try {
            this.userRepository.insertAllRegisterLogin(user)
            ViewState.Success(user)
        } catch (ex: Exception) {
            ViewState.Error(Exception(ERROR_REGISTER_USER_DATA_LOCAL))
        }
    }

    fun getAllRegisterInformationOffline(userId: String): ViewState<List<User>> {
        return try {
            val user = this.userRepository.getAllRegisterInformationOffline(userId)
            ViewState.Success(user)
        } catch (ex: Exception) {
            ViewState.Error(Exception(ERROR_LIST_REGISTER_DATA_LOCAL))
        }
    }

    fun updateInformationUser(user: User): ViewState<User> {
        return try {
            this.userRepository.updateInformationUser(user)
            ViewState.Success(user)
        } catch (ex: Exception) {
            ViewState.Error(Exception(ERROR_UPDATE_INFORMATION))
        }
    }

    fun getCurrentUserId() = this.userRepository.getCurrentUserId()

    fun getAuthor(authorId: String): Task<DataSnapshot> = this.userRepository.getAuthor(authorId)
}