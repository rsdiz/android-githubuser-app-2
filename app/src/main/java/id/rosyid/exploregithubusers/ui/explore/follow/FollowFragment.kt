package id.rosyid.exploregithubusers.ui.explore.follow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.rosyid.exploregithubusers.databinding.FollowFragmentBinding
import id.rosyid.exploregithubusers.ui.explore.users.UsersAdapter
import id.rosyid.exploregithubusers.utils.autoCleared

@AndroidEntryPoint
class FollowFragment : Fragment() {

    private var binding: FollowFragmentBinding by autoCleared()
    private val viewModel: FollowViewModel by viewModels()
    private lateinit var adapter: UsersAdapter

    enum class Type {
        FOLLOWER,
        FOLLOWING
    }

    companion object {
        private const val ARG_PAGE_TYPE = "page_type"

        @JvmStatic
        fun newInstance(type: Type) =
            FollowFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PAGE_TYPE, type.ordinal)
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
        binding.recyclerView.let {
            adapter = UsersAdapter()
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(requireContext())
            val divider = DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.HORIZONTAL
            )
            it.addItemDecoration(divider)
            it.adapter = adapter
        }
    }

    private fun setupObservers() {
    }
}
