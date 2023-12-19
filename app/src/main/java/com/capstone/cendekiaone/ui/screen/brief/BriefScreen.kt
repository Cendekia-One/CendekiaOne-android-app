package com.capstone.cendekiaone.ui.screen.brief

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstone.cendekiaone.R
import com.capstone.cendekiaone.data.helper.LocalViewModelFactory
import com.capstone.cendekiaone.ui.screen.profile.ProfileViewModel
import com.capstone.cendekiaone.ui.screen.search.SearchViewModel
import kotlinx.coroutines.launch

@Composable
fun BriefScreen(
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
) {
    // Observe user details from the ViewModel
    val userDetails by searchViewModel.searchResults.observeAsState()

    // Observe loading state from the ViewModel
    val isLoading by searchViewModel.isLoading.observeAsState(initial = false)

    // Load user details when the screen is created
    LaunchedEffect(searchViewModel) {
        launch {
            searchViewModel.searchByUsername("mamang")
        }
    }

    Log.d("Brief", "User Search: $userDetails")

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(stringResource(R.string.menu_brief))
    }
}

@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun BriefScreenPreview() {
    BriefScreen()
}