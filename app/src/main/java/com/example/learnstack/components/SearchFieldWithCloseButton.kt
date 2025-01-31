package com.example.learnstack.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun SearchFieldWithCloseButton(
    searchQuery: MutableState<String>,
    onSearchClose: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        SearchField(
            searchQuery = searchQuery.value,
            onSearchQueryChanged = { newQuery -> searchQuery.value = newQuery }
        )
        IconButton(onClick = onSearchClose) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Close Search",
                tint = Color.Black
            )
        }
    }
}
