package id.rosyid.exploregithubusers.data.remote

import javax.inject.Inject

class UserDetailRemoteDataSource @Inject constructor(
    private val githubService: GithubService
) : BaseDataSource() {
    suspend fun getDetailUser(username: String) = getResult { githubService.getUserDetail(username) }
}
