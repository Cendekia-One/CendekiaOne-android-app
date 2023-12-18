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

    @field:SerializedName("follower")
    val follower: String,

    @field:SerializedName("following")
    val following: String,

    @field:SerializedName("post")
    val post: String,

    @field:SerializedName("profile_picture")
    val profilePicture: String,
)

// Post
data class TopResponsePost(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: MidResponsePost
)

data class MidResponsePost(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("data")
    val data: BottomResponsePost
)

data class BottomResponsePost(
    @field:SerializedName("idPost")
    val idPost: Int,

    @field:SerializedName("createBy")
    val createBy: String,

    @field:SerializedName("postPicture")
    val postPicture: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("category")
    val category: String,

    @field:SerializedName("subCatergory")
    val subCatergory: String,

    @field:SerializedName("likes")
    val likes: String,

    @field:SerializedName("comments")
    val comments: String,

    @field:SerializedName("following")
    val following: Boolean,

    @field:SerializedName("saved")
    val saved: Boolean,

    @field:SerializedName("summary")
    val summary: Boolean,

    @field:SerializedName("createdAt")
    val createdAt: String,
)

// Get all posts
data class GetPostResponse(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("data")
    val data: List<GetPostMidResponse>,

    @field:SerializedName("pagination")
    val pagination: GetPostPagination
)

data class GetPostMidResponse(
    @field:SerializedName("idPost")
    val idPost: Int,

    @field:SerializedName("createBy")
    val createBy: String,

    @field:SerializedName("postPicture")
    val postPicture: String,

    @field:SerializedName("category")
    val category: String,

    @field:SerializedName("subCatergory")
    val subCatergory: String,

    @field:SerializedName("likes")
    val likes: String,

    @field:SerializedName("comments")
    val comments: String,

    @field:SerializedName("following")
    val following: Boolean,

    @field:SerializedName("saved")
    val saved: Boolean,

    @field:SerializedName("summary")
    val summary: Boolean,
)

data class GetPostPagination(
    @field:SerializedName("currentPage")
    val currentPage: Int,

    @field:SerializedName("totalPages")
    val totalPages: Int,

    @field:SerializedName("totalPosts")
    val totalPosts: Int,
)