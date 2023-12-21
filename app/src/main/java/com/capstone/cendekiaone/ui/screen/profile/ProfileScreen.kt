package com.capstone.cendekiaone.ui.screen.profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBarDefaults.windowInsets
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
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
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.capstone.cendekiaone.R
import com.capstone.cendekiaone.data.helper.LocalViewModelFactory
import com.capstone.cendekiaone.data.helper.UserRepository
import com.capstone.cendekiaone.data.remote.response.FollowData
import com.capstone.cendekiaone.data.remote.response.FollowingData
import com.capstone.cendekiaone.data.remote.response.GetPostFollowedData
import com.capstone.cendekiaone.ui.component.AlertDialogComponent
import com.capstone.cendekiaone.ui.component.ButtonComponent
import com.capstone.cendekiaone.ui.component.OutlinedButtonComponent
import com.capstone.cendekiaone.ui.navigation.Screen
import com.capstone.cendekiaone.ui.theme.Shapes
import com.capstone.cendekiaone.ui.theme.myFont
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController = rememberNavController(),
    userRepository: UserRepository = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    profileViewModel: ProfileViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
) {
    // Observe user details from the ViewModel
    val userDetails by profileViewModel.userDetails.observeAsState()

    // Observe loading state from the ViewModel
    val isLoading by profileViewModel.isLoading.observeAsState(initial = false)

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

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        userDetails?.username ?: "",
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
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 12.dp)
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
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Column {
                HeaderProfile(navController = navController)
                DescriptionProfile(
                    navController
                )
                TabLayout(navController = navController, modifier = Modifier)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderProfile(
    modifier: Modifier = Modifier,
    navController: NavController,
    profileViewModel: ProfileViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    followViewModel: FollowViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    userRepository: UserRepository = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
) {
    // Observe user details from the ViewModel
    val userDetails by profileViewModel.userDetails.observeAsState()

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val skipPartiallyExpanded by remember { mutableStateOf(false) }
    val edgeToEdgeEnabled by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    var openBottomSheet2 by rememberSaveable { mutableStateOf(false) }
    val skipPartiallyExpanded2 by remember { mutableStateOf(false) }
    val edgeToEdgeEnabled2 by remember { mutableStateOf(false) }
    val bottomSheetState2 = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded2
    )

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            contentScale = ContentScale.Crop,
            painter = rememberAsyncImagePainter(
                model = userDetails?.profilePicture ?: R.drawable.placeholder
            ),
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
                modifier = Modifier
                    .width(70.dp)
                    .clickable {
                        openBottomSheet = !openBottomSheet
                    }
            )
            Text(
                text = "Follower",
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontFamily = myFont,
                ),
                modifier = Modifier
                    .width(70.dp)
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
                modifier = Modifier
                    .width(70.dp)
                    .clickable {
                        openBottomSheet2 = !openBottomSheet2
                    }
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

    val followerList by followViewModel.followerList.observeAsState(emptyList())
    val followingList by followViewModel.followingList.observeAsState(emptyList())

    LaunchedEffect(followViewModel) {
        userRepository.getUser().observeForever { user ->
            if (user != null && user.isLogin) {
                Log.d("Follower", "User Token Screen: ${user.token}")
                Log.d("Follower", "User ID Screen: ${user.id}")

                launch {
                    followViewModel.getAllFollowers(user.id)
                    followViewModel.getAllFollowing(user.id)
                }
            }
        }
    }

    if (openBottomSheet) {
        val windowInsets = if (edgeToEdgeEnabled)
            WindowInsets(0) else BottomSheetDefaults.windowInsets

        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = bottomSheetState,
            windowInsets = windowInsets
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                itemsIndexed(followerList) { index, follower ->
                    FollowerItem(index = index + 1, follower = follower)
                }
            }
        }
    }

    // TODO
    if (openBottomSheet2) {
        val windowInsets2 = if (edgeToEdgeEnabled2)
            WindowInsets(0) else BottomSheetDefaults.windowInsets

        ModalBottomSheet(
            onDismissRequest = { openBottomSheet2 = false },
            sheetState = bottomSheetState2,
            windowInsets = windowInsets2
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                itemsIndexed(followingList) { index, following ->
                    FollowingItem(index = index + 1, following = following)
                }
            }
        }
    }
}

