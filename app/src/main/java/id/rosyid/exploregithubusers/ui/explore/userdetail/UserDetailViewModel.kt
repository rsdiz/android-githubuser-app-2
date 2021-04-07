package id.rosyid.exploregithubusers.ui.explore.userdetail

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.rosyid.exploregithubusers.data.entities.UserDetailResponse
import id.rosyid.exploregithubusers.data.repository.UserDetailRepository
import id.rosyid.exploregithubusers.utils.Resource
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val repository: UserDetailRepository
) : ViewModel() {
    private fun getUserDetail(username: String) = repository.getUserDetail(username)

    fun userDetailObserver(
        lifecycleOwner: LifecycleOwner,
        username: String,
        bind: (UserDetailResponse?, Resource.Status) -> Unit
    ) = getUserDetail(username).observe(lifecycleOwner) { bind(it.data, it.status) }

    fun removeUserDetailObserver(
        lifecycleOwner: LifecycleOwner,
        username: String
    ) = getUserDetail(username).removeObservers(lifecycleOwner)
}
