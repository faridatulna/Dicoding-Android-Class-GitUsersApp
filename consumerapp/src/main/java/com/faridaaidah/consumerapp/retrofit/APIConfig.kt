package com.faridaaidah.consumerapp.retrofit

import com.faridaaidah.consumerapp.model.DetailUserModel
import com.faridaaidah.consumerapp.model.UserModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

class APIConfig {
    private fun getInterceptor(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    //set APIClient ambil data api dari base url
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(getInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getService(): Users = getRetrofit().create(Users::class.java)
}

//APIService
interface Users {
    //detail
    @GET("users/{username}")
    @Headers("Authorization: token d8f1963ca4c782a251e0b31b42cd09b511a131b5")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailUserModel>

    //search
    @GET("search/users")
    @Headers("Authorization: token d8f1963ca4c782a251e0b31b42cd09b511a131b5")
    fun getSearchUser(
        @Query("q") username: String
    ): Call<UserModel>

    //follower
    @GET("users/{username}/followers")
    @Headers("Authorization: token d8f1963ca4c782a251e0b31b42cd09b511a131b5")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<UserModel>>

    //following
    @GET("users/{username}/following")
    @Headers("Authorization: token d8f1963ca4c782a251e0b31b42cd09b511a131b5")
    fun getFollowings(
        @Path("username") username: String
    ): Call<List<UserModel>>
}