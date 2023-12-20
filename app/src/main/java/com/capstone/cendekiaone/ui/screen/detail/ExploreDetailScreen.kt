package com.capstone.cendekiaone.ui.screen.detail

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
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
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.capstone.cendekiaone.R
import com.capstone.cendekiaone.data.helper.LocalViewModelFactory
import com.capstone.cendekiaone.data.helper.UserRepository
import com.capstone.cendekiaone.data.remote.response.GetCommentData
import com.capstone.cendekiaone.data.remote.response.GetPostMidResponse
import com.capstone.cendekiaone.data.remote.retforit.ApiService
import com.capstone.cendekiaone.ui.navigation.Screen
import com.capstone.cendekiaone.ui.screen.explore.ExploreScreen2
import com.capstone.cendekiaone.ui.screen.explore.ExploreViewModel
import com.capstone.cendekiaone.ui.theme.Shapes
import com.capstone.cendekiaone.ui.theme.myFont
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreDetailScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    postId: Int,
    username: String
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.menu_explore_detail),
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
                            contentDescription = "Icon Like",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = modifier
                .fillMaxSize()
        ) {
            item {
                PostComponent(modifier, postId, username)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostComponent(
    modifier: Modifier = Modifier,
    postId: Int,
    username: String,
    exploreDetailViewModel: ExploreDetailViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    userRepository: UserRepository = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
) {
    val postDetails by exploreDetailViewModel.postDetails.observeAsState()
    val isLoading by exploreDetailViewModel.isLoading.observeAsState(initial = false)

    // Observe the save result from the ViewModel
    val saveResult by exploreDetailViewModel.savePost.observeAsState()
    val likeResult by exploreDetailViewModel.likedPost.observeAsState()
    val commentResult by exploreDetailViewModel.commentPost.observeAsState()

    LaunchedEffect(exploreDetailViewModel) {
        launch {
            exploreDetailViewModel.loadPostDetails(postId.toString())
        }
    }

    var isSaved by remember { mutableStateOf(false) }
    var isLiked by remember { mutableStateOf(false) }

    // Use a mutable state to keep track of the like count
    var likeCount by remember { mutableStateOf(postDetails?.likes ?: 0) }

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    var skipPartiallyExpanded by remember { mutableStateOf(false) }
    var edgeToEdgeEnabled by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    Column(
        modifier = modifier
            .padding(16.dp)
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
                        model = postDetails?.profileCreator ?: R.drawable.placeholder
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
                        text = postDetails?.createBy ?: "Username",
                        style = TextStyle(
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            fontFamily = myFont,
                            fontWeight = FontWeight.SemiBold
                        ),
                    )
                    Row {
                        Text(
                            text = postDetails?.category ?: "Category",
                            style = TextStyle(
                                fontSize = 12.sp, textAlign = TextAlign.Center, fontFamily = myFont
                            ),
                        )
                        Text(
                            text = " - ${postDetails?.subCatergory}",
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
                    model = postDetails?.postPicture ?: R.drawable.placeholder
                ),
                contentDescription = "Image Post",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center)
                )
            }
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
                        // Trigger the save process in the ViewModel
                        userRepository.getUser().observeForever { user ->
                            if (user != null && user.isLogin) {
                                val savePostId = postId.toString()
                                exploreDetailViewModel.likePost(savePostId, user.id)

                                // Toggle the save button state
                                isLiked = !isLiked

                                // Update the like count
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
                        val savePostId = postId.toString()
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
                        // Trigger the save process in the ViewModel
                        userRepository.getUser().observeForever { user ->
                            if (user != null && user.isLogin) {
                                val savePostId = postId.toString()
                                exploreDetailViewModel.savePost(savePostId, user.id)

                                // Toggle the save button state
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
                text = "${postDetails?.likes?.plus(likeCount)} Likes",
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontFamily = myFont,
                ),
                modifier = Modifier
                    .padding(start = 16.dp)
            )
            Text(
                text = postDetails?.postBody ?: "Description",
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
                        // Trigger the comment process in the ViewModel
                        userRepository.getUser().observeForever { user ->
                            if (user != null && user.isLogin) {
                                val savePostId = postId.toString()
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
            // TODO Place to get Comment
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
                // Registration is successful, show Toast and navigate to LoginScreen
                ShowToast(result.message)
                // Reset the registration result to allow for future registrations
                exploreDetailViewModel.resetSaveResult()
            }

            is ExploreDetailViewModel.SaveResult.Error -> {
                // Handle error if needed, show Toast
                ShowToast(result.errorMessage)
                // Reset the registration result to allow for future registrations
                exploreDetailViewModel.resetSaveResult()
            }

            is ExploreDetailViewModel.SaveResult.NetworkError -> {
                // Handle network error if needed, show Toast
                ShowToast("Network Error")
                // Reset the registration result to allow for future registrations
                exploreDetailViewModel.resetSaveResult()
            }
        }
    }

    // likeResult
    likeResult?.let { result ->
        when (result) {
            is ExploreDetailViewModel.LikeResult.Success -> {
                // Registration is successful, show Toast and navigate to LoginScreen
                ShowToast(result.message)
                // Reset the registration result to allow for future registrations
                exploreDetailViewModel.resetSaveResult()
            }

            is ExploreDetailViewModel.LikeResult.Error -> {
                // Handle error if needed, show Toast
                ShowToast(result.errorMessage)
                // Reset the registration result to allow for future registrations
                exploreDetailViewModel.resetSaveResult()
            }

            is ExploreDetailViewModel.LikeResult.NetworkError -> {
                // Handle network error if needed, show Toast
                ShowToast("Network Error")
                // Reset the registration result to allow for future registrations
                exploreDetailViewModel.resetSaveResult()
            }
        }
    }

    // commentResult
    commentResult?.let { result ->
        when (result) {
            is ExploreDetailViewModel.CommentResult.Success -> {
                // Registration is successful, show Toast and navigate to LoginScreen
                ShowToast(result.message)
                // Reset the registration result to allow for future registrations
                exploreDetailViewModel.resetSaveResult()
            }

            is ExploreDetailViewModel.CommentResult.Error -> {
                // Handle error if needed, show Toast
                ShowToast(result.errorMessage)
                // Reset the registration result to allow for future registrations
                exploreDetailViewModel.resetSaveResult()
            }

            is ExploreDetailViewModel.CommentResult.NetworkError -> {
                // Handle network error if needed, show Toast
                ShowToast("Network Error")
                // Reset the registration result to allow for future registrations
                exploreDetailViewModel.resetSaveResult()
            }
        }
    }
}

@Composable
fun CommentList(apiService: ApiService, viewModel: ExploreDetailViewModel) {
    val commentData: LazyPagingItems<GetCommentData> =
        viewModel.commentData.collectAsLazyPagingItems()

    LazyColumn {
        items(commentData.itemCount) { index ->
            val item = commentData[index]
            if (item != null) {
                CommentComponent(item)
            }
        }
    }
}

@Composable
fun CommentComponent(item: GetCommentData) {
    Row {
        Image(
            painter = rememberAsyncImagePainter(item.profilePicture),
            contentDescription = "Comment Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
        Column {
            Text(text = item.username)
            Text(text = item.commentBody)
        }
    }
}

@Composable
fun ExploreScreen2(item: GetPostMidResponse, navController: NavController) {
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
                navController.navigate(Screen.ExploreDetail.createRoute(item.idPost, item.createBy))
            }
    )
}

@Composable
fun WeaponsList(viewModel: ExploreViewModel, navController: NavController) {
    val weaponsData: LazyPagingItems<GetPostMidResponse> =
        viewModel.weaponsData.collectAsLazyPagingItems()

    LazyVerticalGrid(
        columns = GridCells.Fixed(3)
    ) {
        items(weaponsData.itemCount) { index ->
            val item = weaponsData[index]
            if (item != null) {
                ExploreScreen2(item, navController)
            }
        }
    }
}

@Composable
private fun ShowToast(message: String) {
    Toast.makeText(LocalContext.current, message, Toast.LENGTH_SHORT).show()
}