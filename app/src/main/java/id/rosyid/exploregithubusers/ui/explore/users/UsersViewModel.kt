package id.rosyid.exploregithubusers.ui.explore.users

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.rosyid.exploregithubusers.data.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    repository: UserRepository
) : ViewModel() {
    val users = repository.getAllUsers()
}
