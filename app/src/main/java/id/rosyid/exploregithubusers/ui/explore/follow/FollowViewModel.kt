package id.rosyid.exploregithubusers.ui.explore.follow

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.rosyid.exploregithubusers.data.entities.FollowersResponse
import id.rosyid.exploregithubusers.data.entities.FollowingResponse
import id.rosyid.exploregithubusers.data.repository.UserRepository
import id.rosyid.exploregithubusers.utils.Resource
import javax.inject.Inject

@HiltViewModel
class FollowViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    private fun getFollowersOfUser(username: String) = repository.getFollowersOfUser(username)
    private fun getFollowingOfUser(username: String) = repository.getFollowingOfUser(username)

    fun followersObserver(
        lifecycleOwner: LifecycleOwner,
        username: String,
        bind: (List<FollowersResponse>?, Resource.Status) -> Unit
    ) = getFollowersOfUser(username).observe(lifecycleOwner) { bind(it.data?.toList(), it.status) }

    fun followingObserver(
        lifecycleOwner: LifecycleOwner,
        username: String,
        bind: (List<FollowingResponse>?, Resource.Status) -> Unit
    ) = getFollowingOfUser(username).observe(lifecycleOwner) { bind(it.data?.toList(), it.status) }

    fun removeFollowersObserver(
        lifecycleOwner: LifecycleOwner,
        username: String
    ) = getFollowersOfUser(username).removeObservers(lifecycleOwner)

    fun removeFollowingObserver(
        lifecycleOwner: LifecycleOwner,
        username: String
    ) = getFollowingOfUser(username).removeObservers(lifecycleOwner)
}
