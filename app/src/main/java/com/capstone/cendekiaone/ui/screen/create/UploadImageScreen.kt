package com.capstone.cendekiaone.ui.screen.create

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.capstone.cendekiaone.R
import com.capstone.cendekiaone.ui.component.ButtonComponent
import com.capstone.cendekiaone.ui.component.OutlinedButtonComponent
import com.capstone.cendekiaone.ui.navigation.Screen
@Composable
fun UploadImageScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val context = LocalContext.current
    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
            Toast.makeText(
                context,
                R.string.toast_choose_from_gallery,
                Toast.LENGTH_SHORT
            ).show()
        }
    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            Toast.makeText(
                context,
                "Foto diambil dari kamera",
                Toast.LENGTH_SHORT
            ).show()

        } else {
            Toast.makeText(
                context,
                "Gagal mengambil foto dari kamera",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(
            16.dp,
            Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFECE6F0),
                    shape = RoundedCornerShape(size = 28.dp)
                )
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(
                16.dp,
                Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = Icons.Outlined.AddCircleOutline,
                contentDescription = null
            )
            Text(text = stringResource(R.string.text__pilih_sumber))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    24.dp,
                    Alignment.CenterHorizontally
                )
            ) {
                ButtonComponent(
                    provideText = stringResource(R.string.btn_text_galeri),
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_gallery_thumbnail),
                            contentDescription = "add circle",
                        )
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    pickImageLauncher.launch("image/*")
                }
                OutlinedButtonComponent(
                    provideText = stringResource(R.string.btn_text_kamera),
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.CameraAlt,
                            contentDescription = "add circle",
                        )
                    },
                    modifier = Modifier.weight(1f)
                ) {

                }
            }
        }
        ButtonComponent(
            provideText = stringResource(R.string.btn_textselesai),
            modifier = Modifier.fillMaxWidth()
        ) {
            navController.navigate(Screen.Create.route)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun UploadCameraPrev() {
    UploadImageScreen(navController = rememberNavController())
}



