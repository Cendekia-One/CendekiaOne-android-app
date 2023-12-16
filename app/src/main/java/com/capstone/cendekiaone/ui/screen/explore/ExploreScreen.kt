package com.capstone.cendekiaone.ui.screen.explore

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.capstone.cendekiaone.ui.component.SearchComponent
import com.capstone.cendekiaone.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { },
                scrollBehavior = scrollBehavior,
                actions = {
                    SearchComponent(
                        query = "Search User",
                        onQueryChange = { },
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

            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun ExploreScreenPreview() {
    val navController = rememberNavController()

    ExploreScreen(
        modifier = Modifier,
        navController = navController
    )
}