package com.capstone.cendekiaone.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.capstone.cendekiaone.R
import com.capstone.cendekiaone.ui.navigation.NavigationItem
import com.capstone.cendekiaone.ui.navigation.Screen
import com.capstone.cendekiaone.ui.theme.myFont

@Composable
fun NavigationBarComponent(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    var showDialog by remember { mutableStateOf(false) }

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

    NavigationBar(
        modifier = modifier,
        containerColor = Color.Transparent,
        contentColor = Color.DarkGray,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val navigationItems = listOf(
            NavigationItem(
                title = stringResource(R.string.menu_home),
                icon = R.drawable.ic_home_outline,
                selectedIcon = R.drawable.ic_home_filled,
                screen = Screen.Home,
            ),
            NavigationItem(
                title = stringResource(R.string.menu_explore),
                icon = R.drawable.ic_explore_outline,
                selectedIcon = R.drawable.ic_exlore_filled,
                screen = Screen.Explore,
            ),
            NavigationItem(
                title = stringResource(R.string.menu_create),
                icon = R.drawable.ic_create_outline,
                selectedIcon = R.drawable.ic_create_filled,
                screen = Screen.Create,
            ),
            NavigationItem(
                title = stringResource(R.string.menu_brief),
                icon = R.drawable.ic_magic_outline,
                selectedIcon = R.drawable.ic_magic_filled,
                screen = Screen.Brief,
            ),
            NavigationItem(
                title = stringResource(R.string.menu_profile),
                icon = R.drawable.ic_profile_outline,
                selectedIcon = R.drawable.ic_profile_filled,
                screen = Screen.Profile,
            ),
        )
        navigationItems.map { item ->
            NavigationBarItem(
                icon = {
                    val iconPainter: Painter = painterResource(id = if (currentRoute == item.screen.route) item.selectedIcon else item.icon)
                    Icon(
                        painter = iconPainter,
                        contentDescription = item.title,
                        modifier = Modifier.testTag("${item.screen.route}_Icon")
                    )
                },
                label = {
                    Text(
                        item.title,
                        fontFamily = myFont,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                selected = currentRoute == item.screen.route,
                onClick = {
                    if (item.screen == Screen.Brief) {
                        // Show dialog when "Brief" navigation item is pressed
                        showDialog = true
                    } else {
                        navController.navigate(item.screen.route)
                    }
                },
            )
        }
    }
}