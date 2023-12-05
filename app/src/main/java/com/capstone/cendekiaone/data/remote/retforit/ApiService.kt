package com.capstone.cendekiaone.data.remote.retforit

import com.capstone.cendekiaone.data.remote.response.DataResponse
import com.capstone.cendekiaone.data.remote.response.LoginResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    // Register a user with name, email, and password
//    @FormUrlEncoded
//    @POST("register")
//    fun register(
//        @Field("name") name: String,
//        @Field("email") email: String,
//        @Field("password") password: String
//    ): Call<DataResponse>

    // Just For Testing (start)
    @POST("register")
    fun register(@Body request: RegisterRequest): Call<DataResponse>

    data class RegisterRequest(
        val name: String,
        val email: String,
        val password: String
    )
    // Just For Testing (end)

//    // Log in a user with email and password
//    @FormUrlEncoded
//    @POST("login")
//    fun login(
//        @Field("email") email: String,
//        @Field("password") password: String
//    ): Call<LoginResponse>

    // Just For Testing (start)
    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    data class LoginRequest(
        val email: String,
        val password: String
    )
    // Just For Testing (end)
}