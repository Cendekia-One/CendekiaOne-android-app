package com.capstone.cendekiaone

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.capstone.cendekiaone.ui.screen.create.CreateScreen
import com.capstone.cendekiaone.ui.screen.home.HomeScreen
import com.capstone.cendekiaone.ui.screen.intro.IntroScreen
import com.capstone.cendekiaone.ui.screen.login.LoginScreen
import com.capstone.cendekiaone.ui.screen.profile.ProfileScreen
import com.capstone.cendekiaone.ui.screen.register.RegisterScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CendekiaOneApp(
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.Intro.route && currentRoute != Screen.Login.route && currentRoute != Screen.Register.route) {
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
                HomeScreen()
            }
            composable(Screen.Create.route) {
                CreateScreen()
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
        }
    }
}