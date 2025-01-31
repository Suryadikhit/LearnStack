package com.example.learnstack.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun GreetingSection(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp)
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Row {
                Text(
                    text = "\uD83D\uDC4B",
                    fontSize = 30.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Let's learn together!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground // Dynamically set text color
                )
            }
            Text(
                text = "Explore different roadmaps to enhance your skills and career.",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), // Softer text color
                modifier = Modifier.padding(top = 4.dp, start = 45.dp)
            )
            Row(modifier = Modifier.padding(start = 20.dp)) {
                ActionCardsRow(navController = navController)
            }
        }
    }
}
