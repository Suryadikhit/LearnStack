package com.example.learnstack.topics

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnstack.viewmodels.ResourceItem
import java.util.Locale

@Composable
fun LinkList(resources: List<ResourceItem>) {
    val context = LocalContext.current

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)) {
        resources.forEach { resource ->
            // Determine the background color based on the type
            val backgroundColor = when (resource.type.lowercase()) {
                "article" -> Color(0xFFFCDE47) // Slightly darker blue
                "video" -> Color(0xFFD9B5FF)   // Slightly darker red
                "feed" -> Color(0xFFCE3DF2)    // Slightly darker green
                "course" -> Color(0xFF2664EB)  // Slightly darker yellow
                else -> Color(0xFFFCDE47)      // Default neutral gray
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Surface containing the type (e.g., Article, Video)
                Surface(
                    modifier = Modifier
                        .wrapContentSize()
                        .clip(RoundedCornerShape(5.dp)) // Manually set rounded corners
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            // Open the link in a browser
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(resource.link))
                            context.startActivity(intent)
                        },
                    color = backgroundColor, // Set the background color
                    tonalElevation = 4.dp // Add elevation for shadow effect
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(horizontal = 8.dp), // Padding inside the surface
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = resource.type.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                            },
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp, // Slightly larger font for better visibility

                            )
                        )
                    }
                }

                // Spacer for separation between the surface and title
                Spacer(modifier = Modifier.width(9.dp))

                // Title with underline
                Text(
                    text = resource.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp, // Adjusted font size for better readability
                        textDecoration = TextDecoration.Underline,
                        color = MaterialTheme.colorScheme.tertiary // Darker text color for better contrast
                    ),
                    modifier = Modifier
                        .clickable {
                            // Open the link in a browser
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(resource.link))
                            context.startActivity(intent)
                        }
                )
            }
        }
    }
}
