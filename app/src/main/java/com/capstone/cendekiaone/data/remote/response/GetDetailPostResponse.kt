package com.capstone.cendekiaone.data.remote.response

import com.google.gson.annotations.SerializedName

data class PostDetailResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("data")
    val data: PostDetailData,
)

data class PostDetailData(
    @SerializedName("idPost")
    val idPost: Int,
    @SerializedName("createBy")
    val createBy: String,
    @SerializedName("profileCreator")
    val profileCreator: String,
    @SerializedName("postPicture")
    val postPicture: String,
    @SerializedName("postTitle")
    val postTitle: String,
    @SerializedName("postBody")
    val postBody: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("subCatergory")
    val subCatergory: String,
    @SerializedName("likes")
    val likes: Int,
    @SerializedName("comments")
    val comments: Int
)
