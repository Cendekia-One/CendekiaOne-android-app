package com.capstone.cendekiaone.ui.screen.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.cendekiaone.data.remote.response.FollowData
import com.capstone.cendekiaone.data.remote.response.FollowResponse
import com.capstone.cendekiaone.data.remote.response.FollowingData
import com.capstone.cendekiaone.data.remote.response.FollowingResponse
import com.capstone.cendekiaone.data.remote.retforit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel(private val apiService: ApiService) : ViewModel() {

    private val _followerList = MutableLiveData<List<FollowData>>()
    val followerList: LiveData<List<FollowData>> get() = _followerList

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun getAllFollowers(idUser: String) {
        val call = apiService.getAllFollower(idUser)
        call.enqueue(object : Callback<FollowResponse> {
            override fun onResponse(call: Call<FollowResponse>, response: Response<FollowResponse>) {
                if (response.isSuccessful) {
                    val followResponse = response.body()
                    followResponse?.data?.let {
                        _followerList.postValue(it)
                    }
                } else {
                    _error.postValue("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<FollowResponse>, t: Throwable) {
                _error.postValue("Network Error: ${t.message}")
            }
        })
    }

    // Following
    private val _followingList = MutableLiveData<List<FollowingData>>()
    val followingList: LiveData<List<FollowingData>> get() = _followingList

    private val _errorFollowing = MutableLiveData<String>()
    val errorFollowing: LiveData<String> get() = _errorFollowing

    fun getAllFollowing(idUser: String) {
        val call = apiService.getAllFollowings(idUser)
        call.enqueue(object : Callback<FollowingResponse> {
            override fun onResponse(call: Call<FollowingResponse>, response: Response<FollowingResponse>) {
                if (response.isSuccessful) {
                    val followResponse = response.body()
                    followResponse?.data?.let {
                        _followingList.postValue(it)
                    }
                } else {
                    _errorFollowing.postValue("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<FollowingResponse>, t: Throwable) {
                _errorFollowing.postValue("Network Error: ${t.message}")
            }
        })
    }
}
