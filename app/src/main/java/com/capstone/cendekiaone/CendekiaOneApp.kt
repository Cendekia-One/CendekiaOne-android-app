package com.capstone.cendekiaone

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.capstone.cendekiaone.ui.navigation.Screen
import com.capstone.cendekiaone.ui.screen.intro.IntroScreen
import com.capstone.cendekiaone.ui.screen.login.LoginScreen
import com.capstone.cendekiaone.ui.screen.register.RegisterScreen

@Composable
fun CendekiaOneApp(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Intro.route,
    ) {
        composable(Screen.Intro.route) {
            IntroScreen(navController = navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController = navController)
        }
    }
}