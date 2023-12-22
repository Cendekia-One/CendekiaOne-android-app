package com.capstone.cendekiaone.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.capstone.cendekiaone.R
import com.capstone.cendekiaone.ui.navigation.Screen
import com.capstone.cendekiaone.ui.theme.myFont
import com.capstone.cendekiaone.ui.theme.myFont2

@Composable
fun TopAppComponent(
    modifier: Modifier = Modifier,
    navController: NavController // Add the NavController parameter
) {
    var showDialog by remember { mutableStateOf(false) }

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
                text = "CendikiaOne",
                style = TextStyle(
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = myFont2
                ),
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = { showDialog = true },
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .size(40.dp)
            ) {
                val icon: Painter = painterResource(id = R.drawable.ic_notification_outline)
                Icon(
                    painter = icon,
                    contentDescription = "Icon Notification",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            IconButton(
                onClick = { showDialog = true },
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .size(40.dp)
            ) {
                val icon: Painter = painterResource(id = R.drawable.ic_chat_outline)
                Icon(
                    painter = icon,
                    contentDescription = "Icon Chat",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or presses the back button
                showDialog = false
            },
            title = {
                Text(text = "Under Development")
            },
            text = {
                Text(text = "This feature is currently under development.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicAlertDialogSample() {
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        BasicAlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                openDialog.value = false
            }
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "This area typically contains the supportive text " +
                                "which presents the details regarding the Dialog's purpose.",
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    TextButton(
                        onClick = {
                            openDialog.value = false
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TopAppComponentPreview() {
    val navController = rememberNavController()
    TopAppComponent(
        modifier = Modifier,
        navController = navController
    )
}