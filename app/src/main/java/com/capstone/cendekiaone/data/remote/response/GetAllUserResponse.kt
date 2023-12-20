package com.capstone.cendekiaone.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetAllUserResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: List<GetAllUserData>,
)

data class GetAllUserData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("bio")
    val bio: String?,
    @SerializedName("profile_picture")
    val profilePicture: String?,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
)