package id.rosyid.exploregithubusers.data.repository

import id.rosyid.exploregithubusers.data.local.UserDetailDao
import id.rosyid.exploregithubusers.data.remote.UserDetailRemoteDataSource
import id.rosyid.exploregithubusers.utils.performGetOperation
import javax.inject.Inject

class UserDetailRepository @Inject constructor(
    private val userDetailRemoteDataSource: UserDetailRemoteDataSource,
    private val localDataSource: UserDetailDao
) {
    fun getUserDetail(username: String) = performGetOperation(
        databaseQuery = { localDataSource.getUserDetail(username) },
        networkCall = { userDetailRemoteDataSource.getUserDetail(username) },
        saveCallResult = { localDataSource.insert(it) }
    )
}
