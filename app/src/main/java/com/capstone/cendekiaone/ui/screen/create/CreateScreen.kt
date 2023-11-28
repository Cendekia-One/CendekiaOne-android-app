package com.capstone.cendekiaone.ui.screen.create

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.capstone.cendekiaone.R
import com.capstone.cendekiaone.ui.component.ButtonComponent
import com.capstone.cendekiaone.ui.component.OutlinedTextFieldComponent
import com.capstone.cendekiaone.ui.navigation.Screen

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CreateScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(23.dp)
        ) {
            Image(
                modifier = modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.placholder_image),
                contentDescription = ""
            )
            OutlinedTextFieldComponent(
                provideText = stringResource(R.string.tulis_deskripsi),
            )
            OutlinedTextFieldComponent(
                provideText = stringResource(R.string.text_placeholder_example),
            )
            ButtonComponent(
                provideText = stringResource(R.string.posting),
                onClick = { navController.navigate(Screen.Home.route) }
            )
        }

    }
}

@Preview(showBackground = true, device = "id:pixel_4",)
@Composable
fun CreateScreenPreview() {
    CreateScreen(navController = rememberNavController())
}