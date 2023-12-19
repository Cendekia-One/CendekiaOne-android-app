package com.capstone.cendekiaone.ui.screen.explore

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.capstone.cendekiaone.data.remote.response.GetPostMidResponse
import com.capstone.cendekiaone.ui.navigation.Screen
import com.capstone.cendekiaone.ui.theme.Shapes

@Composable
fun ExploreScreen(navController: NavController) {
    WeaponsList(ExploreViewModel(), navController)
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
                navController.navigate(Screen.ExploreDetail.route + "/${item.idPost}")
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