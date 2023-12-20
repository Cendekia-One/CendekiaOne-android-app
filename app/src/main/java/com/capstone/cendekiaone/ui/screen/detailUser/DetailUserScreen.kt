package com.capstone.cendekiaone.ui.screen.detailUser

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.capstone.cendekiaone.R
import com.capstone.cendekiaone.data.helper.LocalViewModelFactory
import com.capstone.cendekiaone.data.helper.UserRepository
import com.capstone.cendekiaone.ui.component.ButtonComponent
import com.capstone.cendekiaone.ui.component.OutlinedButtonComponent
import com.capstone.cendekiaone.ui.navigation.Screen
import com.capstone.cendekiaone.ui.screen.explore.SearchViewModel
import com.capstone.cendekiaone.ui.screen.profile.EditProfileViewModel
import com.capstone.cendekiaone.ui.screen.profile.ProfileViewModel
import com.capstone.cendekiaone.ui.screen.profile.ShowToast
import com.capstone.cendekiaone.ui.theme.myFont
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailUserScreen(
    navController: NavController = rememberNavController(),
    profileViewModel: ProfileViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    userId: Int
) {
    // Observe user details from the ViewModel
    val userDetails by profileViewModel.userDetails.observeAsState()

    // Observe loading state from the ViewModel
    val isLoading by profileViewModel.isLoading.observeAsState(initial = false)

    // Load user details when the screen is created
    LaunchedEffect(profileViewModel) {
        launch {
            profileViewModel.loadUserDetails(userId.toString())
        }
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(
                        userDetails?.username ?: "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate(Screen.Explore.route) },
                        modifier = Modifier
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
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn {
                item {
                    HeaderDetailUser()
                    DescriptionDetailUser(
                        navController, userId
                    )
                    TabLayoutDetailUser()
                }
            }
            // Loading indicator
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun HeaderDetailUser(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
) {
    // Observe user details from the ViewModel
    val userDetails by profileViewModel.userDetails.observeAsState()

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            contentScale = ContentScale.Crop,
            painter = rememberAsyncImagePainter(model = userDetails?.profilePicture ?: R.drawable.placeholder),
            contentDescription = "Image Profile",
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
        )

        Column(
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(
                text = userDetails?.post ?: "-",
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
                text = userDetails?.follower ?: "-",
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
                text = userDetails?.following ?: "-",
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
fun DescriptionDetailUser(
    navController: NavController = rememberNavController(),
    userId: Int,
    profileViewModel: ProfileViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    searchViewModel: SearchViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    userRepository: UserRepository = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
) {
    // Observe user details from the ViewModel
    val userDetails by profileViewModel.userDetails.observeAsState()
    val userFollow by searchViewModel.followUser.observeAsState()

    var isFollowing by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text(
            text = userDetails?.name ?: "",
            style = TextStyle(
                textAlign = TextAlign.Center,
                fontFamily = myFont,
                fontWeight = FontWeight.SemiBold
            ),
        )
        Text(
            text = userDetails?.bio ?: "-",
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
            val buttonText = if (isFollowing) {
                // User is already following, display unfollow button
                stringResource(R.string.following)
            } else {
                // User is not following, display follow button
                stringResource(R.string.follow)
            }

            if (isFollowing) {
                // User is already following, display unfollow button
                OutlinedButtonComponent(
                    provideText = buttonText,
                    onClick = {
                        // TODO: Implement unfollow logic
                        userRepository.getUser().observeForever { user ->
                            if (user != null && user.isLogin) {
                                searchViewModel.postFollowUser(user.id, userId.toString())
                            }
                            // Update the state to reflect the change
                            isFollowing = false
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                )
            } else {
                // User is not following, display follow button
                ButtonComponent(
                    provideText = buttonText,
                    onClick = {
                        // TODO: Implement follow logic
                        userRepository.getUser().observeForever { user ->
                            if (user != null && user.isLogin) {
                                searchViewModel.postFollowUser(user.id, userId.toString())
                            }
                            // Update the state to reflect the change
                            isFollowing = true
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                )
            }
            OutlinedButtonComponent(
                provideText = stringResource(R.string.share_profile),
                onClick = { },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp),
            )
        }
    }

    userFollow?.let { result ->
        when (result) {
            is SearchViewModel.FollowResult.Success -> {
                // Registration is successful, show Toast and navigate to LoginScreen
                ShowToast(result.message)
                // Reset the registration result to allow for future registrations
                searchViewModel.resetFollowResult()
            }
            is SearchViewModel.FollowResult.Error -> {
                // Handle error if needed, show Toast
                ShowToast(result.errorMessage)
                // Reset the registration result to allow for future registrations
                searchViewModel.resetFollowResult()
            }
            is SearchViewModel.FollowResult.NetworkError -> {
                // Handle network error if needed, show Toast
                ShowToast("Network Error")
                // Reset the registration result to allow for future registrations
                searchViewModel.resetFollowResult()
            }
        }
    }
}

@Composable
fun TabLayoutDetailUser() {
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