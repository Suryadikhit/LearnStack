@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.learnstack.ui.theme

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopBar(
    title: String,
    onMenuClicked: () -> Unit,
    onSearchClicked: () -> Unit
) {


    TopAppBar(
        title = {
            Text(
                text = title,
                color = Color.White, // White text color
                fontSize = 23.sp, // Font size of 20sp
                fontWeight = FontWeight.ExtraBold, // ExtraBold weight
                textAlign = TextAlign.Center, // Center the text
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 13.dp) // Fill the width of the container
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                onMenuClicked()
            }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        actions = {
            IconButton(onClick = { onSearchClicked() }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF0E1629) // Set the background color here
        ),

        )
}
