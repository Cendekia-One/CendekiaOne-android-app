@file:Suppress("NAME_SHADOWING")

package com.capstone.cendekiaone.ui.screen.profile

import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.capstone.cendekiaone.R
import com.capstone.cendekiaone.data.helper.LocalViewModelFactory
import com.capstone.cendekiaone.data.helper.UserRepository
import com.capstone.cendekiaone.ui.component.BaseCircleIconBox
import com.capstone.cendekiaone.ui.component.ButtonComponent
import com.capstone.cendekiaone.ui.component.OutlinedTextFieldComponent
import com.capstone.cendekiaone.ui.navigation.Screen
import com.capstone.cendekiaone.utils.toBitmap
import com.capstone.cendekiaone.utils.uriToFile
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfile(
    modifier: Modifier = Modifier,
    navController: NavController,
    editProfileViewModel: EditProfileViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    userRepository: UserRepository = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    profileViewModel: ProfileViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
) {
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

    // Observe the loading state from the ViewModel
    val isLoading by editProfileViewModel.isLoading.observeAsState(initial = false)

    // Observe the registration result from the ViewModel
    val editResult by editProfileViewModel.editResult.observeAsState()

    // Observe user details from the ViewModel
    val userDetails by profileViewModel.userDetails.observeAsState()

    LaunchedEffect(profileViewModel) {
        userRepository.getUser().observeForever { user ->
            if (user != null && user.isLogin) {
                Log.d("EditProfileScreen", "User Token Screen: ${user.token}")
                Log.d("EditProfileScreen", "User ID Screen: ${user.id}")

                launch {
                    profileViewModel.loadUserDetails(user.id)
                }
            }
        }
    }

    var isImageVisible by remember { mutableStateOf(false) }

    var username by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }

    userDetails?.let { userDetails ->
        // Update the username variable
        username = userDetails.username
        name = userDetails.name
        bio = userDetails.bio
    }

    // Handle registrations result
    editResult?.let { result ->
        when (result) {
            is EditProfileViewModel.EditResult.Success -> {
                // Registration is successful, show Toast and navigate to LoginScreen
                ShowToast(result.message)
                navController.navigate(Screen.Profile.route)
                // Reset the registration result to allow for future registrations
                editProfileViewModel.resetEditResult()
            }
            is EditProfileViewModel.EditResult.Error -> {
                // Handle error if needed, show Toast
                ShowToast(result.errorMessage)
                // Reset the registration result to allow for future registrations
                editProfileViewModel.resetEditResult()
            }
            is EditProfileViewModel.EditResult.NetworkError -> {
                // Handle network error if needed, show Toast
                ShowToast("Network Error")
                // Reset the registration result to allow for future registrations
                editProfileViewModel.resetEditResult()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.edit_profile),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate(Screen.Profile.route) },
                        modifier = modifier
                            .size(40.dp)
                    ) {
                        val icon: Painter = painterResource(id = R.drawable.ic_back)
                        Icon(
                            painter = icon,
                            contentDescription = "Icon Back",
                            tint = colorScheme.onBackground
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = innerPadding.calculateTopPadding(),
                    bottom = 16.dp
                )
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically)
        ) {
            Box {
                if (stateOfImage != null) {
                    Image(
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        bitmap = stateOfImage!!,
                        contentDescription = null
                    )
                } else {
                    Image(
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        painter = rememberAsyncImagePainter(model = userDetails?.profilePicture ?: R.drawable.placeholder),
                        contentDescription = null
                    )
                }
                Box(
                    modifier = Modifier
                        .background(colorScheme.surface, shape = CircleShape)
                        .padding(4.dp)
                        .align(Alignment.BottomEnd),
                ) {
                    BaseCircleIconBox(
                        enabled = true,
                        onClick = {
                            pickImageLauncher.launch("image/*")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = null,
                            tint = colorScheme.onPrimary,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextFieldComponent(
                    provideText = stringResource(R.string.enter_username),
                    icon = painterResource(R.drawable.ic_username_filled),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text
                    ),
                    value = username ?: "",
                    onValueChange = { username = it }
                )
                OutlinedTextFieldComponent(
                    provideText = stringResource(R.string.enter_name),
                    icon = painterResource(R.drawable.ic_name_filled),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text
                    ),
                    value = name ?: "",
                    onValueChange = { name = it }
                )
                OutlinedTextFieldComponent(
                    provideText = stringResource(R.string.enter_bio),
                    icon = painterResource(R.drawable.ic_bio_filled),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text
                    ),
                    value = bio ?: "",
                    onValueChange = { bio = it }
                )
                Spacer(modifier = Modifier.padding(top = 16.dp))
                ButtonComponent(
                    modifier = Modifier.fillMaxWidth(),
                    provideText = stringResource(R.string.btn_text_submit),
                ) {
                    userRepository.getUser().observeForever { user ->
                        if (user != null && user.isLogin && getFile != null) {
                            Log.d("EditProfileScreen", "User Token Screen: ${user.token}")
                            Log.d("EditProfileScreen", "User ID Screen: ${user.id}")

                            editProfileViewModel.editProfileWithPhoto(user.id, username, name, bio, getFile)
                        }
                        else if (user != null && user.isLogin) {
                            Log.d("EditProfileScreen", "User Token Screen: ${user.token}")
                            Log.d("EditProfileScreen", "User ID Screen: ${user.id}")

                            editProfileViewModel.editProfile(user.id, username, name, bio)
                        }
                    }
                }
            }

            // Loading indicator
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    strokeWidth = 5.dp
                )
            }
        }
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

@Composable
private fun ShowToast(message: String) {
    Toast.makeText(LocalContext.current, message, Toast.LENGTH_SHORT).show()
}

@Preview(showSystemUi = true)
@Composable
fun EditProfilePrev() {
    EditProfile(
        navController = rememberNavController()
    )
}