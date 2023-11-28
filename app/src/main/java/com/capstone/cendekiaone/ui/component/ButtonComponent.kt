package com.capstone.cendekiaone.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.capstone.cendekiaone.ui.theme.myFont



@Composable
fun ButtonComponent(
    provideText: String,
    colors: ButtonColors = ButtonDefaults.filledTonalButtonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        disabledContentColor = MaterialTheme.colorScheme.onPrimary
    ),
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = { onClick() },
        modifier = modifier,
        colors = colors
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