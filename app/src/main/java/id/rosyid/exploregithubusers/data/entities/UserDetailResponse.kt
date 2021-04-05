package id.rosyid.exploregithubusers.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user_detail")
data class UserDetailResponse(
    @PrimaryKey
    val id: Long,
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
