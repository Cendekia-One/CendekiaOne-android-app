package com.capstone.cendekiaone.data.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.cendekiaone.data.remote.retforit.ApiService
import com.capstone.cendekiaone.ui.screen.register.RegisterViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val apiService: ApiService,
    private val context: Context? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                if (context != null) {
                    RegisterViewModel(apiService) as T
                } else {
                    throw IllegalArgumentException("Context is required for RegisterViewModel")
                }
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
