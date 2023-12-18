package com.capstone.cendekiaone.ui.screen.profile

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.cendekiaone.data.pref.UserModel
import com.capstone.cendekiaone.data.remote.response.DataResponse
import com.capstone.cendekiaone.data.remote.response.LoginResponse
import com.capstone.cendekiaone.data.remote.retforit.ApiService
import com.capstone.cendekiaone.ui.screen.register.RegisterViewModel
import com.capstone.cendekiaone.utils.reduceFileImage
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    // Function to initiate the login process
//    fun editProfile(idUser: String, username: String, name: String, bio: String, file: File?) {
//        if (file == null) {
//            _editResult.value = EditResult.Error("Take picture image failed")
//            return
//        }
//
//        // Reduce the image file before uploading
//        val reducedFile = reduceFileImage(file)
//
//        // Create a request body for the image file
//        val requestImageFile = reducedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
//
//        // Create a MultipartBody.Part for the image file
//        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
//            "photo",
//            reducedFile.name,
//            requestImageFile
//        )
//
//        // Create MultipartBody.Part for the image file
//        val fileRequestBody = reducedFile.asRequestBody("image/*".toMediaTypeOrNull())
//        val imagePart = MultipartBody.Part.createFormData("file", reducedFile.name, fileRequestBody)
//
//        _isLoading.value = true
//        apiService.updateProfile(idUser, username, name, bio, imagePart).enqueue(object : Callback<DataResponse> {
//            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
//                _isLoading.value = false
//                val responseBody = response.body()
//                if (response.isSuccessful && responseBody?.message == "User updated successfully") {
//                    // Registration is successful
//                    _editResult.value = EditResult.Success("User updated successfully")
//                } else {
//                    // Registration failed
//                    _editResult.value = EditResult.Error("User updated failed")
//                }
//            }
//
//            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
//                _isLoading.value = false
//                // Network error
//                _editResult.value = EditResult.NetworkError
//            }
//        })
//    }

    fun editProfile(idUser: String, file: File?) {
        if (file == null) {
            _editResult.value = EditResult.Error("Take picture image failed")
            return
        }

        // Reduce the image file before uploading
        val reducedFile = reduceFileImage(file)

        // Create a request body for the description
        val idBody = idUser.toRequestBody("text/plain".toMediaType())

        // Create a request body for the image file
        val requestImageFile = reducedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())

        // Create a MultipartBody.Part for the image file
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            reducedFile.name,
            requestImageFile
        )

        _isLoading.value = true
        apiService.updateProfile(idBody, imageMultipart).enqueue(object : Callback<DataResponse> {
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