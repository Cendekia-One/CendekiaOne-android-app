package com.capstone.cendekiaone.data.remote.retforit

import com.capstone.cendekiaone.data.remote.response.DataResponse
import com.capstone.cendekiaone.data.remote.response.LoginResponse
import com.capstone.cendekiaone.data.remote.response.UserDetail
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Log in a user with email and password
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    // Register a user with name, username, email, and password
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<DataResponse>

    // User detail
    @GET("user/{id_user}")
    suspend fun userDetail(@Path("id_user") userId: String): Response<UserDetail>
}