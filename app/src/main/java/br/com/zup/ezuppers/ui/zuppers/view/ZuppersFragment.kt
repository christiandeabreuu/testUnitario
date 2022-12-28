package br.com.zup.ezuppers.ui.zuppers.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.zup.ezuppers.R
import br.com.zup.ezuppers.databinding.FragmentZuppersBinding
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.ui.zuppers.view.adapter.ZuppersAdapter
import br.com.zup.ezuppers.ui.zuppers.viewmodel.ZuppersViewModel
import br.com.zup.ezuppers.utilities.ZUPPER_KEY
import org.koin.androidx.viewmodel.ext.android.viewModel


class ZuppersFragment : Fragment() {
    private lateinit var binding: FragmentZuppersBinding

    private val viewModel : ZuppersViewModel by viewModel()

    private val adapter: ZuppersAdapter by lazy {
        ZuppersAdapter(arrayListOf(), this::goToZupperProfile)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentZuppersBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.actvState.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.getCities(text.toString())
                binding.actvCity.text.clear()
                binding.menuCity.visibility = View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.actvCity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                val city = binding.actvCity.text.toString()
                binding.tvCityTitle.text = city
                viewModel.getListZuppers(city)
                binding.tvQuantityTitle.text =
                    viewModel.getQuantityZuppers(city).toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        setUpRecyclerView()
        setUpObservers()
        viewModel.getStates()
    }

    private fun setUpRecyclerView() {
        binding.rvListZuppers.adapter = adapter
        binding.rvListZuppers.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setUpObservers() {
        viewModel.ufResponse.observe(this.viewLifecycleOwner) {
            val adapter = ArrayAdapter(requireContext(), R.layout.state_or_city_item, it)
            (binding.actvState as? AutoCompleteTextView)?.setAdapter(adapter)
        }

        viewModel.citiesResponse.observe(this.viewLifecycleOwner) {
            val adapter = ArrayAdapter(requireContext(), R.layout.state_or_city_item, it)
            (binding.actvCity as? AutoCompleteTextView)?.setAdapter(adapter)
        }

        viewModel.zuppersResponse.observe(this.viewLifecycleOwner) {
            adapter.updateZuppersList(it.toMutableList())
        }
    }

    private fun goToZupperProfile(zuppers: User) {
        val bundle = bundleOf(ZUPPER_KEY to zuppers)
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_zuppersFragment_to_zupperProfileFragment, bundle)
    }
}