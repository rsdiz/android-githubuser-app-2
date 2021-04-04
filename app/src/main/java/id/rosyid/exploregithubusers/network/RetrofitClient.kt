package id.rosyid.exploregithubusers.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var retrofit: Retrofit? = null

    val instance: GithubApi? by lazy {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(GithubApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        retrofit?.create(GithubApi::class.java)
    }
}
