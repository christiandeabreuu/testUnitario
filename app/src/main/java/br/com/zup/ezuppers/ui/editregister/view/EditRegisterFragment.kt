package br.com.zup.ezuppers.ui.editregister.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import br.com.zup.ezuppers.R
import br.com.zup.ezuppers.databinding.FragmentEditRegisterBinding
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.ui.editregister.viewmodel.EditRegisterViewModel
import br.com.zup.ezuppers.utilities.BUNDLE_KEY
import br.com.zup.ezuppers.utilities.SUCCESS_UPDATE
import br.com.zup.ezuppers.viewstate.ViewState
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditRegisterFragment : Fragment() {

    private lateinit var binding: FragmentEditRegisterBinding

    private val viewModel: EditRegisterViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavigation()
        showRegisterInformation()
        bringCep()
        initObserver()
        clickButtonEditRegister()
    }

    private fun bringCep() {
        binding.buttonCep.setOnClickListener {
            val cep = binding.tlCep.editText?.text.toString().replace(".", "").replace("-", "")
            viewModel.getCep(cep)
        }
    }

    private fun showRegisterInformation() {
        val userInformations = arguments?.getParcelable<User>(BUNDLE_KEY)
        binding.tlPronoun.editText?.setText(userInformations?.pronoun).toString()
        binding.tlName.editText?.setText(userInformations?.name).toString()
        binding.tlNickname.editText?.setText(userInformations?.nickname).toString()
        binding.tlBirthDate.editText?.setText(userInformations?.age).toString()
        binding.tlCep.editText?.setText(userInformations?.cep).toString()
        binding.tlRoadAv.editText?.setText(userInformations?.roadAv).toString()
        binding.tlCity.editText?.setText(userInformations?.city).toString()
        binding.tlState.editText?.setText(userInformations?.state).toString()
        binding.tlCountry.editText?.setText(userInformations?.country).toString()
        binding.tlNumber.editText?.setText(userInformations?.number).toString()
        binding.tlComplement.editText?.setText(userInformations?.complement)
            .toString()
        binding.tlInterests.editText?.setText(userInformations?.interests)
            .toString()
        binding.tlGender.editText?.setText(userInformations?.gender).toString()
        binding.tlSexualOrientation.editText?.setText(userInformations?.sexualOrientation)
            .toString()
    }

    private fun insertNewInformationRegister(): User {
        val pronoun = binding.tlPronoun.editText?.text.toString()
        val name = binding.tlName.editText?.text.toString()
        val nickname = binding.tlNickname.editText?.text.toString()
        val age = binding.tlBirthDate.editText?.text.toString()
        val cep = binding.tlCep.editText?.text.toString()
        val roadAv = binding.tlRoadAv.editText?.text.toString()
        val city = binding.tlCity.editText?.text.toString()
        val state = binding.tlState.editText?.text.toString()
        val country = binding.tlCountry.editText?.text.toString()
        val number = binding.tlNumber.editText?.text.toString()
        val complement = binding.tlComplement.editText?.text.toString()
        val interests = binding.tlInterests.editText?.text.toString()
        val gender = binding.tlGender.editText?.text.toString()
        val sexualOrientation = binding.tlSexualOrientation.editText?.text.toString()
        val email = viewModel.getCurrentUserRegister()?.email
        return User(
            name = name,
            pronoun = pronoun,
            nickname = nickname,
            email = email,
            age = age,
            cep = cep,
            roadAv = roadAv,
            city = city,
            state = state,
            country = country,
            number = number,
            complement = complement,
            interests = interests,
            gender = gender,
            sexualOrientation = sexualOrientation
        )
    }

    private fun initObserver() {
        viewModel.editRegisterState.observe(this.viewLifecycleOwner) {
            when (it) {
                is ViewState.Error -> {
                    Toast.makeText(
                        context,
                        "${it.throwable.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                is ViewState.Success -> {
                    Toast.makeText(
                        context,
                        SUCCESS_UPDATE,
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {}
            }
        }

        viewModel.messageState.observe(this.viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun hideBottomNavigation() {
        val view = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        view.visibility = View.GONE
    }

       private fun clickButtonEditRegister() {
        binding.saveRegisterInformation.setOnClickListener {
            val user = insertNewInformationRegister()
            if (viewModel.validateDateUserRegister(user)) {
                NavHostFragment.findNavController(this)
                    .navigate(R.id.action_editRegisterFragment_to_userProfileFragment2)
            }
        }
    }
}