@Composable
fun FollowerItem(index: Int, follower: FollowData) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "$index. ${follower.followerUsername}",
            modifier = Modifier.align(Alignment.CenterStart)
        )
    }
}

@Composable
fun FollowingItem(index: Int, following: FollowingData) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "$index. ${following.followingUsername}",
            modifier = Modifier.align(Alignment.CenterStart)
        )
    }
}

@Composable
fun DescriptionProfile(
    navController: NavController = rememberNavController(),
    profileViewModel: ProfileViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
) {
    // Observe user details from the ViewModel
    val userDetails by profileViewModel.userDetails.observeAsState()

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
fun TabLayout(
    profileViewModel: ProfileViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    userRepository: UserRepository = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    navController: NavController,
    modifier: Modifier
) {
    val tabIndex= profileViewModel.tabIndex

    val tabs = listOf("Posts", "Saved")

    Column(modifier = modifier.fillMaxWidth()) {
        TabRow(
            selectedTabIndex = tabIndex,
            indicator = { tabPositions ->
                Box(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[tabIndex]),
                    content = {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp) // Adjust the height of the indicator
                                .background(MaterialTheme.colorScheme.primary)
                                .align(Alignment.BottomCenter) // Align with the bottom of the TabRow
                        )
                    }
                )
            }) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = {
                        Text(
                            title,
                        )
                    },
                    selected = tabIndex == index,
                    onClick = { profileViewModel.tabIndex(index) },
                )
            }
        }
        when (tabIndex) {
            0 -> MyPostPage(profileViewModel, userRepository, navController)
            1 -> MySavePage(profileViewModel, userRepository, navController)
        }
    }
}

@Composable
fun MyPostPage(
    profileViewModel: ProfileViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    userRepository: UserRepository = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    navController: NavController
) {
    MyPostList(ProfileViewModel(apiService = profileViewModel.apiService), userRepository, navController)
}

@Composable
fun MyPostListComponent(item: GetPostFollowedData, navController: NavController) {
    Image(
        painter = rememberAsyncImagePainter(item.postPicture),
        contentDescription = "Post Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(140.dp)
            .clip(Shapes.large)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                //TODO
                navController.navigate(Screen.ExploreDetail.createRoute(item.idPost, item.createBy))
            }
    )
}

@Composable
fun MyPostList(
    profileViewModel: ProfileViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    userRepository: UserRepository = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    navController: NavController
) {
    LaunchedEffect(profileViewModel) {
        userRepository.getUser().observeForever { user ->
            if (user != null && user.isLogin) {
                Log.d("Home", "User Token Screen: ${user.token}")
                Log.d("Home", "User ID Screen: ${user.id}")

                launch {
                    profileViewModel.setIdUser(user.id)
                }
            }
        }
    }

    val myPostDataList: LazyPagingItems<GetPostFollowedData> =
        profileViewModel.myPostData.collectAsLazyPagingItems()

    LazyVerticalGrid(
        columns = GridCells.Fixed(3)
    ) {
        items(myPostDataList.itemCount) { index ->
            val item = myPostDataList[index]
            if (item != null) {
                MyPostListComponent(item, navController)
            }
        }
    }
}

@Composable
fun MySavePage(
    profileViewModel: ProfileViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    userRepository: UserRepository = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    navController: NavController
) {
    MySaveList(ProfileViewModel(apiService = profileViewModel.apiService), userRepository, navController)
}

@Composable
fun MySaveList(
    profileViewModel: ProfileViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    userRepository: UserRepository = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    navController: NavController
) {

    LaunchedEffect(profileViewModel) {
        userRepository.getUser().observeForever { user ->
            if (user != null && user.isLogin) {
                Log.d("Home", "User Token Screen: ${user.token}")
                Log.d("Home", "User ID Screen: ${user.id}")

                launch {
                    profileViewModel.setIdUser(user.id)
                }
            }
        }
    }

    val mySaveDataList: LazyPagingItems<GetPostFollowedData> =
        profileViewModel.mySaveData.collectAsLazyPagingItems()

    LazyVerticalGrid(
        columns = GridCells.Fixed(3)
    ) {
        items(mySaveDataList.itemCount) { index ->
            val item = mySaveDataList[index]
            if (item != null) {
                MyPostListComponent(item, navController)
            }
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