package id.rosyid.exploregithubusers.data.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SearchUserResponse(
    @SerializedName("total_count") @Expose val totalResults: Int,
    @SerializedName("items") @Expose val listUsers: ArrayList<UserResponse>
)
