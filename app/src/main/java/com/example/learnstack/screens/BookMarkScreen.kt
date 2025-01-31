package com.example.learnstack.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.learnstack.R
import com.example.learnstack.components.BookmarkIcon
import com.example.learnstack.roomdata.TopicEntity
import com.example.learnstack.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(viewModel: MainViewModel = viewModel(), navController: NavController) {
    // Observing bookmarked topics from the ViewModel
    val bookmarkedTopics by viewModel.bookmarkedTopics.observeAsState(emptyList())

    // Scaffold to include a TopBar
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .padding(end = 20.dp) // Offset to prevent overlap with back icon
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Bookmarks",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            ),
                            textAlign = TextAlign.Center, modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier
                            .clickable {
                                navController.popBackStack() // Navigate back
                            }
                            .padding(8.dp, top = 15.dp)
                            .size(24.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0E1629) // Dynamic background color
                ),
                modifier = Modifier.height(85.dp)
            )
        }
    ) { paddingValues ->
        // Check if the list is empty and show an appropriate message
        if (bookmarkedTopics.isEmpty()) {
            Text(
                text = "No bookmarks yet",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground, // Dynamic text color
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize()
            )
        } else {
            // Display the list of bookmarked topics
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background) // Dynamic background color
                    .padding(8.dp)
                    .padding(top = paddingValues.calculateTopPadding())
            ) {
                items(bookmarkedTopics) { bookmark ->
                    BookmarkCard(
                        bookmark = bookmark,
                        viewModel = viewModel,
                        onTopicSelected = { topicTitle ->
                            Log.d("BookmarkScreen", "Navigating to topic: $topicTitle")
                            if (topicTitle.isNotEmpty()) {
                                val encodedTopicTitle = Uri.encode(topicTitle)
                                navController.navigate("topic_detail/$encodedTopicTitle")
                            } else {
                                Log.e("BookmarkScreen", "Topic title is empty, cannot navigate.")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BookmarkCard(
    bookmark: TopicEntity,
    viewModel: MainViewModel,
    onTopicSelected: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(80.dp)
            .clickable { onTopicSelected(bookmark.title) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface // Dynamic card background color
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Logo or Image at the start of the card
            Image(
                painter = painterResource(id = R.drawable.bookmarkedtopic_icon),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 16.dp)
            )

            // Display the title of the bookmark
            Text(
                text = bookmark.title,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                color = MaterialTheme.colorScheme.onSurface, // Dynamic text color
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp, top = 9.dp),
                textAlign = TextAlign.Start
            )

            // BookmarkIcon to toggle bookmark status
            BookmarkIcon(
                isBookmarked = bookmark.isBookmarked,
                topicTitle = bookmark.title,
                viewModel = viewModel,
                onClick = {}
            )
        }
    }
}
