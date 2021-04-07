package id.rosyid.exploregithubusers.ui.explore.users

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.rosyid.exploregithubusers.data.repository.UserRepository
import id.rosyid.exploregithubusers.utils.Resource
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    private val users = repository.getAllUsers()

    private fun searchUsers(query: String) = repository.searchUser(query)

    fun usersObserver(
        lifecycleOwner: LifecycleOwner,
        adapter: UsersAdapter
    ) {
        users.observe(lifecycleOwner) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    Log.d("OBSERVER", "setupObservers: Success: ${it.data?.size}")
                    if (!it.data.isNullOrEmpty()) adapter.setItems(ArrayList(it.data))
                    users.removeObservers(lifecycleOwner)
                }
                Resource.Status.ERROR -> {
                    Log.d("OBSERVER", "Error: ${it.message}")
                }
                Resource.Status.LOADING -> {
                    println("SEARCHING...")
                }
            }
        }
    }

    fun searchObserver(
        lifecycleOwner: LifecycleOwner,
        adapter: UsersAdapter,
        searchQuery: String
    ) {
        searchUsers(searchQuery).observe(lifecycleOwner) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    Log.d(
                        "RESULT_SEARCH",
                        "searchObservers: Success: ${it.data?.totalResults}"
                    )
                    adapter.setItems(it.data?.listUsers!!)
                    searchUsers(searchQuery).removeObservers(lifecycleOwner)
                }
                Resource.Status.ERROR -> {
                    Log.d("OBSERVER", "Error: ${it.message}")
                }
                Resource.Status.LOADING -> {
                    println("SEARCHING...")
                }
            }
        }
    }
}
