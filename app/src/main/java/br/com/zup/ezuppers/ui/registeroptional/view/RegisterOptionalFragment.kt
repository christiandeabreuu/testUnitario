package br.com.zup.ezuppers.ui.register.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import br.com.zup.ezuppers.R
import br.com.zup.ezuppers.databinding.FragmentRegisterOptionalBinding
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.ui.register.viewmodel.RegisterOptionalViewModel
import br.com.zup.ezuppers.utilities.BUNDLE_KEY
import br.com.zup.ezuppers.utilities.ERROR_INSERTING_REGISTER_USER_DATA_LOCAL
import br.com.zup.ezuppers.utilities.SUCCESS_ADD_REGISTER_USER_DATA
import br.com.zup.ezuppers.viewstate.ViewState
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterOptionalFragment : Fragment() {

    private lateinit var binding: FragmentRegisterOptionalBinding

    private val viewModel : RegisterOptionalViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentRegisterOptionalBinding.inflate(inflater, container, false)
        hideBottomNavigation()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
        bringCep()
        clickButtonRegister()
    }

    private fun hideBottomNavigation() {
        val view = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        view.visibility = View.GONE
    }

    private fun clickButtonRegister() {
        binding.buttonRegisterOptional.setOnClickListener {
            val user = getDataUserRegisterOptional()
            if (viewModel.validateDateUserRegister(user)) {
                sucessMessage()
                goRegisterToFeed(user)
                insertRegisterUserData()
            }
        }
    }

    private fun sucessMessage() = Toast.makeText(this.context, SUCCESS_ADD_REGISTER_USER_DATA, Toast.LENGTH_SHORT).show()

    private fun bringCep() {
        binding.buttonCep.setOnClickListener {
            val cep = binding.tlCep.editText?.text.toString().replace(".", "").replace("-", "")
            viewModel.getCep(cep)
        }
    }

    private fun getDataUserRegisterOptional(): User {
        val arguments = arguments?.getParcelable<User>(BUNDLE_KEY)
        return (
                User(
                    name = arguments?.name.toString(),
                    email = arguments?.email.toString(),
                    password = arguments?.password.toString(),
                    userId = arguments?.userId.toString(),
                    nickname = binding.tlNickname.editText?.text.toString(),
                    age = binding.tlBirthDate.editText?.text.toString(),
                    cep = binding.tlCep.editText?.text.toString(),
                    city = binding.tlCity.editText?.text.toString(),
                    state = binding.tlState.editText?.text.toString(),
                    country = binding.tlCountry.editText?.text.toString(),
                    gender = binding.tlGender.editText?.text.toString(),
                    sexualOrientation = binding.tlSexualOrientation.editText?.text.toString(),
                    roadAv = binding.tlRoadAv.editText?.text.toString(),
                    number = binding.tlNumber.editText?.text.toString(),
                    complement = binding.tlComplement.editText?.text.toString(),
                    interests = binding.tlInterests.editText?.text.toString(),
                    pronoun = binding.tlPronoun.editText?.text.toString(),

                    ))
    }

    private fun initObserve() {
        viewModel.registerOptionalState.observe(this.viewLifecycleOwner) {
            when (it) {
                is ViewState.Error -> ERROR_INSERTING_REGISTER_USER_DATA_LOCAL
                is ViewState.Loading -> true
                is ViewState.Success -> {
                    SUCCESS_ADD_REGISTER_USER_DATA
                }
            }
        }

        viewModel.messageState.observe(this.viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }

        viewModel.cepResult.observe(viewLifecycleOwner, Observer {
            it.let {
                binding.tlCep.editText?.setText(it.cep)
                binding.tlRoadAv.editText?.setText(it.road)
                binding.tlComplement.editText?.setText(it.complement)
                binding.tlCity.editText?.setText(it.city)
                binding.tlState.editText?.setText(it.uf)
            }
        })
    }

    private fun goRegisterToFeed(user: User) {
        val bundle = bundleOf(BUNDLE_KEY to user)
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_registerOptionalFragment_to_feedFragment2, bundle)
    }

    private fun insertRegisterUserData() {
        val arguments = arguments?.getParcelable<User>(BUNDLE_KEY)
        viewModel.insertAllRegister(
            User(
                name = arguments?.name.toString(),
                email = arguments?.email.toString(),
                password = arguments?.password.toString(),
                userId = arguments?.userId.toString(),
                nickname = binding.tlNickname.editText?.text.toString(),
                age = binding.tlBirthDate.editText?.text.toString(),
                country = binding.tlCountry.editText?.text.toString(),
                gender = binding.tlGender.editText?.text.toString(),
                sexualOrientation = binding.tlSexualOrientation.editText?.text.toString(),
                number = binding.tlNumber.editText?.text.toString(),
                complement = binding.tlComplement.editText?.text.toString(),
                interests = binding.tlInterests.editText?.text.toString(),
                cep = binding.tlCep.editText?.text.toString(),
                roadAv = binding.tlRoadAv.editText?.text.toString(),
                state = binding.tlState.editText?.text.toString(),
                city = binding.tlCity.editText?.text.toString(),
                pronoun = binding.tlPronoun.editText?.text.toString(),
            )
        )
    }
}
