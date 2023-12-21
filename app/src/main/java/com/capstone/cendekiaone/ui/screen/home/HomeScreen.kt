package com.capstone.cendekiaone.ui.screen.home

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.capstone.cendekiaone.R
import com.capstone.cendekiaone.data.helper.LocalViewModelFactory
import com.capstone.cendekiaone.data.helper.UserRepository
import com.capstone.cendekiaone.data.remote.response.GetPostFollowedData
import com.capstone.cendekiaone.ui.component.PostComponent
import com.capstone.cendekiaone.ui.component.TopAppComponent
import com.capstone.cendekiaone.ui.screen.detail.CommentList
import com.capstone.cendekiaone.ui.screen.detail.ExploreDetailViewModel
import com.capstone.cendekiaone.ui.theme.myFont
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    userRepository: UserRepository = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
) {

    LaunchedEffect(homeViewModel) {
        userRepository.getUser().observeForever { user ->
            if (user != null && user.isLogin) {
                Log.d("Home", "User Token Screen: ${user.token}")
                Log.d("Home", "User ID Screen: ${user.id}")

                launch {
                    homeViewModel.setIdUser(user.id)
                }
            }
        }
    }

    val commentData: LazyPagingItems<GetPostFollowedData> =
        homeViewModel.followedPostData.collectAsLazyPagingItems()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { },
                scrollBehavior = scrollBehavior,
                actions = {
                    TopAppComponent(
                        navController = navController
                    )
                },
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = modifier
                .fillMaxSize()
        ) {
            items(commentData.itemCount) { index ->
                val item = commentData[index]
                if (item != null) {
                    PostFollowedComponent(item, modifier, onPostClick = { idPost ->
                        // Handle the click event for the specific postId
                        idPost.toString()
                    })
                } else {
                    Text(text = "You Are Not Following Anyone")
                }
            }
            // If the item count is zero, display a message
            if (commentData.itemCount == 0) {
                item {
                    PostComponent()
                }
            }
        }
        BackHandler {
            // Perform any custom actions here, if necessary
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostFollowedComponent(
    item: GetPostFollowedData,
    modifier: Modifier,
    onPostClick: (Int) -> Unit,
    userRepository: UserRepository = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    exploreDetailViewModel: ExploreDetailViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
) {
    // Observe the save result from the ViewModel
    val saveResult by exploreDetailViewModel.savePost.observeAsState()
    val likeResult by exploreDetailViewModel.likedPost.observeAsState()
    val commentResult by exploreDetailViewModel.commentPost.observeAsState()

    var isSaved by remember { mutableStateOf(item.isSaved) }
    var isLiked by remember { mutableStateOf(item.isLike) }
    var likeCount by remember { mutableIntStateOf(item.likes.toInt()) }
    Log.d("PostFollowedComponent", "PostFollowedComponent: $isLiked")
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val skipPartiallyExpanded by remember { mutableStateOf(false) }
    val edgeToEdgeEnabled by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            modifier = modifier
                .height(64.dp)
                .fillMaxWidth()
                .padding(end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Head Post
            Row(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        item.profileCreator
                    ),
                    contentDescription = "Image Profile",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = item.createBy,
                        style = TextStyle(
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            fontFamily = myFont,
                            fontWeight = FontWeight.SemiBold
                        ),
                    )
                    Row {
                        Text(
                            text = item.category,
                            style = TextStyle(
                                fontSize = 12.sp, textAlign = TextAlign.Center, fontFamily = myFont
                            ),
                        )
                        Text(
                            text = " - ${item.subCatergory}",
                            style = TextStyle(
                                fontSize = 12.sp, textAlign = TextAlign.Center, fontFamily = myFont
                            ),
                        )
                    }
                }
            }
            IconButton(
                onClick = { },
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterVertically)
            ) {
                val icon: Painter = painterResource(id = R.drawable.ic_menu_outline)
                Icon(
                    painter = icon,
                    contentDescription = "Icon Menu",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // Main Post
        Box {
            Image(
                painter = rememberAsyncImagePainter(
                    model = item.postPicture
                ),
                contentDescription = "Image Post",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
        }

        // Bottom Post
        Column {
            Row(
                modifier = modifier
                    .height(54.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        userRepository.getUser().observeForever { user ->
                            if (user != null && user.isLogin) {
                                onPostClick(item.idPost)
                                val savePostId = item.idPost.toString()
                                exploreDetailViewModel.likePost(savePostId, user.id)

                                isLiked = !isLiked
                                Log.d("Home", "User Token Screen: $savePostId")
                                likeCount += if (isLiked) 1 else -1

                            }
                        }
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    // Use different icons based on the save button state
                    val icon: Painter = if (isLiked) {
                        painterResource(id = R.drawable.ic_like_filled)
                    } else {
                        painterResource(id = R.drawable.ic_like_outline)
                    }

                    Icon(
                        painter = icon,
                        contentDescription = "Icon Save",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        openBottomSheet = !openBottomSheet
                        onPostClick(item.idPost)
                        val savePostId = item.idPost.toString()
                        exploreDetailViewModel.setPostId(savePostId)
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    val icon: Painter = painterResource(id = R.drawable.ic_comment_outline)
                    Icon(
                        painter = icon,
                        contentDescription = "Icon Comment",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    val icon: Painter = painterResource(id = R.drawable.ic_share_outline)
                    Icon(
                        painter = icon,
                        contentDescription = "Icon Share",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    val icon: Painter = painterResource(id = R.drawable.ic_magic_outline)
                    Icon(
                        painter = icon,
                        contentDescription = "Icon Brief",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                IconButton(
                    onClick = {
                        userRepository.getUser().observeForever { user ->
                            if (user != null && user.isLogin) {
                                onPostClick(item.idPost)
                                val savePostId = item.idPost.toString()
                                exploreDetailViewModel.savePost(savePostId, user.id)

                                isSaved = !isSaved
                            }
                        }
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    // Use different icons based on the save button state
                    val icon: Painter = if (isSaved) {
                        painterResource(id = R.drawable.ic_save_filled)
                    } else {
                        painterResource(id = R.drawable.ic_save_outline)
                    }

                    Icon(
                        painter = icon,
                        contentDescription = "Icon Save",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            Text(
                text = "$likeCount Likes",
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontFamily = myFont,
                ),
                modifier = Modifier
                    .padding(start = 16.dp)
            )
            Text(
                text = item.postBody,
                style = TextStyle(
                    textAlign = TextAlign.Justify,
                    fontFamily = myFont,
                ),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp)
            )
        }
    }

    // Sheet content
    if (openBottomSheet) {
        val windowInsets = if (edgeToEdgeEnabled)
            WindowInsets(0) else BottomSheetDefaults.windowInsets

        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = bottomSheetState,
            windowInsets = windowInsets
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

            }
            var commentBody by remember { mutableStateOf("") }
            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                OutlinedTextField(
                    value = commentBody,
                    onValueChange = {
                        commentBody = it
                    },
                    label = { Text("Add a comment") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_bio_filled),
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        if (commentBody.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    commentBody = ""
                                },
                                modifier = Modifier.size(40.dp)
                            ) {
                                val icon: Painter = painterResource(id = R.drawable.ic_close)
                                Icon(
                                    painter = icon,
                                    contentDescription = "Clear",
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilledIconButton(
                    onClick = {
                        userRepository.getUser().observeForever { user ->
                            if (user != null && user.isLogin) {
                                onPostClick(item.idPost)
                                val savePostId = item.idPost.toString()
                                exploreDetailViewModel.commentPost(savePostId, user.id, commentBody)
                                exploreDetailViewModel.setPostId(savePostId)
                            }
                        }
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    val icon: Painter = painterResource(id = R.drawable.ic_sent_outline)
                    Icon(
                        painter = icon,
                        contentDescription = "Icon Brief",
                        tint = MaterialTheme.colorScheme.background,
                    )
                }
            }
            CommentList(
                apiService = exploreDetailViewModel.apiService,
                viewModel = exploreDetailViewModel
            )
        }
    }

    // saveResult
    saveResult?.let { result ->
        when (result) {
            is ExploreDetailViewModel.SaveResult.Success -> {
                ShowToast(result.message)
                exploreDetailViewModel.resetSaveResult()
            }

            is ExploreDetailViewModel.SaveResult.Error -> {
                ShowToast(result.errorMessage)
                exploreDetailViewModel.resetSaveResult()
            }

            is ExploreDetailViewModel.SaveResult.NetworkError -> {
                ShowToast("Network Error")
                exploreDetailViewModel.resetSaveResult()
            }
        }
    }

    // likeResult
    likeResult?.let { result ->
        when (result) {
            is ExploreDetailViewModel.LikeResult.Success -> {
                ShowToast(result.message)
            }

            is ExploreDetailViewModel.LikeResult.Error -> {
                ShowToast(result.errorMessage)
            }

            is ExploreDetailViewModel.LikeResult.NetworkError -> {
                ShowToast("Network Error")
            }
        }
    }

    // commentResult
    commentResult?.let { result ->
        when (result) {
            is ExploreDetailViewModel.CommentResult.Success -> {
                ShowToast(result.message)
                exploreDetailViewModel.resetSaveResult()
            }

            is ExploreDetailViewModel.CommentResult.Error -> {
                ShowToast(result.errorMessage)
                exploreDetailViewModel.resetSaveResult()
            }

            is ExploreDetailViewModel.CommentResult.NetworkError -> {
                ShowToast("Network Error")
                exploreDetailViewModel.resetSaveResult()
            }
        }
    }
}

@Composable
private fun ShowToast(message: String) {
    Toast.makeText(LocalContext.current, message, Toast.LENGTH_SHORT).show()
}