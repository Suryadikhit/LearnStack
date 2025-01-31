package com.example.learnstack.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchField(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    placeholder: String = "Search...",
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(45.dp)
            .background(
                color = MaterialTheme.colorScheme.surface, // Use a themed background color
                shape = MaterialTheme.shapes.medium // Apply rounded corners
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline, // Use theme-based outline color
                shape = MaterialTheme.shapes.medium
            ),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        BasicTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChanged,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search // Set the IME action to "Search"
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide() // Hide keyboard on Search
                }
            ),
            modifier = Modifier
                .weight(1f) // Allow text input to take up available space
                .padding(start = 12.dp), // Add padding to the text
            decorationBox = { innerTextField ->
                if (searchQuery.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            fontSize = 16.sp
                        ),
                        textAlign = TextAlign.Start
                    )
                }
                innerTextField() // Draw the text input
            }
        )

        // Clear (Cross) Icon
        if (searchQuery.isNotEmpty()) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Clear Search",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(20.dp)
                    .clickable {
                        onSearchQueryChanged("") // Clear the search query

                    }
            )
        }
    }
}
