package com.example.learnstack.topics

//noinspection UsingMaterialAndMaterial3Libraries
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnstack.R
import com.example.learnstack.components.BookmarkIcon
import com.example.learnstack.viewmodels.MainViewModel
import com.example.learnstack.viewmodels.TopicModel
import kotlinx.coroutines.delay

@Composable
fun TopicDetailScreen(
    topic: TopicModel,
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    // Observe the updated bookmark state for the current topic
    val bookmarkedTopics by viewModel.bookmarkedTopics.observeAsState(emptyList())

    // Find the current topic's bookmark state
    val isBookmarked = bookmarkedTopics.any { it.title == topic.title && it.isBookmarked }

    // State to control the visibility of the pop-up
    var showPopup by remember { mutableStateOf(false) }
    var popupMessage by remember { mutableStateOf("") }

    // Pop-up logic after the bookmark state is toggled
    val onBookmarkClick: () -> Unit = {
        try {
            val newBookmarkState = !isBookmarked
            viewModel.toggleBookmark(topic.title, newBookmarkState)
            // Update pop-up message based on bookmark state
            popupMessage = if (newBookmarkState) "Added to bookmarks" else "Removed from bookmarks"
            showPopup = true
        } catch (e: Exception) {
            Log.e("BookmarkIcon", "Error toggling bookmark for ${topic.title}", e)
        }
    }

    // Automatically hide the pop-up after 2 seconds
    if (showPopup) {
        LaunchedEffect(Unit) {
            delay(1000)
            showPopup = false
        }
    }

    // Main UI Layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp, top = 44.dp), // Increased top padding for the Row
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }



        Spacer(modifier = Modifier.height(10.dp))

        // Scrollable content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = topic.title,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 18.dp)
                    )

                    // Bookmark Icon with the onClick functionality
                    BookmarkIcon(
                        topicTitle = topic.title,
                        isBookmarked = isBookmarked,
                        viewModel = viewModel,
                        onClick = onBookmarkClick // pass the click handler here
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Divider(modifier = Modifier.height(1.dp), color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))
                DescriptionText(
                    description = topic.description
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Divider(
                        modifier = Modifier
                            .weight(0.1f)
                            .height(1.dp),
                        color = Color(0xFF15A349)
                    )

                    Surface(
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(horizontal = 8.dp),
                        shape = MaterialTheme.shapes.medium,
                        border = BorderStroke(1.dp, Color(0xFF15A349)),
                        color = Color.White
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
                        ) {
                            Text(
                                text = "Free Resources",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color(0xFF15A349),
                                maxLines = 1
                            )
                        }
                    }

                    Divider(
                        modifier = Modifier
                            .weight(0.8f)
                            .height(1.dp),
                        color = Color(0xFF15A349)
                    )
                }
            }

            item {
                LinkList(resources = topic.resources)
            }
        }
    }

    // Pop-up card for feedback
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = showPopup,
            enter = slideInVertically(
                initialOffsetY = { it }, // Start from the bottom
                animationSpec = tween(100) // Faster slide-in (300ms)
            ) + fadeIn(animationSpec = tween(100)), // Faster fade-in (200ms)
            exit = slideOutVertically(
                targetOffsetY = { it }, // Slide out to the bottom
                animationSpec = tween(100) // Faster slide-out (300ms)
            ) + fadeOut(animationSpec = tween(100)) // Faster fade-out (200ms)
        )
        {
            Card(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(120.dp)
                    .padding(bottom = 40.dp)
                    .shadow(4.dp, shape = MaterialTheme.shapes.medium), // Adjust overlap
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = MaterialTheme.shapes.medium,

                ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Bookmark Icon
                    Icon(
                        painter = painterResource(id = R.drawable.bookmarked_icon), // Replace with your bookmark icon
                        contentDescription = "Bookmark Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp)) // Change height to width for horizontal alignment

                    // Popup Message
                    Text(
                        text = popupMessage,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp
                    )
                }

            }
        }
    }
}
