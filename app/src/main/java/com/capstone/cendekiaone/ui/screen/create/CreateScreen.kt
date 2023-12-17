package com.capstone.cendekiaone.ui.screen.create

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.capstone.cendekiaone.R
import com.capstone.cendekiaone.ui.component.ButtonComponent
import com.capstone.cendekiaone.ui.component.OutlinedTextFieldComponent
import com.capstone.cendekiaone.ui.theme.myFont
import com.capstone.cendekiaone.utils.toBitmap
import com.capstone.cendekiaone.utils.uriToFile
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@Composable
fun CreateScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(16.dp)
            )
            .verticalScroll(rememberScrollState())
    ) {
        HeaderPost(
            username = "CendikiaOne",
        )
        MainPost()
        BottomPost()
    }
}

@Composable
fun HeaderPost(
    username: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(64.dp)
            .fillMaxWidth()
            .padding(end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.CenterVertically)
        ) {
            Image(
                painter = painterResource(id = R.drawable.placeholder),
                contentDescription = "Image Profile",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = username,
                    style = TextStyle(
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = myFont,
                        fontWeight = FontWeight.SemiBold
                    ),
                )
            }
        }
    }
}

@Composable
fun MainPost() {
    var getFile by remember { mutableStateOf<File?>(null) }
    val context = LocalContext.current
    var stateOfImage by remember {
        mutableStateOf<ImageBitmap?>(null)
    }
    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                // Convert the selected URI to a File
                getFile = uriToFile(uri, context)
                stateOfImage = getFile?.toBitmap() ?: ImageBitmap(1, 1)
            }
        }

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            // Image capture was successful, now you can use the capturedImageUri to access the image
            Toast.makeText(context, "Image captured successfully", Toast.LENGTH_SHORT).show()
            // Update capturedImageUri with the URI of the captured image
            capturedImageUri = uri
            // Additional logic to handle the captured image, e.g., save it or display it
        } else {
            Toast.makeText(context, "Image capture failed", Toast.LENGTH_SHORT).show()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        if (capturedImageUri.path?.isNotEmpty() == true) {
            Image(
                painter = rememberAsyncImagePainter(capturedImageUri),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                bitmap = stateOfImage ?: R.drawable.placeholder.toBitmap(context = context),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }

        Box(
            modifier = Modifier
                .background(Color.Transparent, shape = CircleShape)
                .padding(8.dp)
                .align(Alignment.BottomEnd)
        ) {
            Column {
                FilledIconButton(
                    onClick = {
                        val permissionCheckResult =
                            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

                        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                            cameraLauncher.launch(uri)
                        } else {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    val icon: Painter = painterResource(id = R.drawable.ic_camera_outline)
                    Icon(
                        painter = icon,
                        contentDescription = "Icon Comment",
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                FilledIconButton(
                    onClick = {
                        pickImageLauncher.launch("image/*")
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    val icon: Painter = painterResource(id = R.drawable.ic_gallery_outline)
                    Icon(
                        painter = icon,
                        contentDescription = "Icon Comment",
                    )
                }
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyy_MM_dd_HH:mm:ss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
}

@Composable
fun BottomPost() {
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextFieldComponent(
            provideText = stringResource(R.string.description),
            value = description,
            onValueChange = { description = it }
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextFieldComponent(
            provideText = stringResource(R.string.catergory_post),
            value = category,
            onValueChange = { category = it }
        )

        Spacer(modifier = Modifier.height(16.dp))
        ButtonComponent(
            provideText = stringResource(R.string.posting),
            modifier = Modifier.fillMaxWidth()
        ) { }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

fun File.toBitmap(): ImageBitmap {
    return try {
        BitmapFactory.decodeFile(this.path).asImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        ImageBitmap(1, 1) // Return a placeholder or an empty image in case of an error
    }
}

@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun CreateScreenPreview() {
    CreateScreen()
}