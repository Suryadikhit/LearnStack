package com.example.learnstack.topics

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.learnstack.viewmodels.BoxModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SquareBoxDetailScreen(
    box: BoxModel,                     // Box within the roadmap
    onBackClick: () -> Unit,
    navController: NavHostController
) {
    val topics by remember { mutableStateOf(box.topics) }
    topics.size
    val progress = remember { mutableIntStateOf(0) } // Tracks number of topics clicked

    // Initialize topicClickState with an empty map, as it is mutable
    val topicClickState =
        remember { mutableStateOf(mapOf<String, Boolean>()) } // Track clicked state of each topic
    val loading by remember { mutableStateOf(false) }
    val errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Apply theme's background color
    ) {
        // Top App Bar with box name centered and back icon at the start
        TopAppBar(
            title = {
                Box(
                    modifier = Modifier
                        .padding(end = 35.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = box.name, // Display box name in the top bar
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold, // Make the text bold
                            color = Color.White // Set the text color to white
                        ),
                        textAlign = TextAlign.Center // Center align the text
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(0xFF0E1629) // Custom color for the top bar
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Show error message if there's any error
        if (errorMessage.isNotEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error, // Use theme's error color
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 18.sp)
                )
            }
        }

        // When topics are available, display them in a LazyColumn
        if (topics.isNotEmpty() && !loading && errorMessage.isEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(topics) { topic ->
                    val index = topics.indexOf(topic) + 1

                    // Pass the callback to TopicItem
                    TopicItem(
                        topic = topic,
                        index = index,
                        navController = navController,
                        onTopicClick = {
                            // Mark topic as clicked
                            val updatedState = topicClickState.value.toMutableMap()
                            updatedState[topic.title] = true
                            topicClickState.value = updatedState

                            // Update progress state
                            progress.intValue = updatedState.count { it.value }
                        }
                    )
                }
            }
        }
    }

    // Back Handler to navigate back to the previous screen
    BackHandler {
        onBackClick()
    }
}

