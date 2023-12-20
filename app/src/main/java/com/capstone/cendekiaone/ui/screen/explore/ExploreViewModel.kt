package com.capstone.cendekiaone.ui.screen.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.capstone.cendekiaone.data.remote.response.GetPostMidResponse
import com.capstone.cendekiaone.data.remote.retforit.ApiConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ExploreViewModel : ViewModel() {

    private val pager = Pager(
        PagingConfig(pageSize = PAGE_SIZE),
        pagingSourceFactory = { ExplorePagingSource(ApiConfig.getApiService()) }
    )

    val weaponsData = pager.flow.cachedIn(viewModelScope)

    companion object {
        private const val PAGE_SIZE = 10
    }
}