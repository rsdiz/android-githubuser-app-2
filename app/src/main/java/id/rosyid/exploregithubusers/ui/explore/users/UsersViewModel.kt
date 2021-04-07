package id.rosyid.exploregithubusers.ui.explore.users

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.rosyid.exploregithubusers.data.entities.UserResponse
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
        bind: (List<UserResponse>?, Resource.Status, String?) -> Unit
    ) = users.observe(lifecycleOwner) { bind(it.data, it.status, it.message) }

    fun removeUsersObserver(
        lifecycleOwner: LifecycleOwner
    ) = users.removeObservers(lifecycleOwner)

    fun searchObserver(
        lifecycleOwner: LifecycleOwner,
        searchQuery: String,
        bind: (List<UserResponse>?, Resource.Status, String?) -> Unit
    ) = searchUsers(searchQuery).observe(lifecycleOwner) {
        bind(it.data?.listUsers, it.status, it.message)
    }

    fun removeSearchUsersObserver(
        lifecycleOwner: LifecycleOwner,
        searchQuery: String,
    ) = searchUsers(searchQuery).removeObservers(lifecycleOwner)
}
