package id.rosyid.exploregithubusers.data.repository

import id.rosyid.exploregithubusers.data.local.UserDao
import id.rosyid.exploregithubusers.data.remote.UserRemoteDataSource
import id.rosyid.exploregithubusers.utils.performGetOperation
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val localDataSource: UserDao
) {
    fun getAllUsers() = performGetOperation(
        databaseQuery = { localDataSource.getAllUsers() },
        networkCall = { userRemoteDataSource.getAllUsers() },
        saveCallResult = { localDataSource.insertAll(it) }
    )

    fun getUser(username: String) = performGetOperation(
        databaseQuery = { localDataSource.getUser(username) },
        networkCall = { userRemoteDataSource.getUser(username) },
        saveCallResult = { localDataSource.insert(it) }
    )

    fun searchUser(query: String) = performGetOperation(
        databaseQuery = { localDataSource.searchUser(query) },
        networkCall = { userRemoteDataSource.searchUser(query) },
        saveCallResult = { localDataSource.insertAll(it.listUsers) }
    )
}
