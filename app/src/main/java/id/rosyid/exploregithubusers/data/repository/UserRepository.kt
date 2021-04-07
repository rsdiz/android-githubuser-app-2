package id.rosyid.exploregithubusers.data.repository

import id.rosyid.exploregithubusers.data.local.FollowersDao
import id.rosyid.exploregithubusers.data.local.FollowingDao
import id.rosyid.exploregithubusers.data.local.UserDao
import id.rosyid.exploregithubusers.data.remote.UserRemoteDataSource
import id.rosyid.exploregithubusers.utils.performGetOperation
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserDao,
    private val followersLocalDataSource: FollowersDao,
    private val followingLocalDataSource: FollowingDao
) {
    fun getAllUsers() = performGetOperation(
        databaseQuery = { userLocalDataSource.getAllUsers() },
        networkCall = { userRemoteDataSource.getAllUsers() },
        saveCallResult = { userLocalDataSource.insertAll(it) }
    )

    fun getUser(username: String) = performGetOperation(
        databaseQuery = { userLocalDataSource.getUser(username) },
        networkCall = { userRemoteDataSource.getUser(username) },
        saveCallResult = { userLocalDataSource.insert(it) }
    )

    fun searchUser(query: String) = performGetOperation(
        networkCall = { userRemoteDataSource.searchUser(query) },
        saveCallResult = { userLocalDataSource.insertAll(it.listUsers) }
    )

    fun getFollowersOfUser(username: String) = performGetOperation(
        networkCall = { userRemoteDataSource.getFollowersOfUser(username) },
        saveCallResult = {
            followersLocalDataSource.run {
                deleteAllUsers()
                insertAll(it)
            }
        }
    )

    fun getFollowingOfUser(username: String) = performGetOperation(
        networkCall = { userRemoteDataSource.getFollowingOfUser(username) },
        saveCallResult = {
            followingLocalDataSource.run {
                deleteAllUsers()
                insertAll(it)
            }
        }
    )
}
