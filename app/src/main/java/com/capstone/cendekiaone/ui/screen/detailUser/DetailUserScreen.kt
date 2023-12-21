package com.capstone.cendekiaone.ui.screen.detailUser

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import com.capstone.cendekiaone.data.remote.response.GetPostFollowedData
import com.capstone.cendekiaone.ui.component.ButtonComponent
import com.capstone.cendekiaone.ui.component.OutlinedButtonComponent
import com.capstone.cendekiaone.ui.navigation.Screen
import com.capstone.cendekiaone.ui.screen.explore.SearchViewModel
import com.capstone.cendekiaone.ui.screen.profile.FollowViewModel
import com.capstone.cendekiaone.ui.screen.profile.FollowerItem
import com.capstone.cendekiaone.ui.screen.profile.FollowingItem
import com.capstone.cendekiaone.ui.screen.profile.ProfileViewModel
import com.capstone.cendekiaone.ui.screen.profile.ShowToast
import com.capstone.cendekiaone.ui.theme.Shapes
import com.capstone.cendekiaone.ui.theme.myFont
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailUserScreen(
    navController: NavController = rememberNavController(),
    userRepository: UserRepository = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    profileViewModel: ProfileViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    userId: Int,
    modifier: Modifier
) {
    // Observe user details from the ViewModel
    val userDetails by profileViewModel.myUserDetails.observeAsState()
    val otherUserDetails by profileViewModel.otherUserDetails.observeAsState()

    // Observe loading state from the ViewModel
    val isLoading by profileViewModel.isLoading.observeAsState(initial = false)

    Log.d("DETAIL", "User ID Screen: ${userId}")

    // Load user details when the screen is created
    LaunchedEffect(profileViewModel) {
        userRepository.getUser().observeForever { user ->
            if (user != null && user.isLogin) {

                launch {
                    profileViewModel.loadOtherUserDetails(userId.toString() ,user.id)
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        otherUserDetails?.username ?: "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
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
            Column {
                HeaderDetailUser(navController = navController)
                DescriptionDetailUser(
                    navController, userId
                )
                TabLayoutDetailUser(
                    navController = navController,
                    userId = userId,
                    modifier = Modifier
                )

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderDetailUser(
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
    val userDetails by profileViewModel.myUserDetails.observeAsState()
    val otherUserDetails by profileViewModel.otherUserDetails.observeAsState()

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
                model = otherUserDetails?.profilePicture ?: R.drawable.placeholder
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
                text = otherUserDetails?.post ?: "-",
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
                text = otherUserDetails?.follower ?: "-",
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
                modifier = Modifier.width(70.dp).clickable {
                    openBottomSheet2 = !openBottomSheet2
                }
            )
        }
        Column(
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(
                text = otherUserDetails?.following ?: "-",
                style = TextStyle(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = myFont,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.width(70.dp).clickable {
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
                    FollowerItem(follower = follower, navController)
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
                    FollowingItem(following = following, navController)
                }
            }
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
    val userDetails by profileViewModel.myUserDetails.observeAsState()
    val otherUserDetails by profileViewModel.otherUserDetails.observeAsState()
    val userFollow by searchViewModel.followUser.observeAsState()

    var isFollowing by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text(
            text = otherUserDetails?.name ?: "",
            style = TextStyle(
                textAlign = TextAlign.Center,
                fontFamily = myFont,
                fontWeight = FontWeight.SemiBold
            ),
        )
        Text(
            text = otherUserDetails?.bio ?: "-",
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
fun TabLayoutDetailUser(
    profileViewModel: ProfileViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    userRepository: UserRepository = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    navController: NavController,
    userId: Int,
    modifier: Modifier
) {
    val tabIndex = profileViewModel.tabIndex

    val tabs = listOf("Posts")

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
            0 -> DetailUserPage(profileViewModel, userRepository, navController, userId)
        }
    }
}

@Composable
fun DetailUserPage(
    profileViewModel: ProfileViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    userRepository: UserRepository = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    navController: NavController,
    userId: Int
) {
    DetailUserPostList(
        ProfileViewModel(apiService = profileViewModel.apiService),
        userRepository,
        navController,
        userId
    )
}

@Composable
fun DetailUserPostComponent(item: GetPostFollowedData, navController: NavController) {
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
fun DetailUserPostList(
    profileViewModel: ProfileViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    userRepository: UserRepository = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    navController: NavController,
    userId: Int
) {

    LaunchedEffect(profileViewModel) {
        launch {
            profileViewModel.setIdUser(userId.toString())
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
                DetailUserPostComponent(item, navController)
            }
        }
    }
}