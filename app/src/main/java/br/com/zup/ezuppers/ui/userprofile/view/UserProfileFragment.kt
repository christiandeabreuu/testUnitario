package br.com.zup.ezuppers.ui.userprofile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import br.com.zup.ezuppers.R
import br.com.zup.ezuppers.databinding.FragmentUserProfileBinding
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.ui.userprofile.view.adapter.UserProfileAdapter
import br.com.zup.ezuppers.ui.userprofile.viewmodel.UserProfileViewModel
import br.com.zup.ezuppers.utilities.BUNDLE_KEY
import br.com.zup.ezuppers.utilities.LOGOUT_MESSAGE
import br.com.zup.ezuppers.utilities.NetWorkHelper
import br.com.zup.ezuppers.viewstate.ViewState
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserProfileFragment : Fragment() {
    private lateinit var binding: FragmentUserProfileBinding

    private val viewModel : UserProfileViewModel by viewModel()

    private val adapter: UserProfileAdapter by lazy {
        UserProfileAdapter(arrayListOf(), this::goToEditRegister)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRvUserProfile()
        initObserver()
        initLogin()
        logOutClick()
    }

    override fun onStart() {
        super.onStart()
        val actualUser = viewModel.getCurrentUser()
        actualUser?.reload()
    }

    private fun logOutClick() {
        binding.btnLogout.setOnClickListener {
            viewModel.logOut()
            exit()
        }
    }

    private fun exit() {
        Toast.makeText(
            context,
            LOGOUT_MESSAGE,
            Toast.LENGTH_SHORT
        ).show()
        navigateToLogin()
    }

    private fun navigateToLogin() {
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_userProfileFragment_to_loginFragment)
    }

    private fun setUpRvUserProfile() {
        binding.rvUserProfile.adapter = adapter
        binding.rvUserProfile.setHasFixedSize(true)
    }

    private fun isConnect() = NetWorkHelper.isNetworkConnected(requireContext())

    private fun initLogin() {
        val userId = User()
        when (isConnect()) {
            true -> {
                viewModel.getAllInformationRealTime()
            }
            false -> {
                viewModel.getAllInformationRealTime()
                viewModel.getAllRegisterInformationOffline(userId.userId)
            }
        }
    }

    private fun initObserver() {

        viewModel.userProfileListState.observe(this.viewLifecycleOwner) {
            when (it) {
                is ViewState.Success -> {
                    adapter.updateRegisterUser(it.data.toMutableList())
                }
                is ViewState.Error -> {
                    Toast.makeText(
                        context,
                        "${it.throwable.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {}
            }

        }
        viewModel.userProfileDataListState.observe(this.viewLifecycleOwner) {
            adapter.updateRegisterUserData(it)

        }

        viewModel.message.observe(this.viewLifecycleOwner) {
            loadMessage(it)
        }
    }

    private fun loadMessage(message: String) {
        Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()
    }

    private fun goToEditRegister(user: User?) {
        val bundle = bundleOf(BUNDLE_KEY to user)
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_userProfileFragment2_to_editRegisterFragment, bundle)
    }
}






