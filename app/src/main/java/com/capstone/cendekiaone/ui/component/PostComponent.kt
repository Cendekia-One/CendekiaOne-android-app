package com.capstone.cendekiaone.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capstone.cendekiaone.R
import com.capstone.cendekiaone.ui.theme.myFont

@Composable
fun PostComponent(
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
        HeaderPost(
            username = "CendikiaOne",
            category = "Geology"
        )
        MainPost()
        BottomPost()
    }
}

@Composable
fun HeaderPost(
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
                painter = painterResource(id = R.drawable.placeholder),
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
}

@Composable
fun MainPost() {
    Image(
        painter = painterResource(id = R.drawable.placeholder),
        contentDescription = "Image Profile",
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    )
}

@Composable
fun BottomPost(
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            modifier = modifier
                .height(54.dp)
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { },
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterVertically)
            ) {
                val icon: Painter = painterResource(id = R.drawable.ic_like_outline)
                Icon(
                    painter = icon,
                    contentDescription = "Icon Like",
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
                onClick = { },
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterVertically)
            ) {
                val icon: Painter = painterResource(id = R.drawable.ic_save_outline)
                Icon(
                    painter = icon,
                    contentDescription = "Icon Save",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.many_like),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontFamily = myFont,
                ),
                modifier = Modifier.padding(start = 16.dp).align(Alignment.CenterVertically)
            )
            val icon: Painter = painterResource(id = R.drawable.ic_verified)
            Icon(
                painter = icon,
                contentDescription = "Icon Share",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp)
            )
            Text(
                text = stringResource(R.string.verified),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontFamily = myFont,
                    color = MaterialTheme.colorScheme.primary,
                ),
                modifier = Modifier.padding(start = 4.dp).align(Alignment.CenterVertically)
            )
        }
        Text(
            text = stringResource(R.string.description_post),
            style = TextStyle(
                textAlign = TextAlign.Justify,
                fontFamily = myFont,
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun PostComponentPreview() {
    PostComponent()
}

@Preview
@Composable
fun HeaderPostPreview() {
    HeaderPost(
        username = "CendikiaOne",
        category = "Geology"
    )
}