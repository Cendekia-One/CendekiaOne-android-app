package com.capstone.cendekiaone.data.remote.response

import com.google.gson.annotations.SerializedName

data class DataResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

// login response
data class LoginResponse(
    @field:SerializedName("data")
    val loginResult: LoginResult,

    @field:SerializedName("status")
    val status: String
)

data class LoginResult(
    @field:SerializedName("token")
    val token: String,

    @field:SerializedName("id_user")
    val userId: String,

    @field:SerializedName("username")
    val username: String
)

data class UserDetail(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("data")
    val data: DataUser,
)

data class DataUser(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("bio")
    val bio: String,

    @field:SerializedName("profile_picture")
    val profilePicture: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String,
)