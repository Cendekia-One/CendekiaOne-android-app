package com.capstone.cendekiaone.ui.screen.explore

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.capstone.cendekiaone.data.remote.response.GetPostMidResponse
import com.capstone.cendekiaone.data.remote.retforit.ApiService
import retrofit2.HttpException
import java.io.IOException

class ExplorePagingSource(private val apiService: ApiService) : PagingSource<Int, GetPostMidResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GetPostMidResponse> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = apiService.getAllPosts(page = nextPageNumber)

            if (response.isSuccessful) {
                val data = response.body()?.data ?: emptyList()
                LoadResult.Page(
                    data = data,
                    prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                    nextKey = if (data.isEmpty()) null else nextPageNumber + 1
                )
            } else {
                LoadResult.Error(Exception("Failed to load"))
            }
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GetPostMidResponse>): Int? {
        return state.anchorPosition
    }
}
