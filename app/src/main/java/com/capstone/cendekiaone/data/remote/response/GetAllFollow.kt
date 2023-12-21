package com.capstone.cendekiaone.data.remote.response

import com.google.gson.annotations.SerializedName

data class FollowResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: List<FollowData>
)

data class FollowData(
    @SerializedName("id_user")
    val userId: Int,
    @SerializedName("following_id")
    val followingId: Int,
    @SerializedName("follower_name")
    val followerName: String,
    @SerializedName("profile_picture")
    val profilePicture: String,
    @SerializedName("follower_username")
    val followerUsername: String,
    @SerializedName("account_owner_name")
    val accountOwnerName: String,
    @SerializedName("account_owner_username")
    val accountOwnerUsername: String
)

data class FollowingResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: List<FollowingData>
)

data class FollowingData(
    @SerializedName("id_user")
    val userId: Int,
    @SerializedName("following_id")
    val followingId: Int,
    @SerializedName("following_name")
    val followingName: String,
    @SerializedName("profile_picture")
    val profilePicture: String,
    @SerializedName("following_username")
    val followingUsername: String,
    @SerializedName("account_owner_name")
    val accountOwnerName: String,
    @SerializedName("account_owner_username")
    val accountOwnerUsername: String
)
