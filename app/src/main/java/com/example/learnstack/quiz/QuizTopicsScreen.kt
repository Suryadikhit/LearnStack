package com.example.learnstack.quiz

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.learnstack.R

data class QuizTopic(val name: String, val iconRes: Int)

val topics = listOf(
    QuizTopic("HTML", R.drawable.html_icon),
    QuizTopic("CSS", R.drawable.css_icon),
    QuizTopic("JavaScript", R.drawable.javascript),
    QuizTopic("React", R.drawable.react),
    QuizTopic("Python", R.drawable.python_icon),
    QuizTopic("Django", R.drawable.django),
    QuizTopic("AI", R.drawable.ai),
    QuizTopic("ML", R.drawable.ml),
    QuizTopic("Data Science", R.drawable.data_science)
)


@Composable
fun QuizTopicsScreen(navController: NavController) {
    // Apply padding to the whole column layout, including the top
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp) // Adjust the padding at the top here
    ) {
        // Top bar with back icon
        TopBar(navController)

        Spacer(modifier = Modifier.height(16.dp))

        // LazyVerticalGrid for quiz topics
        LazyVerticalGrid(
            columns = GridCells.Fixed(3), // This will create 3 columns
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp), // Padding for sides of grid
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(topics) { topic ->
                QuizTopicCard(topic, navController)
            }
        }
    }
}

@Composable
fun TopBar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp), // Padding from the left side for the top bar
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Surface with back icon
        Surface(
            modifier = Modifier
                .size(40.dp)
                .padding(top = 10.dp),
            shape = RoundedCornerShape(30),
            color = MaterialTheme.colorScheme.primaryContainer,
            shadowElevation = 4.dp
        ) {
            IconButton(
                onClick = { navController.popBackStack() } // Pop the navigation stack to go back
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
fun QuizTopicCard(topic: QuizTopic, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(3.dp)
            .size(100.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() }, // Disable interaction animations
                indication = null, // Disable ripple effect
                onClick = {
                    // Navigate to the quiz screen with the selected topic
                    navController.navigate("quizScreen/${topic.name}")
                }
            ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant // Theme-based background
        )
    ) {
        // Display only the icon without the text
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = topic.iconRes),
                contentDescription = topic.name,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}
