package com.capstone.cendekiaone.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

@Composable
fun Int.toBitmap(context: Context): ImageBitmap {
    val drawableBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, this)
    return drawableBitmap.asImageBitmap()
}

fun Uri?.toBitmap(context: Context): ImageBitmap? {
    return if (this == null) {
        null
    } else {
        try {
            val contentResolver: ContentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(this)
            BitmapFactory.decodeStream(inputStream).asImageBitmap()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}