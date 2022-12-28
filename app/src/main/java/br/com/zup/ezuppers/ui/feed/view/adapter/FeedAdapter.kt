package br.com.zup.ezuppers.ui.feed.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.zup.ezuppers.R
import br.com.zup.ezuppers.data.model.PostResponse
import br.com.zup.ezuppers.databinding.FeedItemBinding

class FeedAdapter(
    private var postList: MutableList<PostResponse>,
    private val onCLick: (post: PostResponse) -> Unit,
    private val onNameClick: (authorId: String) -> Unit,
) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FeedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = postList[position]
        holder.showPosts(post)
        holder.binding.ivFavorite.setOnClickListener {
            post.isFavorite = !post.isFavorite
            onCLick(post)
        }
        holder.binding.tvNameFeed.setOnClickListener {
            onNameClick(post.authorId)
        }
    }

    override fun getItemCount() = postList.size

    fun updatePostList(newList: MutableList<PostResponse>) {
        postList = newList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: FeedItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun showPosts(post: PostResponse) {
            binding.tvTextPerson.text = post.postMessage
            binding.tvNameFeed.text = post.author
            binding.tvDate.text = post.date
            if (post.isFavorite) {
                binding.ivFavorite.setImageResource(R.drawable.ic_favorite_red)
            } else {
                binding.ivFavorite.setImageResource(R.drawable.ic_favorite_grey)
            }
        }

    }
}