package com.capstone.cendekiaone.ui.screen.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.cendekiaone.data.remote.response.GetPostMidResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExploreDetailViewModel(private val repository: PostRepository) : ViewModel() {

    private val _postById = MutableLiveData<GetPostMidResponse?>()
    val postById: LiveData<GetPostMidResponse?> = _postById

    fun getPostById(idPost: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val post = repository.getPostById(idPost)
            _postById.postValue(post)
        }
    }
}
