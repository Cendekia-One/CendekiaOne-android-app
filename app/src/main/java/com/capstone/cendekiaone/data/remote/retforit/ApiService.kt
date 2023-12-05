package com.capstone.cendekiaone.data.remote.retforit

import com.capstone.cendekiaone.data.remote.response.DataResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    // Register a user with name, email, and password
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<DataResponse>
}