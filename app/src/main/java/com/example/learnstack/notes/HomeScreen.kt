package com.example.learnstack.notes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.learnstack.R
import com.example.learnstack.viewmodels.MainViewModel
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onCardClick: (String) -> Unit,
    viewModel: MainViewModel,
    navController: NavController // Pass navController to navigate to PDF screen
) {

    // Dynamically load PDF file names from Firebase Storage
    val pdfFiles = remember { mutableStateListOf<String>() }
    val isLoading = remember { mutableStateOf(true) }



    LaunchedEffect(navController) {
        // Disable the drawer when entering the screen
        viewModel.setDisableDrawer(true)
    }

    DisposableEffect(navController) {
        // Create the listener instance
        val listener = NavController.OnDestinationChangedListener { _, _, _ ->
            viewModel.setDisableDrawer(false)
        }

        // Attach the listener to the NavController
        navController.addOnDestinationChangedListener(listener)

        // Cleanup listener when the composable is removed
        onDispose {
            // Remove the listener when the composable is disposed of
            navController.removeOnDestinationChangedListener(listener)
        }
    }




    LaunchedEffect(Unit) {
        try {
            // Get a reference to Firebase Storage
            val storageRef = FirebaseStorage.getInstance().reference.child("pdfs/")

            // List all files in the 'pdfs' folder
            val result = storageRef.listAll().await()

            // Filter out only the files that end with ".pdf"
            val pdfFileNames = result.items
                .map { it.name } // Get the file names
                .filter { it.endsWith(".pdf") } // Filter for PDF files
                .map { it.substringBeforeLast(".pdf") }
            // Add the PDF file names to the list
            pdfFiles.addAll(pdfFileNames)
            isLoading.value = false
        } catch (e: Exception) {
            e.printStackTrace() // Handle error while fetching files
            isLoading.value = false
        }
    }

    // Scaffold with top bar and content
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
                            text = "Notes",
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
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Dynamically create rows of cards
                    pdfFiles.chunked(2).forEach { rowFiles ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            rowFiles.forEach { fileName ->
                                ClickableCard(
                                    text = fileName,
                                    modifier = Modifier.weight(1f),
                                    onClick = { onCardClick(fileName) },
                                    iconResId = R.drawable.notes
                                )
                            }
                            // If there's an odd number of items, fill the space with an empty Box
                            if (rowFiles.size < 2) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
                if (isLoading.value) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

            }
        }
    )
}


@Composable
fun ClickableCard(
    text: String,
    iconResId: Int, // Icon from drawable resources
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .aspectRatio(1f) // Make the card square
            .clickable { onClick() }, // Click handling
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Upper part of the card with icon
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.White), // First part color
                contentAlignment = Alignment.CenterStart
            ) {
                // Use painterResource to load the icon from drawables
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = "Icon",
                    modifier = Modifier
                        .size(70.dp)
                        .padding(start = 10.dp), // Icon size
                )
            }

            // Lower part of the card with text
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(.5f)
                    .background(Color(0xFFF7F7FF)), // Second part color
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    fontSize = 16.sp,
                    color = Color.Black // Text color
                )
            }
        }
    }
}
