package id.rosyid.exploregithubusers.ui.explore.users

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.rosyid.exploregithubusers.databinding.UsersFragmentBinding
import id.rosyid.exploregithubusers.utils.Resource
import id.rosyid.exploregithubusers.utils.autoCleared

@AndroidEntryPoint
class UsersFragment : Fragment(), UsersAdapter.UserItemListener {

    private var binding: UsersFragmentBinding by autoCleared()
    private val viewModel: UsersViewModel by viewModels()
    private lateinit var adapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UsersFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        adapter = UsersAdapter(this)
        binding.rvListUsers.let {
            it.layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, false
            )
            it.addItemDecoration(
                DividerItemDecoration(
                    requireContext(), DividerItemDecoration.VERTICAL
                )
            )
            it.adapter = adapter
        }
    }

    private fun setupObservers() {
        viewModel.users.observe(
            viewLifecycleOwner,
            {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        Log.d("OBSERVER", "setupObservers: Success: ${it.data?.size}")
                        if (!it.data.isNullOrEmpty()) adapter.setItems(ArrayList(it.data))
                    }
                    Resource.Status.ERROR -> {
                        Log.d("OBSERVER", "Error: ${it.message}")
                    }
                    Resource.Status.LOADING -> {
                    }
                }
            }
        )
    }

    override fun onClickedUser(username: String) {
    }
}
