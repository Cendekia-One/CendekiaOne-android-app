package com.capstone.cendekiaone.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.capstone.cendekiaone.ui.theme.myFont

@Composable
fun OutlinedButtonComponent(
    provideText: String,
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit,
) {
    OutlinedButton(
        onClick = { onClick() },
        modifier = modifier
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

@Preview(showSystemUi = true)
@Composable
fun exampleUseBtnOutlined() {
    Box(contentAlignment = Alignment.Center) {

        OutlinedButtonComponent(
            provideText = "Contoh",
            modifier = Modifier
                .width(126.dp)
                .height(40.dp)
                .padding(start = 16.dp, top = 10.dp, end = 24.dp, bottom = 10.dp),
            icon = {
                Icon(
                    imageVector = Icons.Outlined.AddCircleOutline,
                    contentDescription = "add circle",
                )
            },
        ) {

        }
    }
}
