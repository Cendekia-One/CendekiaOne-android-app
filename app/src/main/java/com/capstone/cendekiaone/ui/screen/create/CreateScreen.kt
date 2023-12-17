package com.capstone.cendekiaone.ui.screen.create

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capstone.cendekiaone.R
import com.capstone.cendekiaone.ui.component.BaseCircleIconBox
import com.capstone.cendekiaone.ui.component.ButtonComponent
import com.capstone.cendekiaone.ui.component.OutlinedTextFieldComponent
import com.capstone.cendekiaone.ui.theme.myFont
import com.capstone.cendekiaone.utils.toBitmap
import com.capstone.cendekiaone.utils.uriToFile
import java.io.File

@Composable
fun CreateScreen(
    modifier: Modifier = Modifier,
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
            .verticalScroll(rememberScrollState())
    ) {
        HeaderPost(
            username = "CendikiaOne",
        )
        MainPost()
        BottomPost()
    }
}

@Composable
fun HeaderPost(
    username: String,
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
            }
        }
    }
}

@Composable
fun MainPost(
    modifier: Modifier = Modifier
) {
    var getFile by remember { mutableStateOf<File?>(null) }
    val context = LocalContext.current
    var stateOfImage by remember {
        mutableStateOf<ImageBitmap?>(null)
    }
    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                // Convert the selected URI to a File
                getFile = uriToFile(uri, context)
                stateOfImage = getFile?.toBitmap() ?: ImageBitmap(1, 1)
            }
        }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        Image(
            bitmap = stateOfImage ?: R.drawable.placeholder.toBitmap(context = context),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        Box(
            modifier = Modifier
                .background(Color.Transparent, shape = CircleShape)
                .padding(8.dp)
                .align(Alignment.BottomEnd)
        ) {
            val icon: Painter = painterResource(id = R.drawable.ic_edit_outline)
            FilledIconButton(onClick = { pickImageLauncher.launch("image/*") }) {
                Icon(
                    painter = icon,
                    contentDescription = "Localized description"
                )
            }
        }
    }
}

@Composable
fun BottomPost() {
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextFieldComponent(
            provideText = stringResource(R.string.description),
            value = description,
            onValueChange = { description = it }
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextFieldComponent(
            provideText = stringResource(R.string.catergory_post),
            value = category,
            onValueChange = { category = it }
        )

        Spacer(modifier = Modifier.height(16.dp))
        ButtonComponent(
            provideText = stringResource(R.string.posting),
            modifier = Modifier.fillMaxWidth()
        ) { }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

fun File.toBitmap(): ImageBitmap {
    return try {
        BitmapFactory.decodeFile(this.path).asImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        ImageBitmap(1, 1) // Return a placeholder or an empty image in case of an error
    }
}

@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun CreateScreenPreview() {
    CreateScreen()
}