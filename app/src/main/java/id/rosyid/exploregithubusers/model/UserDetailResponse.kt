package id.rosyid.exploregithubusers.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserDetailResponse(
    val name: String?,
    @SerializedName("login") @Expose val username: String,
    @SerializedName("avatar_url") @Expose val avatarUrl: String,
    val bio: String?,
    val location: String?,
    val company: String?,
    val blog: String?,
    val email: String?,
    @SerializedName("twitter_username") @Expose val twitterUsername: String?,
    @SerializedName("public_repos") @Expose val repositories: Int,
    val followers: Int,
    val following: Int
)
