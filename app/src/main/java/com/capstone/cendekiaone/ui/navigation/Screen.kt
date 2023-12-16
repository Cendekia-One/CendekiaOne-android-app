package com.capstone.cendekiaone.ui.navigation

sealed class Screen(val route: String) {
    object Intro : Screen("intro")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Create : Screen("create")
    object UploadImage : Screen("upload_image")
    object Explore : Screen("explore")
    object Search : Screen("search")
    object Profile : Screen("profile")
    object EditProfile : Screen("edit_profile")

    object Notification : Screen("notification")
    object Chat : Screen("chat")
}