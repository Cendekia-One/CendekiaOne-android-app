package com.capstone.cendekiaone.ui.screen.search

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.cendekiaone.data.remote.response.User
import com.capstone.cendekiaone.data.remote.retforit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchViewModel(private val apiService: ApiService) : ViewModel() {

    // LiveData to observe loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LiveData to observe search results
    private val _searchResults = MutableLiveData<List<User>>()
    val searchResults: LiveData<List<User>> = _searchResults

    suspend fun searchByUsername(username: String) {
        withContext(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                val response = apiService.searchByUsername(username)
                if (response.isSuccessful) {
                    _searchResults.postValue(response.body()?.data)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception: ${e.message.toString()}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}