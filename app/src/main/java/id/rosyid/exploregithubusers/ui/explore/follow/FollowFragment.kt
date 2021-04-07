package id.rosyid.exploregithubusers.ui.explore.follow

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.rosyid.exploregithubusers.R
import id.rosyid.exploregithubusers.data.entities.FollowersResponse
import id.rosyid.exploregithubusers.data.entities.FollowingResponse
import id.rosyid.exploregithubusers.databinding.FollowFragmentBinding
import id.rosyid.exploregithubusers.utils.Resource
import id.rosyid.exploregithubusers.utils.autoCleared

@AndroidEntryPoint
class FollowFragment : Fragment() {

    private var binding: FollowFragmentBinding by autoCleared()
    private val viewModel: FollowViewModel by viewModels()
    private lateinit var adapter: Any

    enum class Type {
        FOLLOWER,
        FOLLOWING
    }

    companion object {
        const val ARG_PAGE_TYPE = "page_type"
        const val ARG_USERNAME = "username"

        @JvmStatic
        fun newInstance(type: Type, username: String) =
            FollowFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PAGE_TYPE, type.ordinal)
                    putString(ARG_USERNAME, username)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FollowFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        when (requireArguments().getInt(ARG_PAGE_TYPE)) {
            Type.FOLLOWER.ordinal -> adapter = FollowersAdapter()
            Type.FOLLOWING.ordinal -> adapter = FollowingAdapter()
        }
        binding.recyclerView.let {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(requireContext())
            val divider = DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.HORIZONTAL
            )
            it.addItemDecoration(divider)
            when (requireArguments().getInt(ARG_PAGE_TYPE)) {
                Type.FOLLOWER.ordinal -> it.adapter = (adapter as FollowersAdapter)
                Type.FOLLOWING.ordinal -> it.adapter = (adapter as FollowingAdapter)
            }
        }
    }

    private fun setupObservers() {
        val type = arguments?.getInt(ARG_PAGE_TYPE)
        val username = requireArguments().getString(ARG_USERNAME, resources.getString(R.string.default_user))
        when (type) {
            Type.FOLLOWER.ordinal -> {
                viewModel.followersObserver(viewLifecycleOwner, username) { data, status ->
                    onFollowersObserverFinish(data, status, username)
                    if (!data.isNullOrEmpty() && status == Resource.Status.SUCCESS)
                        viewModel.removeFollowersObserver(viewLifecycleOwner, username)
                }
            }
            Type.FOLLOWING.ordinal -> {
                viewModel.followingObserver(viewLifecycleOwner, username) { data, status ->
                    onFollowingObserverFinish(data, status, username)
                    if (!data.isNullOrEmpty() && status == Resource.Status.SUCCESS)
                        viewModel.removeFollowingObserver(viewLifecycleOwner, username)
                }
            }
        }
    }

    private fun onFollowersObserverFinish(
        data: List<FollowersResponse>?,
        status: Resource.Status,
        username: String
    ) {
        when (status) {
            Resource.Status.SUCCESS -> {
                if (!data.isNullOrEmpty()) (adapter as FollowersAdapter).setItems(ArrayList(data))
                else showNoData(username, Type.FOLLOWER)
                showLoading(false)
            }
            Resource.Status.ERROR -> {
                showError(true)
            }
            Resource.Status.LOADING -> {
                showLoading(true)
            }
        }
    }

    private fun onFollowingObserverFinish(
        data: List<FollowingResponse>?,
        status: Resource.Status,
        username: String
    ) {
        when (status) {
            Resource.Status.SUCCESS -> {
                if (!data.isNullOrEmpty()) (adapter as FollowingAdapter).setItems(ArrayList(data))
                else showNoData(username, Type.FOLLOWING)
                showLoading(false)
            }
            Resource.Status.ERROR -> {
                showError(true)
            }
            Resource.Status.LOADING -> {
                showLoading(true)
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.apply {
                loadingAnimation.visibility = View.VISIBLE
                loadingText.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                errorAnimation.visibility = View.GONE
                errorText.visibility = View.GONE
            }
        } else {
            binding.apply {
                loadingAnimation.visibility = View.GONE
                loadingText.visibility = View.GONE
                errorAnimation.visibility = View.GONE
                errorText.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }
    }

    private fun showError(state: Boolean) {
        if (state) {
            binding.apply {
                errorAnimation.visibility = View.VISIBLE
                errorText.visibility = View.VISIBLE
                loadingAnimation.visibility = View.GONE
                loadingText.visibility = View.GONE
                recyclerView.visibility = View.GONE
            }
        } else {
            binding.apply {
                errorAnimation.visibility = View.GONE
                errorText.visibility = View.GONE
                recyclerView.visibility = View.GONE
                loadingAnimation.visibility = View.VISIBLE
                loadingText.visibility = View.VISIBLE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showNoData(username: String, type: Type) {
        when (type) {
            Type.FOLLOWER -> {
                binding.nodataText.text = username + resources.getString(R.string.nodata_follower)
            }
            Type.FOLLOWING -> {
                binding.nodataText.text = username + resources.getString(R.string.nodata_following)
            }
        }

        binding.apply {
            recyclerView.visibility = View.GONE
            nodataAnimation.visibility = View.VISIBLE
            nodataText.visibility = View.VISIBLE
        }
    }
}
