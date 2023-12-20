package com.capstone.cendekiaone.data.remote.response

import com.google.gson.annotations.SerializedName

// Post Comment
data class PostCommentResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: PostCommentData,
)

data class PostCommentData(
    @SerializedName("username")
    val username: String,
    @SerializedName("comments")
    val comments: String,
)

// Get Comment
data class GetCommentResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: List<GetCommentData>,
    @SerializedName("pagination")
    val pagination: GetCommentPagination
)

data class GetCommentData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("comment_body")
    val commentBody: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("profilePicture")
    val profilePicture: String,
)

data class GetCommentPagination(
    @field:SerializedName("currentPage")
    val currentPage: Int,
    @field:SerializedName("totalPages")
    val totalPages: Int,
    @field:SerializedName("totalPosts")
    val totalPosts: Int,
)