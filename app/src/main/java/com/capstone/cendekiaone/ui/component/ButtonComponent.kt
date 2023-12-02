package com.capstone.cendekiaone.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.capstone.cendekiaone.ui.theme.myFont


@Composable
fun ButtonComponent(
    modifier: Modifier = Modifier,
    provideText: String,
    colors: ButtonColors = ButtonDefaults.filledTonalButtonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    ),
    icon: @Composable (() -> Unit)? = null,
    shape: Shape = ButtonDefaults.shape,
    onClick: () -> Unit,
) {
    Button(
        onClick = { onClick() },
        modifier = modifier,
        colors = colors,
        shape = shape
    ) {
        if (icon != null) {
            icon()
            Spacer(modifier = Modifier.padding(8.dp))
        } else {
            null
        }
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