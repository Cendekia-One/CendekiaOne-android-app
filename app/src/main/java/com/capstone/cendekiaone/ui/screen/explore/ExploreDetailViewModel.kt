package com.capstone.cendekiaone.ui.screen.explore

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.cendekiaone.data.remote.response.PostDetailData
import com.capstone.cendekiaone.data.remote.retforit.ApiService

class ExploreDetailViewModel(private val apiService: ApiService) : ViewModel() {
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
}