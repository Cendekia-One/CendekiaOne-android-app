package com.capstone.cendekiaone.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

val timeStamp: String = SimpleDateFormat(
    "yyyyMMdd_HHmmss",
    Locale.US
).format(System.currentTimeMillis())
private const val MAXIMAL_SIZE = 1000000 // max 1mb
fun reduceFileImage(file: File): File {
    val bitmap = BitmapFactory.decodeFile(file.path)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > MAXIMAL_SIZE)
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}

fun createTempFiles(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}
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