package id.rosyid.exploregithubusers.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.rosyid.exploregithubusers.data.local.AppDatabase
import id.rosyid.exploregithubusers.data.local.UserDao
import id.rosyid.exploregithubusers.data.local.UserDetailDao
import id.rosyid.exploregithubusers.data.remote.GithubService
import id.rosyid.exploregithubusers.data.remote.UserDetailRemoteDataSource
import id.rosyid.exploregithubusers.data.remote.UserRemoteDataSource
import id.rosyid.exploregithubusers.data.repository.UserDetailRepository
import id.rosyid.exploregithubusers.data.repository.UserRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(GithubService.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideGithubService(retrofit: Retrofit): GithubService = retrofit.create(GithubService::class.java)

    @Singleton
    @Provides
    fun provideUserDetailRemoteDataSource(githubService: GithubService) = UserDetailRemoteDataSource(githubService)

    @Singleton
    @Provides
    fun provideUserRemoteDataSource(githubService: GithubService) = UserRemoteDataSource(githubService)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideUserDetailDao(database: AppDatabase) = database.userDetailDao()

    @Singleton
    @Provides
    fun provideUserDao(database: AppDatabase) = database.userDao()

    @Singleton
    @Provides
    fun provideUserDetailRepository(
        userDetailRemoteDataSource: UserDetailRemoteDataSource,
        localDataSource: UserDetailDao
    ) = UserDetailRepository(userDetailRemoteDataSource, localDataSource)

    @Singleton
    @Provides
    fun provideUserRepository(
        userRemoteDataSource: UserRemoteDataSource,
        localDataSource: UserDao
    ) = UserRepository(userRemoteDataSource, localDataSource)
}
