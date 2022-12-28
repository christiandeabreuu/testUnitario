package br.com.zup.ezuppers.ui.mockfavoriteprofile.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.zup.ezuppers.R
import br.com.zup.ezuppers.databinding.FragmentMockFavoriteProfileBinding
import br.com.zup.ezuppers.utilities.UNFAVORITE_SUCESS_AMANDA

class MockFavoriteProfileFragment : Fragment() {
    private lateinit var binding: FragmentMockFavoriteProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMockFavoriteProfileBinding.inflate(inflater,container,false)

        clickFavoriteBtn()

        return binding.root
    }

    private fun clickFavoriteBtn() {
        binding.ivFavorite.setOnClickListener {
            binding.ivFavorite.setImageResource(R.drawable.ic_favorite_grey)
            unfavoriteMessage()
        }
    }

    private fun unfavoriteMessage() {
        Toast.makeText(
            context,
            UNFAVORITE_SUCESS_AMANDA,
            Toast.LENGTH_LONG
        ).show()
    }

}