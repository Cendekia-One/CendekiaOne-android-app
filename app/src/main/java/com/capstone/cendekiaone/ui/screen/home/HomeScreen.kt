package com.capstone.cendekiaone.ui.screen.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.capstone.cendekiaone.R
import com.capstone.cendekiaone.ui.component.PostComponent
import com.capstone.cendekiaone.ui.component.TopAppComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
//    searchQuery: String,
//    onSearchQueryChanged: (String) -> Unit,
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { },
                scrollBehavior = scrollBehavior,
                actions = {
                    TopAppComponent(
                        query = "Search",
                        onQueryChange = {  },
                        navController = navController
                    )
                },
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = modifier
                .fillMaxSize()
        ) {
            item {
                PostComponent()
            }
        }
        BackHandler {
            // Perform any custom actions here, if necessary
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()

    HomeScreen(
        modifier = Modifier,
        navController = navController
    )
}