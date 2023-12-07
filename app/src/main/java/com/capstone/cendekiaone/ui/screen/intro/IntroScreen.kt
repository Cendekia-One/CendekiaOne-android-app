package com.capstone.cendekiaone.ui.screen.intro

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.capstone.cendekiaone.R
import com.capstone.cendekiaone.data.helper.LocalViewModelFactory
import com.capstone.cendekiaone.data.helper.UserRepository
import com.capstone.cendekiaone.data.pref.UserModel
import com.capstone.cendekiaone.ui.component.ButtonComponent
import com.capstone.cendekiaone.ui.navigation.Screen
import com.capstone.cendekiaone.ui.theme.myFont
import com.capstone.cendekiaone.ui.theme.myFont2

@Composable
fun IntroScreen(
    navController: NavController,
    userRepository: UserRepository = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    // Observe user login status with LaunchedEffect
    LaunchedEffect(userRepository) {
        userRepository.getUser().observeForever { user ->
            if (user != null && user.isLogin) {
                Log.d(TAG, "User is logged in. Navigating to HomeActivity.")
                navController.navigate(Screen.Home.route) {
                    // Pop up to the Home screen, removing all previous screens from the back stack
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                }
            }
        }
    }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = TextStyle(
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
                fontFamily = myFont2
            ),
        )
        Image(
            painter = painterResource(R.drawable.img_intro),
            contentDescription = "image_login",
            modifier = Modifier
                .size(350.dp)
                .align(Alignment.CenterHorizontally)
                .padding(top = 32.dp)
        )
        Text(
            text = stringResource(R.string.intro_desc_short),
            style = TextStyle(
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
                fontFamily = myFont2
            ),
            modifier = Modifier.padding(top = 32.dp)
        )
        Text(
            text = stringResource(R.string.intro_desc),
            style = TextStyle(
                textAlign = TextAlign.Center,
                fontFamily = myFont

            ),
            modifier = Modifier.padding(top = 16.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        ButtonComponent(
            provideText = stringResource(R.string.get_started),
            modifier = Modifier.fillMaxWidth(),
        ) { navController.navigate(Screen.Login.route) }
    }
}

@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun LoginScreenPreview() {
    IntroScreen(navController = rememberNavController())
}