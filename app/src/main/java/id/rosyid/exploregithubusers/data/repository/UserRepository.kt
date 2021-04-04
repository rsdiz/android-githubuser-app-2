package id.rosyid.exploregithubusers.data.repository

import id.rosyid.exploregithubusers.data.local.UserDao
import id.rosyid.exploregithubusers.data.remote.UserRemoteDataSource
import id.rosyid.exploregithubusers.utils.performGetOperation
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val localDataSource: UserDao
) {
    fun getUsers(username: String) = performGetOperation(
        databaseQuery = { localDataSource.getUser(username) },
        networkCall = { remoteDataSource.getUser(username) },
        saveCallResult = { localDataSource.insert(it) }
    )

    fun getRandomUsers() = performGetOperation(
        databaseQuery = { localDataSource.getAllUsers() },
        networkCall = { remoteDataSource.getRandomUsers() },
        saveCallResult = {}
    )
}
