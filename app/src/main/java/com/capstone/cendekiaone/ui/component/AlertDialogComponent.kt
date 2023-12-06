package com.capstone.cendekiaone.ui.component


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun AlertDialogComponent(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onDismiss.invoke()
        },
        title = {
            Text(text = "Logout Confirmation")
        },
        text = {
            Text(text = "Are you sure you want to logout?")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm.invoke()
                }
            ) {
                Text("Logout")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss.invoke()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}