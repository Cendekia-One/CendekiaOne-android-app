package com.capstone.cendekiaone.ui.screen.explore

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.cendekiaone.data.remote.response.GetPostMidResponse
import com.capstone.cendekiaone.data.remote.response.GetPostResponse
import com.capstone.cendekiaone.data.remote.retforit.ApiConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class ExploreViewModel : ViewModel() {
    private val _weaponsData: MutableStateFlow<List<GetPostMidResponse>> = MutableStateFlow(listOf())
    val weaponsData: StateFlow<List<GetPostMidResponse>> = _weaponsData

    init{
        retrieveWeaponsData()
    }

    fun retrieveWeaponsData() {
        viewModelScope.launch {
            try {
                val response: Response<GetPostResponse> = ApiConfig.getApiService().getAllPosts()
                if (response.isSuccessful) {
                    val responseData: List<GetPostMidResponse>? = response.body()?.data
                    if(responseData != null){
                        _weaponsData.value = responseData.filter { true }
                    }
                } else {
                    // Handle error if needed
                    Log.e("ExploreViewModel", "Network Error: ${response.code()}")
                }
            } catch (e: Exception) {
                // Handle exception if needed
                Log.e("ExploreViewModel", "Error: ${e.message}")
            }
        }
    }
}