package com.example.learnstack.topics

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.learnstack.viewmodels.TopicModel


@Composable
fun TopicItem(
    topic: TopicModel,
    index: Int, // Adding index for numbering
    navController: NavHostController, // Add navController for navigation
    onTopicClick: () -> Unit // Callback to handle click events
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(80.dp) // Increased card height
            .clickable(
                interactionSource = remember { MutableInteractionSource() }, // No interaction source
                indication = null // Disable ripple effect
            ) {
                // Handle navigation when the item is clicked
                val encodedTopicTitle = Uri.encode(topic.title)
                navController.navigate("topic_detail/$encodedTopicTitle")

                // Call the onTopicClick callback when the topic is clicked
                onTopicClick()
            },
        elevation = CardDefaults.cardElevation(4.dp), // Optional: Add shadow effect to the card
        shape = MaterialTheme.shapes.medium, // Optional: Make the card corners rounded
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface, // Use the theme's surfaceVariant color
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer // Use the corresponding text/icon color
        )
    ) {
        // Box to position the content inside the card
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically, // Center items vertically within the row
                horizontalArrangement = Arrangement.Start, // Ensure content starts from the left
                modifier = Modifier.fillMaxSize() // Take up all available space
            ) {
                Spacer(modifier = Modifier.width(22.dp))
                // Index text
                Text(
                    text = "$index.", // Display the topic number
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = 24.sp, // Larger font size for the index
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface // Gray or dynamic color
                    ),
                    modifier = Modifier.padding(end = 12.dp) // Space between number and title
                )

                Spacer(modifier = Modifier.width(8.dp)) // Space between index and title
                Text(
                    text = topic.title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 18.sp, // Slightly larger font size for the title
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
