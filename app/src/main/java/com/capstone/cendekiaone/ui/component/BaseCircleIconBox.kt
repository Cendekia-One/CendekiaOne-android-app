package com.capstone.cendekiaone.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BaseCircleIconBox(
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    iconBackgroundColor: Color = colorScheme.primary,
    onClick: (() -> Unit)? = null,
    icon: @Composable BoxScope.() -> Unit = {}
) {
    if (onClick != null) {
        Box(
            modifier = modifier
                .clip(CircleShape)
                .background(
                    color = iconBackgroundColor
                )
                .size(28.dp)
                .clickable(enabled = enabled, onClick = onClick)
                .padding(6.dp)
        ) {
            icon()
        }
    } else {
        Box(
            modifier = modifier
                .clip(CircleShape)
                .background(
                    color = iconBackgroundColor
                )
                .size(28.dp)
                .padding(6.dp)
        ) {
            icon()
        }
    }
}