package com.example.learnstack.components

import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.learnstack.R
import com.example.learnstack.viewmodels.MainViewModel

@Composable
fun BookmarkIcon(
    topicTitle: String,
    isBookmarked: Boolean,
    viewModel: MainViewModel,
    onClick: () -> Unit
) {
    IconButton(onClick = {
        try {
            onClick()
            // Toggle bookmark state in ViewModel
            viewModel.toggleBookmark(topicTitle, isBookmarked)
        } catch (e: Exception) {
            Log.e("BookmarkIcon", "Error toggling bookmark for $topicTitle", e)
        }
    }) {
        Icon(
            painter = painterResource(
                id = if (isBookmarked) R.drawable.bookmarked_icon else R.drawable.bookmark_icon
            ),
            contentDescription = if (isBookmarked) "Remove Bookmark" else "Add Bookmark",
            tint = if (isBookmarked) MaterialTheme.colorScheme.primary else Color.Gray,
            modifier = Modifier.size(24.dp)
        )
    }
}
