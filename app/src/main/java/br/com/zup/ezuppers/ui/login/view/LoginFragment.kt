package br.com.zup.ezuppers.ui.login.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import br.com.zup.ezuppers.R
import br.com.zup.ezuppers.databinding.FragmentLoginBinding
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.ui.login.viewmodel.LoginViewModel
import br.com.zup.ezuppers.utilities.BUNDLE_KEY
import br.com.zup.ezuppers.utilities.ERROR_LOGIN_MESSAGE
import br.com.zup.ezuppers.utilities.NetWorkHelper
import br.com.zup.ezuppers.viewstate.ViewState
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModel()

    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        hideBottomNavigation()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickRegister()
        clickButtonLogin()
        initObserve()

        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        } catch (ex: Exception) {
            ex.message
        }
    }

    override fun onStart() {
        super.onStart()
        try {
            binding.tlEmail.editText?.setText(viewModel.getCurrentUser()?.email)

        } catch (e: Exception) {
            binding.tlEmail.editText?.setText("")
        }
    }

    private fun hideBottomNavigation() {
        val view = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        view.visibility = View.GONE
    }

    private fun getDataUser(): User {
        return User(
            email = binding.tlEmail.editText?.text.toString(),
            password = binding.tlPassword.editText?.text.toString()
        )
    }

    private fun clickRegister() {
        binding.tvNewRegister.setOnClickListener {
            goToRegister()
        }
    }

    private fun clickButtonLogin() {
        binding.buttonLogin.setOnClickListener {
            initLogin()
        }
    }

    private fun goToRegister() {
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_loginFragment_to_registerLoginFragment)
    }

    private fun isConnect() = NetWorkHelper.isNetworkConnected(requireContext())

    private fun initLogin() {
        when (isConnect()) {
            true -> {
                val user = getDataUser()
                viewModel.validateDataUser(user)
            }
            false -> {
                viewModel.getRegisterLoginInformation()
            }
        }
    }

    private fun initObserve() {
        viewModel.errorState.observe(this.viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
        }
        viewModel.loginUserAddData.observe(this.viewLifecycleOwner) {
            when (it) {
                is ViewState.Error -> {
                    ViewState.Error(
                        (Throwable(
                            ERROR_LOGIN_MESSAGE
                        ))
                    )
                }
                is ViewState.Loading -> ViewState.Loading(true)
                is ViewState.Success -> { goToFeed(it.data) }
            }
        }
//        lifecycleScope.launchWhenStarted {
//            viewModel.loginUserAddData.collect {
//                when (it) {
//                    is ViewState.Error -> {
//                        ViewState.Error(
//                            Throwable(
//                                ERROR_LOGIN_MESSAGE
//                            )
//                        )
//                    }
//                    is ViewState.Loading -> ViewState.Loading()
//                    is ViewState.Success -> {
//                        goToFeed(it.data)
//                    }
//                }
//            }
//        }
    }

    private fun goToFeed(user: User) {
        val bundle = bundleOf(BUNDLE_KEY to user)
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_loginFragment_to_feedFragment2, bundle)
    }
}
