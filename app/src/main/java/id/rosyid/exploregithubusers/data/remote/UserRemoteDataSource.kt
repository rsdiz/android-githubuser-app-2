package id.rosyid.exploregithubusers.data.remote

import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val githubService: GithubService
) : BaseDataSource() {
    suspend fun getRandomUsers() = getResult { githubService.getUser() }
    suspend fun getUser(username: String) = getResult { githubService.getUserDetail(username) }
}
