package com.capstone.cendekiaone.ui.screen.brief

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.capstone.cendekiaone.R
import com.capstone.cendekiaone.ui.theme.myFont
import com.capstone.cendekiaone.ui.theme.myFont2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BriefScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                actions = {
                    FakeTopAppComponent(
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
            item {
                FakePostComponent()
            }
        }
    }
}

@Composable
fun FakeTopAppComponent(
    modifier: Modifier = Modifier,
    navController: NavController // Add the NavController parameter
) {
    ProvideTextStyle(
        value = TextStyle(
            fontFamily = myFont, // Replace with your desired fontFamily
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp // Adjust the fontSize as needed
        )
    ) {
        Row(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "CendikiaOne Summary",
                style = TextStyle(
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = myFont2
                ),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun FakePostComponent(
    modifier: Modifier = Modifier
) {
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
        FakeHeaderPost(
            username = "MuhDila",
            category = "Your 3 posts in a nutshell"
        )
        FakeMainPost()
        FakeBottomPost()
    }
}

@Composable
fun FakeHeaderPost(
    username: String,
    category: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(64.dp)
            .fillMaxWidth()
            .padding(end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.CenterVertically)
        ) {
            Image(
                painter = painterResource(id = R.drawable.my_photo),
                contentDescription = "Image Profile",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = username,
                    style = TextStyle(
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = myFont,
                        fontWeight = FontWeight.SemiBold
                    ),
                )
                Text(
                    text = category,
                    style = TextStyle(
                        fontSize = 12.sp, textAlign = TextAlign.Center, fontFamily = myFont
                    ),
                )
            }
        }
    }
}

@Composable
fun FakeMainPost() {
    Image(
        painter = painterResource(id = R.drawable.fake_img),
        contentDescription = "Image Profile",
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    )
}

@Composable
fun FakeBottomPost(
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            text = stringResource(R.string.fake2_description_post),
            style = TextStyle(
                textAlign = TextAlign.Justify,
                fontFamily = myFont,
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}