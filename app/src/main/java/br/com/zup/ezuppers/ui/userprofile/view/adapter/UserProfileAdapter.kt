package br.com.zup.ezuppers.ui.userprofile.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.zup.ezuppers.databinding.UserProfileItemBinding
import br.com.zup.ezuppers.domain.model.User

class UserProfileAdapter(

    private var listUser: MutableList<User?>,
    private var clickEditText: (user: User?) -> Unit,

    ) : RecyclerView.Adapter<UserProfileAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            UserProfileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    private var key = mutableListOf<String>()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userProfile = listUser[position]
        holder.showUserInfo(userProfile)
        holder.changeInformationVisibility()
        holder.binding.ivEditText.setOnClickListener {
            clickEditText(userProfile)
        }
    }

    override fun getItemCount(): Int = listUser.size

    fun updateRegisterUser(newList: MutableList<User?>) {
        listUser = newList
        notifyDataSetChanged()
    }

    fun updateRegisterUserData(newList: HashMap<String, User?>) {
        listUser = newList.values.toMutableList()
        key = newList.keys.toMutableList()
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: UserProfileItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun showUserInfo(user: User?) {
            binding.tvNameProfile.text = user?.name
            binding.tvUserEmailValue.text = user?.email
            binding.tvPronoun.text = user?.pronoun
            binding.tvNickname.text = user?.nickname
            binding.tvUserDescription.text = user?.interests
            binding.tvUserState.text = user?.state
            binding.tvUserCountry.text = user?.country
            binding.tvUserAgeValue.text = user?.age
            binding.tvUserCityValue.text = user?.city
            binding.tvUserRoadNameValue.text = user?.roadAv
            binding.tvUserRoadNumberValue.text = user?.number
            binding.tvUserAdressComplementValue.text = user?.complement
            binding.tvUserGenderValue.text = user?.gender
            binding.tvUserSexualOrientationValue.text = user?.sexualOrientation
        }

        fun changeInformationVisibility() {
            binding.tvPronoun.visibility = View.VISIBLE
            binding.tvNickname.visibility = View.VISIBLE
            binding.tvUserDescription.visibility = View.VISIBLE
            binding.tvUserState.visibility = View.VISIBLE
            binding.tvUserCountry.visibility = View.VISIBLE
            binding.tvUserAge.visibility - View.VISIBLE
            binding.tvUserAgeValue.visibility = View.VISIBLE
            binding.tvUserCity.visibility = View.VISIBLE
            binding.tvUserCityValue.visibility = View.VISIBLE
            binding.tvUserAdress.visibility = View.VISIBLE
            binding.tvUserRoadNameValue.visibility = View.VISIBLE
            binding.tvUserRoadNumberValue.visibility = View.VISIBLE
            binding.tvUserAdressComplementValue.visibility = View.VISIBLE
            binding.tvUserGender.visibility = View.VISIBLE
            binding.tvUserGenderValue.visibility = View.VISIBLE
            binding.tvUserSexualOrientation.visibility = View.VISIBLE
            binding.tvUserSexualOrientationValue.visibility = View.VISIBLE
        }
    }
}