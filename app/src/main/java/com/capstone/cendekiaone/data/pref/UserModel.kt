package com.capstone.cendekiaone.data.pref

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val token: String,
    val id: String,
    val isLogin: Boolean
) : Parcelable