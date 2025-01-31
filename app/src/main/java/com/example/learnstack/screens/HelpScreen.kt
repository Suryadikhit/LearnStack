package com.example.learnstack.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HelpScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Added padding around the entire screen
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center) // Center content vertically and horizontally
                .padding(16.dp) // Padding around the column
        ) {
            Text(
                text = "Welcome to the Help Screen!",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.padding(bottom = 12.dp) // Padding below title for separation
            )

            Text(
                text = "For support or inquiries, feel free to reach out to us at:",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.padding(bottom = 8.dp) // Padding between sections
            )

            // Displaying the email professionally
            Text(
                text = "Email: skantadikhit@gmail.com",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.padding(bottom = 16.dp) // Padding to separate email and closing note
            )

            // Additional helpful message or closing note
            Text(
                text = "We're here to assist you with any questions you may have.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}


