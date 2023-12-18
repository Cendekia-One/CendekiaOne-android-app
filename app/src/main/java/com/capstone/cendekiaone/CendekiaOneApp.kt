package com.capstone.cendekiaone

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.capstone.cendekiaone.ui.component.NavigationBarComponent
import com.capstone.cendekiaone.ui.navigation.Screen
import com.capstone.cendekiaone.ui.screen.brief.BriefScreen
import com.capstone.cendekiaone.ui.screen.camera.CameraScreen
import com.capstone.cendekiaone.ui.screen.chat.ChatScreen
import com.capstone.cendekiaone.ui.screen.create.CreateScreen
import com.capstone.cendekiaone.ui.screen.create.UploadImageScreen
import com.capstone.cendekiaone.ui.screen.explore.ExploreScreen
import com.capstone.cendekiaone.ui.screen.explore.SearchScreen
import com.capstone.cendekiaone.ui.screen.home.HomeScreen
import com.capstone.cendekiaone.ui.screen.intro.IntroScreen
import com.capstone.cendekiaone.ui.screen.login.LoginScreen
import com.capstone.cendekiaone.ui.screen.notification.NotificationScreen
import com.capstone.cendekiaone.ui.screen.profile.EditProfile
import com.capstone.cendekiaone.ui.screen.profile.ProfileScreen
import com.capstone.cendekiaone.ui.screen.register.RegisterScreen

@Composable
fun CendekiaOneApp(
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute == Screen.Home.route || currentRoute == Screen.Create.route || currentRoute == Screen.Profile.route ||
                currentRoute == Screen.Explore.route || currentRoute == Screen.Brief.route) {
                NavigationBarComponent(navController)
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Intro.route,
            modifier = Modifier.padding(innerPadding)
        )
        {
            composable(Screen.Intro.route) {
                IntroScreen(navController = navController)
            }
            composable(Screen.Login.route) {
                LoginScreen(navController = navController)
            }
            composable(Screen.Register.route) {
                RegisterScreen(navController = navController)
            }
            composable(Screen.Home.route) {
                HomeScreen(navController = navController)
            }
            composable(Screen.Explore.route) {
                ExploreScreen()
            }
            composable(Screen.Search.route) {
                SearchScreen()
            }
            composable(Screen.Brief.route) {
                BriefScreen()
            }
            composable(Screen.Camera.route) {
                CameraScreen()
            }
            composable(Screen.Create.route) {
                CreateScreen(navController = navController)
            }
            composable(Screen.UploadImage.route) {
                UploadImageScreen(navController = navController)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(navController = navController)
            }
            composable(Screen.EditProfile.route) {
                EditProfile(navController = navController)
            }
            composable(Screen.Notification.route) {
                NotificationScreen(navController = navController)
            }
            composable(Screen.Chat.route) {
                ChatScreen(navController = navController)
            }
        }
    }
}