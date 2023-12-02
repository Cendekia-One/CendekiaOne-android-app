package com.capstone.cendekiaone.ui.screen.profile

import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.capstone.cendekiaone.R
import com.capstone.cendekiaone.ui.component.BaseCircleIconBox
import com.capstone.cendekiaone.ui.component.ButtonComponent
import com.capstone.cendekiaone.ui.component.OutlinedTextFieldComponent
import com.capstone.cendekiaone.ui.navigation.Screen
import com.capstone.cendekiaone.utils.toBitmap

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun EditProfile(
    modifier: Modifier = Modifier,
    navController: NavController,
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val context = LocalContext.current
    var stateOfProfileImage by remember {
        mutableStateOf<ImageBitmap?>(null)
    }
    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                // TODO: Upload to server
                stateOfProfileImage = uri.toBitmap(context)
            }
        }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
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
                            tint = MaterialTheme.colorScheme.onBackground
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
            Box{
                //dipake klo udh ada api image
//                GlideImage(
//                    modifier = Modifier
//                        .size(90.dp)
//                        .clip(CircleShape),
//                    model = "https://adik-api.neodigitalcreation.my.id/public/images/default/default.jpeg",
//                    contentDescription = null
//                )
                Image(
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape),
                    bitmap = stateOfProfileImage ?:
                    R.drawable.placholder_image.toBitmap(context = context),
                    contentDescription = null
                )
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextFieldComponent(
                    provideText = stringResource(R.string.text_placeholder_username)
                )
                OutlinedTextFieldComponent(
                    provideText = stringResource(R.string.text_placeholder_name)
                )
                OutlinedTextFieldComponent(
                    provideText = stringResource(R.string.text_placeholder_bio)
                )
                Spacer(modifier = Modifier.padding(top = 16.dp))
                ButtonComponent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp)),
                    provideText = stringResource(R.string.btn_text_submit),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Toast.makeText(context, "Success Change Profile", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun EditProfilePrev() {
    EditProfile(
        navController = rememberNavController()
    )
}