package id.rosyid.exploregithubusers.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "user")
@Parcelize
data class User(
    @PrimaryKey
    val id: Long,
    val username: String,
    val avatarUrl: String,
    var name: String = "",
    val company: String = "",
    val blog: String = "",
    val location: String = "",
    val email: String = "",
    val twitterUsername: String = "",
    val bio: String = "",
    val repositories: Int = 0,
    val followers: Int = 0,
    val following: Int = 0
) : Parcelable
