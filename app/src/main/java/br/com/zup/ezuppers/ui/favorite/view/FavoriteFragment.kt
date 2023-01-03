package br.com.zup.ezuppers.ui.favorite.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.zup.ezuppers.R
import br.com.zup.ezuppers.databinding.FragmentFavoriteBinding
import br.com.zup.ezuppers.domain.model.User
import br.com.zup.ezuppers.ui.favorite.view.adapter.FavoriteAdapter
import br.com.zup.ezuppers.ui.favorite.viewmodel.FavoriteViewModel
import br.com.zup.ezuppers.utilities.ZUPPER_KEY
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding

    private val viewModel: FavoriteViewModel by viewModel()

    private val adapter: FavoriteAdapter by lazy {
        FavoriteAdapter(arrayListOf(), this::goToZupperProfile, this::goToFakeAmandaProfile)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getListFavorite()
        setUpRecyclerView()
        viewModel.favoriteZuppers.observe(viewLifecycleOwner) {
            adapter.updateFavorite(it.toMutableList())
        }
    }

    private fun setUpRecyclerView() {
        binding.rvListFavorite.adapter = adapter
        binding.rvListFavorite.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun goToZupperProfile(zuppers: User) {
        val bundle = bundleOf(ZUPPER_KEY to zuppers)
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_favoriteFragment_to_zupperProfileFragment, bundle)
    }

    private fun goToFakeAmandaProfile(zuppers: User) {
        val bundle = bundleOf(ZUPPER_KEY to zuppers)
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_favoriteFragment_to_mockFavoriteProfileFragment, bundle)
    }
}