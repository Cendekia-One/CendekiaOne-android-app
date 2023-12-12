package com.capstone.cendekiaone.ui.screen.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.cendekiaone.data.helper.UserRepository
import com.capstone.cendekiaone.data.pref.UserModel
import com.capstone.cendekiaone.data.remote.response.DataResponse
import com.capstone.cendekiaone.data.remote.response.LoginResponse
import com.capstone.cendekiaone.data.remote.retforit.ApiService
import com.capstone.cendekiaone.ui.screen.register.RegisterViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileViewModel(
    private val apiService: ApiService,
) : ViewModel() {
    // LiveData to observe loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LiveData to observe the login result
    private val _editResult = MutableLiveData<EditResult?>()
    val editResult: LiveData<EditResult?> = _editResult

    // Sealed class to represent different login results
    sealed class EditResult {
        data class Success(val message: String) : EditResult()
        data class Error(val errorMessage: String) : EditResult()
        object NetworkError : EditResult()
    }

    // Function to initiate the login process
    fun editProfile(idUser: String, username: String, name: String, bio: String) {
        _isLoading.value = true
        apiService.updateProfile(idUser, username, name, bio).enqueue(object : Callback<DataResponse> {
            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody?.message == "User updated successfully") {
                    // Registration is successful
                    _editResult.value = EditResult.Success("User updated successfully")
                } else {
                    // Registration failed
                    _editResult.value = EditResult.Error("User updated failed")
                }
            }

            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                _isLoading.value = false
                // Network error
                _editResult.value = EditResult.NetworkError
            }
        })
    }

    fun resetEditResult() {
        _editResult.value = null
    }
}