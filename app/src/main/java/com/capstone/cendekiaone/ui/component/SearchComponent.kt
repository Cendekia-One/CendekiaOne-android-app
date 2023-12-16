package com.capstone.cendekiaone.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.capstone.cendekiaone.R
import com.capstone.cendekiaone.ui.navigation.Screen
import com.capstone.cendekiaone.ui.theme.myFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchComponent(
    query: String,
    onQueryChange: (String) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    ProvideTextStyle(
        value = TextStyle(
            fontFamily = myFont, // Replace with your desired fontFamily
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp // Adjust the fontSize as needed
        )
    ) {
        Row(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            SearchBar(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = {  },
                active = false,
                onActiveChange = { navController.navigate(Screen.Search.route) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search_outline),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                placeholder = {
                    Text(
                        stringResource(R.string.search),
                        color = Color.Black,
                        fontSize = 15.sp,
                    )
                },
                colors = SearchBarDefaults.colors(
                    MaterialTheme.colorScheme.secondaryContainer,
                    inputFieldColors = SearchBarDefaults.inputFieldColors(Color.Black),
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("Search_Bar")
            ) {}
        }
    }
}