package com.capstone.cendekiaone.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.capstone.cendekiaone.data.remote.retforit.ApiConfig
import com.capstone.cendekiaone.data.remote.retforit.ApiService
import com.capstone.cendekiaone.ui.screen.detail.CommentPagingSource

class HomeViewModel(
    val apiService: ApiService,
) : ViewModel() {
    private var idUser: String? = null

    // Set the userId
    fun setIdUser(idUser: String) {
        this.idUser = idUser
    }

    private val pager = Pager(
        PagingConfig(pageSize = PAGE_SIZE),
        pagingSourceFactory = { HomePagingSource(ApiConfig.getApiService(), idUser ?: "") }
    )

    val followedPostData = pager.flow.cachedIn(viewModelScope)

    companion object {
        private const val PAGE_SIZE = 10
    }
}