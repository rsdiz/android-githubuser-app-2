package id.rosyid.exploregithubusers.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.rosyid.exploregithubusers.data.entities.FollowersResponse

@Dao
interface FollowersDao {
    @Query("SELECT * FROM followers")
    fun getAllUsers(): LiveData<List<FollowersResponse>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(listUser: List<FollowersResponse>)

    @Query("DELETE FROM followers")
    suspend fun deleteAllUsers()
}
