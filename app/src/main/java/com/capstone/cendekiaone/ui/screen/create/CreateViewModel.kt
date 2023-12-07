package com.capstone.cendekiaone.ui.screen.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.cendekiaone.data.helper.UserRepository
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

class CreateViewModel(
    private val apiService: ApiService,
    private val userRepository: UserRepository
) : ViewModel() {
    // LiveData to observe loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LiveData to observe the upload result
    private val _uploadResult = MutableLiveData<UploadResult?>()
    val uploadResult: LiveData<UploadResult?> = _uploadResult

    // Sealed class to represent different upload results
    sealed class UploadResult {
        data class Success(val message: String) : UploadResult()
        data class Error(val errorMessage: String) : UploadResult()
        object NetworkError : UploadResult()
    }

    // Function to initiate the upload process
    fun uploadStory(file: File?, description: String) {
        if (file == null) {
            _uploadResult.value = UploadResult.Error("Error get image file")
            return
        }

        _isLoading.value = true

        // Reduce the image file before uploading
        val reducedFile = reduceFileImage(file)

        // Create a request body for the description
        val descriptionBody = description.toRequestBody("text/plain".toMediaType())

        // Create a request body for the image file
        val requestImageFile = reducedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())

        // Create a MultipartBody.Part for the image file
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            reducedFile.name,
            requestImageFile
        )

        // Observe the user to get the authorization token
        userRepository.getUser().observeForever { user ->
            if (user != null) {
                // Construct the client for uploading the story
                val client = apiService.uploadStory("Bearer " + user.token, imageMultipart, descriptionBody)

                // Make the upload request
                client.enqueue(object : Callback<DataResponse> {
                    override fun onResponse(
                        call: Call<DataResponse>,
                        response: Response<DataResponse>
                    ) {
                        _isLoading.value = false
                        val responseBody = response.body()

                        if (response.isSuccessful && responseBody?.message == "Post created successfully") {
                            _uploadResult.value = UploadResult.Success(responseBody.message)
                        } else {
                            _uploadResult.value = UploadResult.Error("Post created failed")
                        }
                    }

                    override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                        _isLoading.value = false
                        _uploadResult.value = UploadResult.NetworkError
                    }
                })
            }
        }
    }

    fun resetUploadResult() {
        _uploadResult.value = null
    }
}