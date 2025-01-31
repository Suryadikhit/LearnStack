package com.example.learnstack.topics

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DescriptionText(description: String) {
    // Clean up the input string to handle any extra spaces and split it into paragraphs
    val cleanedDescription = description.trim()
    val paragraphs = cleanedDescription.split("\n")
        .map { it.trim() } // Trim each paragraph to remove leading/trailing spaces
        .filter { it.isNotEmpty() } // Only keep non-empty paragraphs

    paragraphs.forEach { paragraph ->
        // Split each paragraph into words
        val words = paragraph.split(" ")
        val firstWord = words.getOrNull(0) ?: ""
        val remainingText = words.drop(1).joinToString(" ")

        // Build AnnotatedString for inline styling
        val styledText = buildAnnotatedString {
            // Add the first word with bold styling
            append(
                AnnotatedString(
                    text = firstWord,
                    spanStyle = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary, // Use primary color for the first word
                        fontSize = 18.sp
                    )
                )
            )

            // Add a space and the remaining text with normal styling
            append(" ")
            append(
                AnnotatedString(
                    text = remainingText,
                    spanStyle = SpanStyle(
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onBackground, // Use onBackground color for the remaining text
                        fontSize = 18.sp
                    )
                )
            )
        }

        // Render the styled text for each paragraph
        Text(
            text = styledText,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )

        // Add a spacer between paragraphs
        Spacer(modifier = Modifier.height(16.dp))
    }
}
