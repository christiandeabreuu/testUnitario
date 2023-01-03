package br.com.zup.ezuppers.ui.zupperprofile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.zup.ezuppers.R
import br.com.zup.ezuppers.databinding.FragmentZupperProfileBinding
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.ui.home.view.HomeActivity
import br.com.zup.ezuppers.ui.zupperprofile.viewmodel.ZupperProfileViewModel
import br.com.zup.ezuppers.utilities.AUTHORID
import br.com.zup.ezuppers.utilities.SUCESS_FAVORITE_USER
import org.koin.androidx.viewmodel.ext.android.viewModel

class ZupperProfileFragment : Fragment() {

    private val viewModel : ZupperProfileViewModel by viewModel()

    private lateinit var binding: FragmentZupperProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentZupperProfileBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observable()
        getZupper()
    }

    private fun getZupper() {
        val zupper = arguments?.getString(AUTHORID)
        zupper?.let { viewModel.getAuthor(zupper) }
    }

    private fun clickFavoriteBtn() {
        binding.ivFavorite.setImageResource(R.drawable.ic_favorite_grey)
        binding.ivFavorite.setOnClickListener {
            binding.ivFavorite.setImageResource(R.drawable.ic_favorite_red)
            favoriteZupperMessage()
        }
    }

    private fun favoriteZupperMessage() {
        Toast.makeText(
            context,
            SUCESS_FAVORITE_USER,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun hideFavoriteBtn(user: User) {
        if (user.userId.equals(getCurrentUserRegister())) {
            binding.ivFavorite.visibility = View.GONE
        } else {
            binding.ivFavorite.visibility = View.VISIBLE
        }
    }

    private fun getCurrentUserRegister() = viewModel.getCurrentUserRegister()

    private fun observable() {
        viewModel.authorResponse.observe(this.viewLifecycleOwner) {
            binding.tvNameProfile.text = it.name
            binding.tvPronoun.text = it.pronoun
            binding.tvUserEmailValue.text = it.email
            binding.tvNickname.text = it.nickname
            binding.tvUserDescription.text = it.interests
            binding.tvUserState.text = it.state
            binding.tvUserAdressComplementValue.text = it.complement
            binding.tvUserAgeValue.text = it.age
            binding.tvUserCityValue.text = it.city
            binding.tvUserGenderValue.text = it.gender
            binding.tvUserRoadNameValue.text = it.roadAv
            binding.tvUserSexualOrientationValue.text = it.sexualOrientation

            clickFavoriteBtn()
            hideFavoriteBtn(it)

            (activity as HomeActivity).supportActionBar?.title = it.name
        }
    }
}