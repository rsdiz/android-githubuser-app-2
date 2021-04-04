package id.rosyid.exploregithubusers.data.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserResponse(
    val id: Long,
    @SerializedName("avatar_url") @Expose val avatarUrl: String,
    @SerializedName("login") @Expose val username: String,
    val name: String
)
