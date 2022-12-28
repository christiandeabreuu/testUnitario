package br.com.zup.ezuppers.ui.favorite.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import br.com.zup.ezuppers.databinding.FavoriteListItemBinding
import br.com.zup.ezuppers.domain.model.User

class FavoriteAdapter(
    private var favoriteList: MutableList<User>,
    private val clickFavorite: (favorite: User) -> Unit,
    private val clickName: (favorite: User) -> Unit,
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            FavoriteListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favorite = favoriteList[position]
        holder.showInformations(favorite)
        holder.binding.ivFavorite.setOnClickListener {
            holder.binding.cvFavoriteItemList.visibility = View.GONE
        }
        holder.binding.tvNameFavorite.setOnClickListener {
            clickName(favorite)
        }
    }

    override fun getItemCount() = favoriteList.size

    fun updateFavorite(newList: MutableList<User>) {
        favoriteList = newList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: FavoriteListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun showInformations(favorite: User) {
            binding.tvNameFavorite.text = favorite.name
        }
    }
}