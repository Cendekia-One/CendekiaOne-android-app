package com.capstone.cendekiaone.ui.screen.profile

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.capstone.cendekiaone.BuildConfig
import com.capstone.cendekiaone.R
import com.capstone.cendekiaone.data.helper.LocalViewModelFactory
import com.capstone.cendekiaone.data.helper.UserRepository
import com.capstone.cendekiaone.ui.component.AlertDialogComponent
import com.capstone.cendekiaone.ui.component.ButtonComponent
import com.capstone.cendekiaone.ui.component.OutlinedButtonComponent
import com.capstone.cendekiaone.ui.navigation.Screen
import com.capstone.cendekiaone.ui.theme.myFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController = rememberNavController(),
    userRepository: UserRepository = viewModel(
        factory = LocalViewModelFactory.provide()
    )
) {
    var showDialog by remember { mutableStateOf(false) }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(
                        "cendikia_one",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            // Set showDialog to true when the logout icon is clicked
                            showDialog = true
                        },
                        modifier = Modifier.size(40.dp)
                    ) {
                        val icon: Painter = painterResource(id = R.drawable.ic_logout_outline)
                        Icon(
                            painter = icon,
                            contentDescription = "Icon Logout",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            item {
                HeaderProfile()
                DescriptionProfile(
                    navController
                )
                TabLayout()
            }
        }
    }

    // Logout Confirmation Dialog
    if (showDialog) {
        AlertDialogComponent(
            onDismiss = {
                // Reset the showDialog state when the dialog is dismissed
                showDialog = false
            },
            onConfirm = {
                // Log out the user and navigate to the Intro screen
                userRepository.logout()
                navController.navigate(Screen.Login.route) {
                    // Clear the back stack
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun HeaderProfile(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Image Profile",
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
        )
        Column(
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(
                text = "12",
                style = TextStyle(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = myFont,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.width(50.dp)
            )
            Text(
                text = "Posts",
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontFamily = myFont,
                ),
                modifier = Modifier.width(50.dp)
            )
        }
        Column(
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(
                text = "1200",
                style = TextStyle(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = myFont,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.width(70.dp)
            )
            Text(
                text = "Follower",
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontFamily = myFont,
                ),
                modifier = Modifier.width(70.dp)
            )
        }
        Column(
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(
                text = "1039",
                style = TextStyle(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = myFont,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.width(70.dp)
            )
            Text(
                text = "Following",
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontFamily = myFont,
                ),
                modifier = Modifier.width(70.dp)
            )
        }
    }
}

@Composable
fun DescriptionProfile(
    navController: NavController = rememberNavController()
) {
    Column(
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text(
            text = "CendikiaOne",
            style = TextStyle(
                textAlign = TextAlign.Center,
                fontFamily = myFont,
                fontWeight = FontWeight.SemiBold
            ),
        )
        Text(
            text = stringResource(R.string.description_post),
            style = TextStyle(
                textAlign = TextAlign.Justify,
                fontFamily = myFont,
            ),
            modifier = Modifier.padding(top = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ButtonComponent(
                provideText = stringResource(R.string.edit_profile),
                onClick = {
                    navController.navigate(Screen.EditProfile.route)
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp),
            )
            OutlinedButtonComponent(
                provideText = stringResource(R.string.share_profile),
                onClick = { },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp),
            )
        }
    }
}

@Composable
fun TabLayout() {
    val tabs = remember { listOf("Posts", "Saved") }
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    TabRow(
        selectedTabIndex = selectedTabIndex,
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { selectedTabIndex = index },
                text = {
                    Text(
                        text = title,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        navController = rememberNavController()
    )
}