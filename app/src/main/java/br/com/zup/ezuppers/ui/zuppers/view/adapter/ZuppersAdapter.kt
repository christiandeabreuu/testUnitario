package br.com.zup.ezuppers.ui.zuppers.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.zup.ezuppers.R
import br.com.zup.ezuppers.databinding.ZupperListItemBinding
import br.com.zup.ezuppers.domain.model.User

class ZuppersAdapter(
    private var zupperList: MutableList<User>,
    private val clickZupper: (zupper: User) -> Unit,
) : RecyclerView.Adapter<ZuppersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ZupperListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val zupper = zupperList[position]
        holder.showInformations(zupper)
        holder.binding.cvZupperItemList.setOnClickListener {
            clickZupper(zupper)
        }
    }

    override fun getItemCount() = zupperList.size

    fun updateZuppersList(newList: MutableList<User>) {
        zupperList = newList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ZupperListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun showInformations(zupper: User) {
            binding.tvNameZupper.text = zupper.name

            if (zupper.favorite == true) {
                binding.ivFavorite.setImageResource(R.drawable.ic_favorite_red)
            } else {
                binding.ivFavorite.setImageResource(R.drawable.ic_favorite_grey)
            }
        }
    }
}