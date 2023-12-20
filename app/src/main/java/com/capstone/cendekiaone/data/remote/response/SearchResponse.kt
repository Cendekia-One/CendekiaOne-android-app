package com.capstone.cendekiaone.data.remote.response

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @field:SerializedName("status")
    val status: String,
    @field:SerializedName("data")
    val data: List<User>
)

data class User(
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("username")
    val username: String,
    @field:SerializedName("bio")
    val bio: String?,
    @field:SerializedName("profile_picture")
    val profilePicture: String?,
    @field:SerializedName("createdAt")
    val createdAt: String,
    @field:SerializedName("updatedAt")
    val updatedAt: String
)

data class PostFollowResponse(
    @field:SerializedName("message")
    val message: String,
)