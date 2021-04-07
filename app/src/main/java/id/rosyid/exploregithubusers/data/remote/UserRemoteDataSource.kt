package id.rosyid.exploregithubusers.data.remote

import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val githubService: GithubService
) : BaseDataSource() {
    suspend fun getAllUsers() = getResult { githubService.getUser() }
    suspend fun getUser(username: String) = getResult { githubService.getUser(username) }
    suspend fun searchUser(username: String) = getResult { githubService.searchUsers(username) }
    suspend fun getFollowersOfUser(username: String) = getResult { githubService.getFollowersUser(username) }
    suspend fun getFollowingOfUser(username: String) = getResult { githubService.getFollowingUser(username) }
}
