package com.capstone.cendekiaone.ui.screen.explore

import com.capstone.cendekiaone.data.remote.response.GetPostMidResponse
import com.capstone.cendekiaone.data.remote.retforit.ApiService

class PostRepository(private val apiService: ApiService) {

    suspend fun getAllPosts(): List<GetPostMidResponse>? {
        return try {
            val response = apiService.getAllPosts()
            if (response.isSuccessful) {
                response.body()?.data
            } else {
                null
            }
        } catch (e: Exception) {
            // Handle error, log or show a toast message
            null
        }
    }

    suspend fun getPostById(idPost: Int): GetPostMidResponse? {
        return getAllPosts()?.find { it.idPost == idPost }
    }
}