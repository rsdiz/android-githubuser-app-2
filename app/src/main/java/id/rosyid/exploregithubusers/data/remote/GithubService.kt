package id.rosyid.exploregithubusers.data.remote

import id.rosyid.exploregithubusers.BuildConfig
import id.rosyid.exploregithubusers.data.entities.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {
    companion object {
        const val BASE_URL = "https://api.github.com/"
        const val TOKEN = BuildConfig.API_KEY
    }

    @GET("users")
    @Headers("Authorization: token $TOKEN")
    suspend fun getUser(): Response<ArrayList<UserResponse>>

    @GET("users/{username}")
    @Headers("Authorization: token $TOKEN")
    suspend fun getUser(@Path("username") username: String): Response<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token $TOKEN")
    suspend fun getUserDetail(@Path("username") username: String): Response<UserDetailResponse>

    @GET("users/{username}/following")
    @Headers("Authorization: token $TOKEN")
    suspend fun getFollowingUser(@Path("username") username: String): Response<ArrayList<FollowingResponse>>

    @GET("users/{username}/followers")
    @Headers("Authorization: token $TOKEN")
    suspend fun getFollowersUser(@Path("username") username: String): Response<ArrayList<FollowersResponse>>

    @GET("search/users")
    @Headers("Authorization: token $TOKEN")
    suspend fun searchUsers(@Query("q") query: String): Response<SearchUserResponse>
}
