package com.capstone.cendekiaone.ui.screen.profile

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.cendekiaone.data.remote.response.DataUser
import com.capstone.cendekiaone.data.remote.retforit.ApiService

class ProfileViewModel(private val apiService: ApiService) : ViewModel() {

    // LiveData to observe loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LiveData to observe user details
    private val _userDetails = MutableLiveData<DataUser?>()
    val userDetails: LiveData<DataUser?> = _userDetails

    suspend fun loadUserDetails(id: String) {
        _isLoading.value = true
        try {
            val response = apiService.userDetail(id)
            if (response.isSuccessful) {
                _userDetails.value = response.body()?.data
            } else {
                Log.e(TAG, "onFailure: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message.toString()}")
        } finally {
            _isLoading.value = false
        }
    }
}