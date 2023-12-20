package com.capstone.cendekiaone.ui.screen.explore

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.capstone.cendekiaone.data.helper.LocalViewModelFactory
import com.capstone.cendekiaone.data.remote.response.GetPostMidResponse
import com.capstone.cendekiaone.ui.navigation.Screen
import com.capstone.cendekiaone.ui.theme.Shapes

@Composable
fun ExploreScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.padding(top = 75.dp)) {
            WeaponsList(ExploreViewModel(), navController)
        }
        SearchScreen()
    }
}

@Composable
fun ExploreScreen2(item: GetPostMidResponse, navController: NavController) {
    Image(
        painter = rememberAsyncImagePainter(item.postPicture),
        contentDescription = "Post Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(140.dp)
            .clip(Shapes.large)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                navController.navigate(Screen.ExploreDetail.createRoute(item.idPost, item.createBy))
            }
    )
}

@Composable
fun WeaponsList(viewModel: ExploreViewModel, navController: NavController) {
    val weaponsData: LazyPagingItems<GetPostMidResponse> =
        viewModel.weaponsData.collectAsLazyPagingItems()

    LazyVerticalGrid(
        columns = GridCells.Fixed(3)
    ) {
        items(weaponsData.itemCount) { index ->
            val item = weaponsData[index]
            if (item != null) {
                ExploreScreen2(item, navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    )
) {
    var text by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }

    val searchResults by searchViewModel.searchResults.observeAsState(emptyList())
    val allUsers by searchViewModel.allUsers.observeAsState(emptyList())
    DisposableEffect(text) {
        if (text.isNotBlank()) {
            searchViewModel.searchByUsername(text)
        }
        onDispose {}
    }

    Box(Modifier.fillMaxSize()) {
        // DockedSearchBar
        DockedSearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp),
            query = text,
            onQueryChange = { newText ->
                text = newText
            },
            onSearch = {
                active = false
            },
            active = active, // Set to true to show the search bar by default
            onActiveChange = { active = it },
            placeholder = { Text("Search User") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = { Icon(Icons.Default.MoreVert, contentDescription = null) },
            colors = SearchBarDefaults.colors(
                MaterialTheme.colorScheme.secondaryContainer,
                inputFieldColors = SearchBarDefaults.inputFieldColors(Color.Black),
            )
        ) {
            // Display search results as ListItem
            if (searchResults.isNotEmpty()) {
                LazyColumn {
                    items(searchResults) { user ->
                        ListItem(
                            headlineContent = { Text(user.username) },
                            supportingContent = { Text(user.bio ?: "") },
                            leadingContent = {
                                Image(
                                    painter = rememberAsyncImagePainter(user.profilePicture),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop,
                                )
                            },
                            colors = ListItemDefaults.colors(
                                MaterialTheme.colorScheme.secondaryContainer,
                            ),
                            modifier = Modifier
                                .clickable {
                                    // Handle click on the search result item
                                }
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                }
            } else {
                // Display all users as ListItem
                LazyColumn {
                    items(allUsers) { userData ->
                        ListItem(
                            headlineContent = { Text(userData.name) },
                            supportingContent = { Text(userData.bio ?: "") },
                            leadingContent = {
                                Image(
                                    painter = rememberAsyncImagePainter(userData.profilePicture),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop,
                                )
                            },
                            colors = ListItemDefaults.colors(
                                MaterialTheme.colorScheme.secondaryContainer,
                            ),
                            modifier = Modifier
                                .clickable {
                                    // Handle click on the user item
                                }
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}