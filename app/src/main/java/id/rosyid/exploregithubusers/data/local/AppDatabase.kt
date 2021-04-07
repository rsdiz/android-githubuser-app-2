package id.rosyid.exploregithubusers.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.rosyid.exploregithubusers.data.entities.FollowersResponse
import id.rosyid.exploregithubusers.data.entities.FollowingResponse
import id.rosyid.exploregithubusers.data.entities.UserDetailResponse
import id.rosyid.exploregithubusers.data.entities.UserResponse

@Database(entities = [UserDetailResponse::class, UserResponse::class, FollowersResponse::class, FollowingResponse::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDetailDao(): UserDetailDao
    abstract fun userDao(): UserDao
    abstract fun followersDao(): FollowersDao
    abstract fun followingDao(): FollowingDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "explore_github")
                .fallbackToDestructiveMigration()
                .build()
    }
}
