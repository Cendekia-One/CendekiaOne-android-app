package com.capstone.cendekiaone.ui.screen.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
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
import com.capstone.cendekiaone.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun EditProfile(
    modifier: Modifier = Modifier,
    navController: NavController,
){
    val context = LocalContext.current
    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                // Image selected successfully from gallery, proceed to sending to API
                // TODO: Upload to server
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
                        onClick = { navController.navigate(Screen.Home.route) },
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
            modifier = modifier.padding(innerPadding)
        ) {
            Box {
                GlideImage(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape),
                    model = "https://adik-api.neodigitalcreation.my.id/public/images/default/default.jpeg",
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