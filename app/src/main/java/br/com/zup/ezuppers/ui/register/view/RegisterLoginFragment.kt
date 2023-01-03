package br.com.zup.ezuppers.ui.register.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import br.com.zup.ezuppers.R
import br.com.zup.ezuppers.databinding.FragmentRegisterLoginBinding
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.ui.register.viewmodel.RegisterLoginViewModel
import br.com.zup.ezuppers.utilities.BUNDLE_KEY
import br.com.zup.ezuppers.utilities.ERROR_LOGIN_MESSAGE
import br.com.zup.ezuppers.utilities.SUCCESS_ADD_REGISTER_USER_DATA
import br.com.zup.ezuppers.viewstate.ViewState
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterLoginFragment : Fragment() {

    private lateinit var binding: FragmentRegisterLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterLoginBinding.inflate(inflater, container, false)
        hideBottomNavigation()
        return binding.root
    }

    private val viewModel: RegisterLoginViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
        viewModel.getRegisterLoginInformation()
        clickButtonRegister()
    }

    private fun hideBottomNavigation() {
        val view = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        view.visibility = View.GONE
    }

    private fun clickButtonRegister() {
        binding.buttonRegister.setOnClickListener {
            val user = getDataUserRegisterLogin()
            viewModel.haveErrorsDateUser(user)
        }
    }

    private fun getDataUserRegisterLogin(): User {
        return (
                User(
                    name = binding.tlName.editText?.text.toString(),
                    email = binding.tlEmailRegister.editText?.text.toString(),
                    password = binding.tlPasswordRegister.editText?.text.toString()
                ))
    }

    private fun initObserve() {
        lifecycleScope.launchWhenStarted {
            viewModel.registerUserLoginState.collect{
                when (it) {
                    is ViewState.Error -> ERROR_LOGIN_MESSAGE
//                    is ViewState.Loading -> true  //
                    is ViewState.Success -> SUCCESS_ADD_REGISTER_USER_DATA
                    else -> {}
                }
            }
        }
//        viewModel.registerUserLoginState.observe(this.viewLifecycleOwner) {
//            when (it) {
//                is ViewState.Error -> ERROR_LOGIN_MESSAGE
//                is ViewState.Loading -> true
//                is ViewState.Success -> SUCCESS_ADD_REGISTER_USER_DATA
//            }
//        }

        viewModel.messageState.observe(this.viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
        viewModel.userIsValid.observe(this.viewLifecycleOwner) {
            goToRegisterOptional(it)
            insertRegisterLoginUserData()
            viewModel.registerUserLogin(it)
        }
    }

    private fun goToRegisterOptional(user: User) {
        val bundle = bundleOf(BUNDLE_KEY to user)
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_registerLoginFragment_to_registerOptionalFragment, bundle)
    }

    private fun insertRegisterLoginUserData() {
        viewModel.insertRegisterLoginUserData(
            User(
                name = binding.tlName.editText?.text.toString(),
                email = binding.tlEmailRegister.editText?.text.toString(),
                password = binding.tlPasswordRegister.editText?.text.toString()
            )
        )
    }
}