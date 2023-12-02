package com.capstone.cendekiaone.ui.data

data class UserProfile(
    val name: String,
    val username: String,
    val bio: String
)

val userProfile = UserProfile(
    name = "Cendekia One",
    username = "cendekia_one",
    bio = "Lorem ipsum dolor sit  amet,consectur"
)