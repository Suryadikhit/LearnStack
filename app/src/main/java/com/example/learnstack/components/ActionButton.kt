package com.example.learnstack.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.learnstack.R


@Composable
fun ActionCard(
    text: String,
    iconResId: Int,
    onClick: () -> Unit,
    width: Dp = 100.dp // Default width if not provided
) {
    // Access custom page-specific colors
    val backgroundColor = MaterialTheme.colorScheme.primaryContainer // Page-specific card color
    val textColor = MaterialTheme.colorScheme.onPrimaryContainer // Page-specific text/icon color

    Card(
        modifier = Modifier
            .padding(6.dp) // Padding around each card
            .height(35.dp)
            .width(width) // Set dynamic width for each card
            .clickable(onClick = onClick), // Set click action here
        shape = RoundedCornerShape(8.dp), // Rounded corners
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = backgroundColor // Specific container color
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), // Ensure row takes full width
            verticalAlignment = Alignment.CenterVertically, // Vertically align the icon and text
            horizontalArrangement = Arrangement.Center // Align content to the center of the row
        ) {
            Icon(
                painter = painterResource(id = iconResId), // Load the icon
                contentDescription = "$text Icon",
                modifier = Modifier
                    .size(30.dp) // Set icon size
                    .padding(end = 8.dp), // Space between icon and text
                tint = textColor // Specific icon color
            )
            Text(
                text = text,
                fontSize = 16.sp, // Adjust font size
                color = textColor // Specific text color
            )
        }
    }
}


@Composable
fun ActionCardsRow(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth() // Ensure row takes the full width
            .padding(16.dp), // Outer padding for the row
        horizontalArrangement = Arrangement.spacedBy(7.dp), // Space between cards
        verticalAlignment = Alignment.CenterVertically // Align cards vertically
    ) {
        // Quiz Card (Default width)
        ActionCard(
            text = "Quizzes",
            iconResId = R.drawable.quiz, // Replace with your quiz icon resource
            onClick = {
                navController.navigate("quizzes") // Navigate to the quizzes screen
            }
        )

        // IQ Card with reduced width
        ActionCard(
            text = "Notes",
            iconResId = R.drawable.iq_logo, // Replace with your IQ icon resource
            onClick = {
                navController.navigate("Notes") // Navigate to the IQ screen
            },
            width = 80.dp // Reduced width for the IQ card
        )
    }
}
