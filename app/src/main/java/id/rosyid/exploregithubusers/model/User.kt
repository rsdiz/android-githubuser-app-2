package id.rosyid.exploregithubusers.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
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
