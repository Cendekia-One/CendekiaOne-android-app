package com.capstone.cendekiaone.data.remote.retforit

import com.capstone.cendekiaone.data.remote.response.DataResponse
import com.capstone.cendekiaone.data.remote.response.LoginResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    // Register a user with name, email, and password
    @POST("register")
    fun register(@Body request: RegisterRequest): Call<DataResponse>

    data class RegisterRequest(
        val name: String,
        val email: String,
        val password: String
    )

    // Log in a user with email and password
    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    data class LoginRequest(
        val email: String,
        val password: String
    )
}