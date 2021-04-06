package id.rosyid.exploregithubusers.ui.explore.users

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.rosyid.exploregithubusers.R
import id.rosyid.exploregithubusers.databinding.UsersFragmentBinding
import id.rosyid.exploregithubusers.utils.autoCleared

@AndroidEntryPoint
class UsersFragment : Fragment(), UsersAdapter.UserItemListener, MenuItem.OnActionExpandListener {

    private var binding: UsersFragmentBinding by autoCleared()
    private val viewModel: UsersViewModel by viewModels()
    private lateinit var adapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UsersFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        adapter = UsersAdapter(this)
        setupObservers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvListUsers.let {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(requireContext())
            val divider = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            it.addItemDecoration(divider)
            it.adapter = adapter
        }
    }

    private fun setupObservers() {
        viewModel.usersObserver(viewLifecycleOwner, adapter)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.explore_menu, menu)

        val menuItem = menu.findItem(R.id.search)
        menuItem.setOnActionExpandListener(this)
        val searchView = menuItem?.actionView as SearchView

        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.isSubmitButtonEnabled = true

        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (!query.isNullOrBlank()) viewModel.searchObserver(viewLifecycleOwner, adapter, query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.isNullOrBlank() || newText.isNullOrEmpty()) {
                        setupObservers()
                        return true
                    }
                    return false
                }
            }
        )

        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.language_settings -> {
                val settingIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(settingIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        setupObservers()
        return true
    }
    override fun onMenuItemActionExpand(item: MenuItem?): Boolean = true

    override fun onClickedUser(username: String) {
    }
}
