package id.rosyid.exploregithubusers.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import id.rosyid.exploregithubusers.data.entities.UserResponse

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAllUsers(): LiveData<List<UserResponse>>

    @Query("SELECT * FROM user WHERE username = :username")
    fun getUser(username: String): LiveData<UserResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(listUser: List<UserResponse>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserResponse)

    @Query("SELECT * FROM user WHERE username LIKE :username")
    fun searchUser(username: String): LiveData<List<UserResponse>>

    @Query("DELETE FROM user")
    suspend fun deleteAllUsers()
}
