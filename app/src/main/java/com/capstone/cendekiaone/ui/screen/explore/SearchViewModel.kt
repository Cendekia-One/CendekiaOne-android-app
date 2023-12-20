package com.capstone.cendekiaone.ui.screen.explore

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.cendekiaone.data.remote.response.GetAllUserData
import com.capstone.cendekiaone.data.remote.response.User
import com.capstone.cendekiaone.data.remote.retforit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(private val apiService: ApiService) : ViewModel() {

    // LiveData to observe loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LiveData to observe search results
    private val _searchResults = MutableLiveData<List<User>>()
    val searchResults: LiveData<List<User>> = _searchResults

    fun searchByUsername(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                val response = apiService.searchByUsername(username)
                if (response.isSuccessful) {
                    _searchResults.postValue(response.body()?.data)
                } else {
                    Log.e(TAG, "API request failed with message: ${response.message()}")
                    // Handle the error appropriately, e.g., show an error message to the user
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception: ${e.message.toString()}")
                // Handle the exception appropriately, e.g., show an error message to the user
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    // Get all user
    private val _allUsers = MutableLiveData<List<GetAllUserData>>()
    val allUsers: LiveData<List<GetAllUserData>> = _allUsers

    suspend fun getAllUsers() {
        withContext(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                val response = apiService.getAllUsers()
                if (response.isSuccessful) {
                    _allUsers.postValue(response.body()?.data)
                } else {
                    Log.e(TAG, "API request failed with message: ${response.message()}")
                    // Handle the error appropriately, e.g., show an error message to the user
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception: ${e.message.toString()}")
                // Handle the exception appropriately, e.g., show an error message to the user
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}