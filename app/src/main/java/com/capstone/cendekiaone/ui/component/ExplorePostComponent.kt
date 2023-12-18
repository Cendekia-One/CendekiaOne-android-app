package com.capstone.cendekiaone.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun ExplorePostComponent(postPictureUrl: String) {
    val painter = rememberAsyncImagePainter(model = postPictureUrl)

    Image(
        painter = painter,
        contentDescription = "Post Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(140.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(16.dp)
            )
    )
}

@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun ExplorePostComponentPreview() {
    ExplorePostComponent("https://storage.googleapis.com/cendikiaone/1702881332004_75526.jpg")
}