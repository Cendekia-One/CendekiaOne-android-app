package com.capstone.cendekiaone.ui.screen.create

import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.capstone.cendekiaone.R
import com.capstone.cendekiaone.data.helper.LocalViewModelFactory
import com.capstone.cendekiaone.ui.component.ButtonComponent
import com.capstone.cendekiaone.ui.component.OutlinedTextFieldComponent
import com.capstone.cendekiaone.ui.navigation.Screen
import com.capstone.cendekiaone.utils.toBitmap
import com.capstone.cendekiaone.utils.uriToFile
import java.io.File

@Composable
fun CreateScreen(
    modifier: Modifier = Modifier,
    createViewModel: CreateViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    navController: NavController,
) {
    var getFile by remember { mutableStateOf<File?>(null) }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

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
    val isLoading by createViewModel.isLoading.observeAsState(initial = false)

    // Observe the registration result from the ViewModel
    val uploadResult by createViewModel.uploadResult.observeAsState()

    Box(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(23.dp, Alignment.CenterVertically),
        ) {
            Image(
                bitmap = stateOfImage ?:
                R.drawable.placeholder.toBitmap(context = context),
                contentDescription = null,
                modifier = modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clickable { pickImageLauncher.launch("image/*") },
                contentScale = ContentScale.Crop,
            )
            OutlinedTextFieldComponent(
                provideText = stringResource(R.string.description),
                value = description,
                onValueChange = { description = it}
            )
            OutlinedTextFieldComponent(
                provideText = stringResource(R.string.catergory_post),
                value = category,
                onValueChange = { category = it }
            )
            ButtonComponent(
                provideText = stringResource(R.string.posting),
                modifier = Modifier.fillMaxWidth()
            ) {
                createViewModel.uploadStory(getFile, description)
            }
        }

        // Handle login result
        uploadResult?.let { result ->
            when (result) {
                is CreateViewModel.UploadResult.Success -> {
                    // Registration is successful, show Toast and navigate to LoginScreen
                    ShowToast(result.message)
                    navController.navigate(Screen.Home.route)
                    createViewModel.resetUploadResult()
                }
                is CreateViewModel.UploadResult.Error -> {
                    // Handle error if needed, show Toast
                    ShowToast(result.errorMessage)
                    createViewModel.resetUploadResult()
                }
                is CreateViewModel.UploadResult.NetworkError -> {
                    // Handle network error if needed, show Toast
                    ShowToast("Network Error")
                    createViewModel.resetUploadResult()
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

@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun CreateScreenPreview() {
    CreateScreen(navController = rememberNavController())
}