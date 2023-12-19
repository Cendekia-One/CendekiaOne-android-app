package com.capstone.cendekiaone.data.remote.retforit

import com.capstone.cendekiaone.data.remote.response.DataResponse
import com.capstone.cendekiaone.data.remote.response.GetPostResponse
import com.capstone.cendekiaone.data.remote.response.LoginResponse
import com.capstone.cendekiaone.data.remote.response.PostDetailResponse
import com.capstone.cendekiaone.data.remote.response.PostSavedResponse
import com.capstone.cendekiaone.data.remote.response.SearchResponse
import com.capstone.cendekiaone.data.remote.response.TopResponsePost
import com.capstone.cendekiaone.data.remote.response.UserDetail
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Log in a user with email and password
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    // Register a user with name, username, email, and password
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<DataResponse>

    // User detail
    @GET("user/{id_user}")
    suspend fun userDetail(@Path("id_user") userId: String): Response<UserDetail>

    // Edit profile
    @Multipart
    @POST("update-profile")
    fun updateProfileWithPhoto(
        @Part("id_user") idUser: RequestBody,
        @Part("username") username: RequestBody,
        @Part("name") name: RequestBody,
        @Part("bio") bio: RequestBody,
        @Part profileImage: MultipartBody.Part,
    ): Call<DataResponse>

    @Multipart
    @POST("update-profile")
    fun updateProfile(
        @Part("id_user") idUser: RequestBody,
        @Part("username") username: RequestBody,
        @Part("name") name: RequestBody,
        @Part("bio") bio: RequestBody,
    ): Call<DataResponse>

    // Post
    @Multipart
    @POST("post")
    fun post(
        @Part("id_user") idUser: RequestBody,
        @Part("post_title") postTitle: RequestBody,
        @Part postImage: MultipartBody.Part,
        @Part("post_body") postBody: RequestBody,
        @Part("categories") categories: RequestBody,
        @Part("sub_categories") subCategories: RequestBody,
    ): Call<TopResponsePost>

    // Get all posts
    @GET("posts")
    suspend fun getAllPosts(@Query("page") page: Int): Response<GetPostResponse>

    // Get post by id
    @GET("post/{id_post}")
    suspend fun getDetailPosts(@Path("id_post") userId: String): Response<PostDetailResponse>

    // Post save post
    @FormUrlEncoded
    @POST("saved")
    fun savePost(
        @Field("id_post") idPost: String,
        @Field("saved_by") savedBy: String,
    ): Call<PostSavedResponse>

    // Post save post
    @FormUrlEncoded
    @POST("like")
    fun likePost(
        @Field("post_id") idPost: String,
        @Field("liked_by") LikedBy: String,
    ): Call<PostSavedResponse>

    // Search user
    @GET("search")
    suspend fun searchByUsername(@Query("username") username: String): Response<SearchResponse>
}