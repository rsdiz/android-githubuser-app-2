package id.rosyid.exploregithubusers.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.rosyid.exploregithubusers.data.entities.FollowingResponse

@Dao
interface FollowingDao {
    @Query("SELECT * FROM following")
    fun getAllUsers(): LiveData<List<FollowingResponse>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(listUser: List<FollowingResponse>)

    @Query("DELETE FROM following")
    suspend fun deleteAllUsers()
}
