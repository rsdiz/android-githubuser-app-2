package id.rosyid.exploregithubusers.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.rosyid.exploregithubusers.data.entities.UserDetailResponse

@Dao
interface UserDetailDao {
    @Query("SELECT * FROM user_detail WHERE username = :username")
    fun getUserDetail(username: String): LiveData<UserDetailResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userDetail: UserDetailResponse)
}
