package com.capstone.cendekiaone.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.capstone.cendekiaone.ui.theme.myFont

@Composable
fun ButtonComponent(
    provideText: String,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(provideText, fontFamily = myFont)
    }
}

@Composable
fun TextButtonComponent(
    provideText: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = { onClick() }
    ) {
        Text(provideText, fontFamily = myFont)
    }
}