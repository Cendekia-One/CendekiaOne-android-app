package com.capstone.cendekiaone.ui.screen.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.cendekiaone.data.remote.response.DataResponse
import com.capstone.cendekiaone.data.remote.retforit.ApiService
import com.capstone.cendekiaone.utils.reduceFileImage
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

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

    fun editProfileWithPhoto(idUser: String, username: String, name: String, bio: String, file: File?) {
        if (file == null) {
            _editResult.value = EditResult.Error("Take picture image failed")
            return
        }

        val reducedFile = reduceFileImage(file)
        val idBody = idUser.toRequestBody("text/plain".toMediaType())
        val usernameBody = username.toRequestBody("text/plain".toMediaType())
        val nameBody = name.toRequestBody("text/plain".toMediaType())
        val bioBody = bio.toRequestBody("text/plain".toMediaType())
        val requestImageFile = reducedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "profileImage",
            reducedFile.name,
            requestImageFile
        )

        _isLoading.value = true
        apiService.updateProfileWithPhoto(idBody, usernameBody, nameBody, bioBody, imageMultipart).enqueue(object : Callback<DataResponse> {
            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody?.message == "User updated successfully") {
                    _editResult.value = EditResult.Success("User updated successfully")
                } else {
                    _editResult.value = EditResult.Error("User updated failed")
                }
            }

            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                _isLoading.value = false
                _editResult.value = EditResult.NetworkError
            }
        })
    }

    fun editProfile(idUser: String, username: String, name: String, bio: String) {
        val idBody = idUser.toRequestBody("text/plain".toMediaType())
        val usernameBody = username.toRequestBody("text/plain".toMediaType())
        val nameBody = name.toRequestBody("text/plain".toMediaType())
        val bioBody = bio.toRequestBody("text/plain".toMediaType())

        _isLoading.value = true
        apiService.updateProfile(idBody, usernameBody, nameBody, bioBody).enqueue(object : Callback<DataResponse> {
            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody?.message == "User updated successfully") {
                    _editResult.value = EditResult.Success("User updated successfully")
                } else {
                    _editResult.value = EditResult.Error("User updated failed")
                }
            }

            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                _isLoading.value = false
                _editResult.value = EditResult.NetworkError
            }
        })
    }

    fun resetEditResult() {
        _editResult.value = null
    }
}