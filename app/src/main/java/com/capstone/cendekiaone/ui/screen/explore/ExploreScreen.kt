package com.capstone.cendekiaone.ui.screen.explore

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.capstone.cendekiaone.data.remote.response.GetPostMidResponse

@Composable
fun ExploreScreen() {
    WeaponsList(ExploreViewModel())
}

@Composable
fun ExploreScreen2(dataItem: GetPostMidResponse) {
    Box(
        Modifier
            .height(200.dp)
            .background(Color.Transparent)
            .padding(horizontal = 6.dp)
    ) {
        Image(
            contentScale = ContentScale.Inside,
            painter = rememberAsyncImagePainter(dataItem.postPicture),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center).fillMaxWidth()
        )
    }
}

@Composable
fun WeaponsList(viewModel: ExploreViewModel) {
    val weaponsData = viewModel.weaponsData.collectAsState()
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(count = weaponsData.value.size) {
            ExploreScreen2(weaponsData.value[it])
        }
    }
}