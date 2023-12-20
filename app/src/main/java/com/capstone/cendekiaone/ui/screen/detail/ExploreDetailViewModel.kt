package com.capstone.cendekiaone.ui.screen.detail

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.capstone.cendekiaone.data.remote.response.DataResponse
import com.capstone.cendekiaone.data.remote.response.PostCommentResponse
import com.capstone.cendekiaone.data.remote.response.PostDetailData
import com.capstone.cendekiaone.data.remote.response.PostSavedResponse
import com.capstone.cendekiaone.data.remote.retforit.ApiConfig
import com.capstone.cendekiaone.data.remote.retforit.ApiService
import com.capstone.cendekiaone.ui.screen.explore.ExplorePagingSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExploreDetailViewModel(
    val apiService: ApiService,
) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _postDetails = MutableLiveData<PostDetailData?>()
    val postDetails: LiveData<PostDetailData?> = _postDetails

    suspend fun loadPostDetails(id: String) {
        _isLoading.value = true
        try {
            val response = apiService.getDetailPosts(id)
            if (response.isSuccessful) {
                _postDetails.postValue(response.body()?.data)
            } else {
                Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Exception: ${e.message.toString()}")
        } finally {
            _isLoading.value = false
        }
    }

    // Save Post
    private val _savePost = MutableLiveData<SaveResult?>()
    val savePost: MutableLiveData<SaveResult?> = _savePost

    sealed class SaveResult {
        data class Success(val message: String) : SaveResult()
        data class Error(val errorMessage: String) : SaveResult()
        object NetworkError : SaveResult()
    }

    fun savePost(idPost: String, savedBy: String) {
        _isLoading.value = true
        apiService.savePost(idPost, savedBy).enqueue(object : Callback<PostSavedResponse> {
            override fun onResponse(call: Call<PostSavedResponse>, response: Response<PostSavedResponse>) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody?.message == "post saved") {
                    _savePost.value = SaveResult.Success("Post Saved")
                } else {
                    _savePost.value = SaveResult.Error("Post Already Saved")
                }
            }

            override fun onFailure(call: Call<PostSavedResponse>, t: Throwable) {
                _isLoading.value = false
                _savePost.value = SaveResult.NetworkError
            }
        })
    }

    fun resetSaveResult() {
        _savePost.value = null
    }

    // Likes post
    private val _likedPost = MutableLiveData<LikeResult?>()
    val likedPost: MutableLiveData<LikeResult?> = _likedPost

    sealed class LikeResult {
        data class Success(val message: String) : LikeResult()
        data class Error(val errorMessage: String) : LikeResult()
        object NetworkError : LikeResult()
    }

    fun likePost(idPost: String, likedBy: String) {
        _isLoading.value = true
        apiService.likePost(idPost, likedBy).enqueue(object : Callback<PostSavedResponse> {
            override fun onResponse(call: Call<PostSavedResponse>, response: Response<PostSavedResponse>) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody?.message == "successfully liked this post") {
                    _likedPost.value = LikeResult.Success(responseBody.message)
                } else {
                    if (responseBody != null) {
                        _likedPost.value = LikeResult.Error(responseBody.message)
                    }else{
                        _likedPost.value = LikeResult.Error("internet error")
                    }
                }
            }

            override fun onFailure(call: Call<PostSavedResponse>, t: Throwable) {
                _isLoading.value = false
                _likedPost.value = LikeResult.NetworkError
            }
        })
    }

    // Comment post
    private val _commentPost = MutableLiveData<CommentResult?>()
    val commentPost: MutableLiveData<CommentResult?> = _commentPost

    sealed class CommentResult {
        data class Success(val message: String) : CommentResult()
        data class Error(val errorMessage: String) : CommentResult()
        object NetworkError : CommentResult()
    }

    fun commentPost(idPost: String, commentBy: String , commentBody: String) {
        _isLoading.value = true
        apiService.commentPost(idPost, commentBy, commentBody).enqueue(object : Callback<PostCommentResponse> {
            override fun onResponse(call: Call<PostCommentResponse>, response: Response<PostCommentResponse>) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody?.status == "success") {
                    _commentPost.value = CommentResult.Success("Successfully Comment This Post")
                } else {
                    _commentPost.value = CommentResult.Error("Failed Comment This Post")
                }
            }

            override fun onFailure(call: Call<PostCommentResponse>, t: Throwable) {
                _isLoading.value = false
                _commentPost.value = CommentResult.NetworkError
            }
        })
    }

    // Get comment
    private var postId: String? = null

    // Set the postId
    fun setPostId(postId: String) {
        this.postId = postId
    }

    private val pager = Pager(
        PagingConfig(pageSize = PAGE_SIZE),
        pagingSourceFactory = { CommentPagingSource(ApiConfig.getApiService(), postId ?: "") }
    )

    val commentData = pager.flow.cachedIn(viewModelScope)

    companion object {
        private const val PAGE_SIZE = 10
    }

    // Delete Post
    private val _deletePost = MutableLiveData<DeleteResult?>()
    val deletePost: MutableLiveData<DeleteResult?> = _deletePost

    sealed class DeleteResult {
        data class Success(val message: String) : DeleteResult()
        data class Error(val errorMessage: String) : DeleteResult()
        object NetworkError : DeleteResult()
    }

     fun deletePost(idPost: String) {
        _isLoading.value = true
        apiService.deletePost(idPost).enqueue(object : Callback<DataResponse> {
            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody?.message == "delete post success") {
                    _deletePost.value = DeleteResult.Success("Delete Post Success")
                } else {
                    _deletePost.value = DeleteResult.Error("This Post Not Your")
                }
            }

            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                _isLoading.value = false
                _deletePost.value = DeleteResult.NetworkError
            }
        })
    }
}