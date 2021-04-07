package id.rosyid.exploregithubusers.ui.explore.users

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.rosyid.exploregithubusers.R
import id.rosyid.exploregithubusers.databinding.UsersFragmentBinding
import id.rosyid.exploregithubusers.utils.Resource
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

        val baseActivity = activity as AppCompatActivity
        val navHostFragment: NavHostFragment =
            baseActivity.supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        baseActivity.setSupportActionBar(binding.toolbar)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        binding.rvListUsers.let {
            adapter = UsersAdapter(this)
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(requireContext())
            val divider = DividerItemDecoration(
                it.context,
                LinearLayoutManager.HORIZONTAL
            )
            it.addItemDecoration(divider)
            it.adapter = adapter
        }
    }

    private fun setupObservers() {
        viewModel.usersObserver(viewLifecycleOwner) { data, status, _ ->
            when (status) {
                Resource.Status.SUCCESS -> {
                    Log.d("OBSERVER", "setupObservers: Success: ${data?.size}")
                    if (!data.isNullOrEmpty()) adapter.setItems(ArrayList(data))
                    showLoading(false)
                    viewModel.removeUsersObserver(viewLifecycleOwner)
                }
                Resource.Status.ERROR -> {
                    showError(true)
                }
                Resource.Status.LOADING -> {
                    showLoading(true)
                }
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.apply {
                layoutLoading.visibility = View.VISIBLE
                rvListUsers.visibility = View.GONE
                layoutError.visibility = View.GONE
            }
        } else {
            binding.apply {
                layoutLoading.visibility = View.GONE
                layoutError.visibility = View.GONE
                rvListUsers.visibility = View.VISIBLE
            }
        }
    }

    private fun showError(state: Boolean) {
        if (state) {
            binding.apply {
                layoutError.visibility = View.VISIBLE
                layoutLoading.visibility = View.GONE
                rvListUsers.visibility = View.GONE
            }
        } else {
            binding.apply {
                layoutError.visibility = View.GONE
                rvListUsers.visibility = View.GONE
                layoutLoading.visibility = View.VISIBLE
            }
        }
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
                    if (!query.isNullOrBlank())
                        viewModel.searchObserver(viewLifecycleOwner, query) { data, status, _ ->
                            when (status) {
                                Resource.Status.SUCCESS -> {
                                    Log.d(
                                        "RESULT_SEARCH",
                                        "searchObservers: Success: ${data?.size}"
                                    )
                                    adapter.setItems(ArrayList(data!!))
                                    showLoading(false)
                                    viewModel.removeSearchUsersObserver(viewLifecycleOwner, query)
                                }
                                Resource.Status.ERROR -> {
                                    showError(true)
                                }
                                Resource.Status.LOADING -> {
                                    showLoading(true)
                                }
                            }
                        }
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
        val toDetail = UsersFragmentDirections.actionUsersFragmentToUserDetailFragment(username)
        view?.findNavController()?.navigate(toDetail)
    }
}
