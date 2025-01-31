package com.example.learnstack.components

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.learnstack.viewmodels.BoxModel

@Composable
fun SquareCard(
    title: String,
    iconResId: Int? = null,  // Optional local resource ID
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(3.dp)
            .size(100.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() }, // Disable interaction animations
                indication = null, // Disable ripple effect
                onClick = onClick
            ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant // Theme-based background
        )
    ) {
        Box(contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                iconResId?.let {
                    androidx.compose.foundation.Image(
                        painter = painterResource(id = it),
                        contentDescription = title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(55.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun SquareCardGrid(
    roadmapName: String, // Pass the roadmap name as roadmapId
    roadmapTopics: List<BoxModel>, // List of com.example.learnstack.viewmodels.BoxModel objects
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
        ) {
            val rows = roadmapTopics.chunked(3) // Split the list into chunks of 3 items each

            // Create rows dynamically
            rows.forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    row.forEach { boxModel ->
                        SquareCard(
                            title = boxModel.name,
                            iconResId = boxModel.iconResId,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                // Encode the roadmapName and boxName to safely pass them in the URL
                                val encodedRoadmapName = Uri.encode(roadmapName)
                                val encodedBoxName = Uri.encode(boxModel.name)
                                // Navigate to the SquareBoxDetailScreen, passing the encoded parameters
                                navController.navigate("box_detail/$encodedRoadmapName/$encodedBoxName")
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(5.dp)) // Add spacing between rows
            }
        }
    }
}
