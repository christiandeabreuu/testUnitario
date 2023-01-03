package br.com.zup.ezuppers.ui.feed.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.zup.ezuppers.R
import br.com.zup.ezuppers.data.model.PostResponse
import br.com.zup.ezuppers.databinding.FragmentFeedBinding
import br.com.zup.ezuppers.ui.feed.view.adapter.FeedAdapter
import br.com.zup.ezuppers.ui.feed.viewmodel.FeedViewModel
import br.com.zup.ezuppers.utilities.AUTHORID
import br.com.zup.ezuppers.utilities.POST_ERROR_MESSAGE
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeedFragment : Fragment() {

    private lateinit var binding: FragmentFeedBinding

    private val viewModel: FeedViewModel by viewModel()

    private val adapter: FeedAdapter by lazy {
        FeedAdapter(arrayListOf(), ::updatePost, ::goToZupperProfile)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        initFeed()
        initObserver()
        showRecyclerView()
        sendPostClick()
    }

    private fun initFeed() {
        viewModel.getPostList()
    }

    private fun showRecyclerView() {
        binding.rvListFeed.adapter = adapter
        binding.rvListFeed.layoutManager = LinearLayoutManager(context)
    }

    private fun initObserver() {

        viewModel.postListResponse.observe(this.viewLifecycleOwner) {
            adapter.updatePostList(it)
        }
        viewModel.loading.observe(this.viewLifecycleOwner) {
            when (it) {
                true -> binding.pbLoading.visibility = View.VISIBLE
                else -> binding.pbLoading.visibility = View.GONE
            }
        }
        viewModel.message.observe(this.viewLifecycleOwner) {
            Toast.makeText(this.context, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getPost(): PostResponse {
        var authorId = ""
        viewModel.getCurrentUserId()?.let {
            authorId = it
        }
        return PostResponse(
            id = viewModel.getPostId(),
            authorId = authorId,
            postMessage = binding.etPost.text.toString(),
            author = viewModel.getUserName(),
            date = viewModel.getDate(),
        )
    }

    private fun sendPostClick() {
        binding.ivSendPost.setOnClickListener {
            val post = getPost()
            if (viewModel.validatePost(post)) {
                viewModel.savePost(post)
                binding.etPost.text?.clear()
            } else {
                Toast.makeText(
                    context,
                    POST_ERROR_MESSAGE,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun updatePost(postResponse: PostResponse) {
        viewModel.updatePostFavoriteStatus(postResponse)
    }

    private fun goToZupperProfile(authorId: String) {
        val bundle = bundleOf(AUTHORID to authorId)
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_feedFragment2_to_zupperProfileFragment, bundle)
    }
}