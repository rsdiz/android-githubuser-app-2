package id.rosyid.exploregithubusers.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user")
data class UserResponse(
    @PrimaryKey
    val id: Long,
    @SerializedName("avatar_url") @Expose val avatarUrl: String,
    @SerializedName("login") @Expose val username: String
)
