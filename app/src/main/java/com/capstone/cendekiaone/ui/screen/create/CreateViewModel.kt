package com.capstone.cendekiaone.ui.screen.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.cendekiaone.data.remote.response.DataResponse
import com.capstone.cendekiaone.data.remote.response.TopResponsePost
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
) : ViewModel() {
    // LiveData to observe loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LiveData to observe the login result
    private val _postResult = MutableLiveData<PostResult?>()
    val editResult: LiveData<PostResult?> = _postResult

    // Sealed class to represent different login results
    sealed class PostResult {
        data class Success(val message: String) : PostResult()
        data class Error(val errorMessage: String) : PostResult()
        object NetworkError : PostResult()
    }

    fun post(
        idUser: String,
        postTitle: String,
        file: File?,
        postBody: String,
        categories: String,
        subCategories: String
    ) {
        if (file == null) {
            _postResult.value = PostResult.Error("Take Picture Image Failed")
            return
        }

        val reducedFile = reduceFileImage(file)
        val idUser = idUser.toRequestBody("text/plain".toMediaType())
        val postTitle = postTitle.toRequestBody("text/plain".toMediaType())
        val requestImageFile = reducedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "postImage",
            reducedFile.name,
            requestImageFile
        )
        val postBody = postBody.toRequestBody("text/plain".toMediaType())
        val categories = categories.toRequestBody("text/plain".toMediaType())
        val subCategories = subCategories.toRequestBody("text/plain".toMediaType())

        _isLoading.value = true
        apiService.post(idUser, postTitle, imageMultipart, postBody, categories, subCategories)
            .enqueue(object : Callback<TopResponsePost> {
                override fun onResponse(
                    call: Call<TopResponsePost>,
                    response: Response<TopResponsePost>
                ) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody?.error == false) {
                        _postResult.value = PostResult.Success("Post Created")
                    } else {
                        _postResult.value = PostResult.Error("Post Created Failed")
                    }
                }

                override fun onFailure(call: Call<TopResponsePost>, t: Throwable) {
                    _isLoading.value = false
                    _postResult.value = PostResult.NetworkError
                }
            })
    }

    fun resetPostResult() {
        _postResult.value = null
    }
}