package com.capstone.cendekiaone.ui.screen.create

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.capstone.cendekiaone.R
import com.capstone.cendekiaone.data.helper.LocalViewModelFactory
import com.capstone.cendekiaone.data.helper.UserRepository
import com.capstone.cendekiaone.ui.component.ButtonComponent
import com.capstone.cendekiaone.ui.component.OutlinedTextFieldComponent
import com.capstone.cendekiaone.ui.navigation.Screen
import com.capstone.cendekiaone.ui.screen.profile.ProfileViewModel
import com.capstone.cendekiaone.ui.theme.myFont
import com.capstone.cendekiaone.utils.toBitmap
import com.capstone.cendekiaone.utils.uriToFile
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@Composable
fun CreateScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
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
        HeaderPost()
        MainPost(navController = navController)
    }
}

@Composable
fun HeaderPost(
    modifier: Modifier = Modifier,
    userRepository: UserRepository = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    profileViewModel: ProfileViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
) {
    // Observe user details from the ViewModel
    val userDetails by profileViewModel.userDetails.observeAsState()

    // Load user details when the screen is created
    LaunchedEffect(profileViewModel) {
        userRepository.getUser().observeForever { user ->
            if (user != null && user.isLogin) {
                Log.d("ProfileScreen", "User Token Screen: ${user.token}")
                Log.d("ProfileScreen", "User ID Screen: ${user.id}")

                launch {
                    profileViewModel.loadUserDetails(user.id)
                }
            }
        }
    }

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
                contentScale = ContentScale.Crop,
                painter = rememberAsyncImagePainter(model = userDetails?.profilePicture ?: R.drawable.placeholder),
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
                    text = userDetails?.username ?: "",
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
fun MainPost(
    createViewModel: CreateViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    userRepository: UserRepository = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    navController: NavController,
) {
    val isLoading by createViewModel.isLoading.observeAsState(initial = false)
    val postResult by createViewModel.editResult.observeAsState()

    var getFile by remember { mutableStateOf<File?>(null) }
    val context = LocalContext.current
    var stateOfImage by remember {
        mutableStateOf<ImageBitmap?>(null)
    }
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    var isFromGallery by remember { mutableStateOf(false) }

    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                // Convert the selected URI to a File
                getFile = uriToFile(uri, context)
                stateOfImage = getFile?.toBitmap() ?: ImageBitmap(1, 1)
                isFromGallery = true
            }
        }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (uri != null) {
            // Convert the selected URI to a File
            getFile = uriToFile(uri, context)
            stateOfImage = getFile?.toBitmap() ?: ImageBitmap(1, 1)
            isFromGallery = false
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

    postResult?.let { result ->
        when (result) {
            is CreateViewModel.PostResult.Success -> {
                // Registration is successful, show Toast and navigate to LoginScreen
                ShowToast(result.message)
                navController.navigate(Screen.Home.route)
                // Reset the registration result to allow for future registrations
                createViewModel.resetPostResult()
            }
            is CreateViewModel.PostResult.Error -> {
                // Handle error if needed, show Toast
                ShowToast(result.errorMessage)
                // Reset the registration result to allow for future registrations
                createViewModel.resetPostResult()
            }
            is CreateViewModel.PostResult.NetworkError -> {
                // Handle network error if needed, show Toast
                ShowToast("Network Error")
                // Reset the registration result to allow for future registrations
                createViewModel.resetPostResult()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        Image(
            bitmap = stateOfImage?.let {
                if (isFromGallery) {
                    it
                } else {
                    it.rotate(90f)
                }
            } ?: R.drawable.placeholder.toBitmap(context = context),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

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
        // Loading indicator
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(50.dp),
                strokeWidth = 5.dp
            )
        }
    }

    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var subCategory by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextFieldComponent(
            provideText = stringResource(R.string.description),
            value = description,
            onValueChange = { description = it },
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextFieldComponent(
            provideText = stringResource(R.string.catergory_post),
            value = category,
            onValueChange = { category = it },
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextFieldComponent(
            provideText = stringResource(R.string.subcatergory_post),
            value = subCategory,
            onValueChange = { subCategory = it },
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(16.dp))
        ButtonComponent(
            provideText = stringResource(R.string.posting),
            modifier = Modifier.fillMaxWidth()
        ) {
            userRepository.getUser().observeForever { user ->
                if (user != null && user.isLogin) {
                    Log.d("CreateScreen", "User ID Screen: ${user.id}")

                    createViewModel.post(user.id, description, getFile, description, category, subCategory)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

fun ImageBitmap.rotate(degrees: Float): ImageBitmap {
    val matrix = Matrix()
    matrix.postRotate(degrees)
    return Bitmap.createBitmap(this.asAndroidBitmap(), 0, 0, width, height, matrix, true).asImageBitmap()
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
private fun ShowToast(message: String) {
    Toast.makeText(LocalContext.current, message, Toast.LENGTH_SHORT).show()
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
    CreateScreen(navController = rememberNavController())
}