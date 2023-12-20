package com.capstone.cendekiaone.ui.screen.profile

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.capstone.cendekiaone.data.remote.response.DataUser
import com.capstone.cendekiaone.data.remote.retforit.ApiConfig
import com.capstone.cendekiaone.data.remote.retforit.ApiService
import com.capstone.cendekiaone.ui.screen.home.HomePagingSource
import kotlinx.coroutines.launch

class ProfileViewModel(val apiService: ApiService) : ViewModel() {

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

    // Get all my posts
    var tabIndex by mutableStateOf(0)
        private set

    var errorString : String = " "
    var loading: Boolean by mutableStateOf(false)

    fun tabIndex(index: Int){
        viewModelScope.launch {
            tabIndex = index
        }
    }

    private var idUser: String? = null

    fun setIdUser(idUser: String) {
        this.idUser = idUser
    }

    private val pager = Pager(
        PagingConfig(pageSize = PAGE_SIZE),
        pagingSourceFactory = { MyPostPagingSource(ApiConfig.getApiService(), idUser ?: "") }
    )

    val myPostData = pager.flow.cachedIn(viewModelScope)

    // Saved
    private val pagerSave = Pager(
        PagingConfig(pageSize = PAGE_SIZE),
        pagingSourceFactory = { MySavePagingSource(ApiConfig.getApiService(), idUser ?: "") }
    )

    val mySaveData = pagerSave.flow.cachedIn(viewModelScope)

    companion object {
        private const val PAGE_SIZE = 10
    }
}