package com.example.learnstack.notes

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowPdfScreen(
    fileName: String,
    navController: NavController,
    context: Context,
    viewModel: MainViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val isSaving = remember { mutableStateOf(false) }  // Track the save progress
    val errorOccurred = remember { mutableStateOf(false) }  // Track errors


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


    val activityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val uri = result.data?.data
        if (uri != null) {
            coroutineScope.launch {
                isSaving.value = true // Set saving status
                savePdfToUri(context, uri, fileName)
                isSaving.value = false // Reset saving status
            }
        } else {
            Toast.makeText(context, "No location selected", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = fileName,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 12.dp)
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
                                navController.popBackStack()
                            }
                            .padding(8.dp, top = 15.dp)
                            .size(24.dp)
                    )
                },
                actions = {
                    Icon(
                        painter = painterResource(id = R.drawable.download),
                        contentDescription = "Download",
                        tint = Color.White,
                        modifier = Modifier
                            .clickable {
                                showFileManagerToSavePdf(fileName, activityLauncher)
                            }
                            .padding(8.dp, top = 15.dp, end = 10.dp)
                            .size(24.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0E1629)
                ),
                modifier = Modifier.height(85.dp)
            )
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                PdfView(fileName = fileName)
            }
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Show loading indicator when saving
        if (isSaving.value) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        // Show error message if saving fails
        if (errorOccurred.value) {
            Snackbar(
                modifier = Modifier.align(Alignment.BottomCenter),
                action = {
                    Button(onClick = { errorOccurred.value = false }) {
                        Text("Dismiss")
                    }
                }
            ) {
                Text("Failed to save the PDF.")
            }
        }
    }
}

fun showFileManagerToSavePdf(
    fileName: String,
    launcher: ActivityResultLauncher<Intent>
) {
    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "application/pdf"
        putExtra(Intent.EXTRA_TITLE, fileName) // Suggest the file name
    }
    launcher.launch(intent) // Launch the file picker
}

suspend fun savePdfToUri(context: Context, uri: Uri, fileName: String) {
    // Get the PDF URL from Firebase Storage
    val url = getPdfUrlFromFirebase(fileName)

    // If URL is not null, retrieve the PDF from the cache or download it
    url?.let {
        val cachedPdfFile = retrievePdfFromCacheOrUrl(context, it)

        if (cachedPdfFile != null) {
            // PDF file found, proceed to save it to the user-selected URI
            savePdfFileToUri(context, uri, cachedPdfFile)
        } else {
            // Show error message if file is not found
            Toast.makeText(context, "Failed to retrieve the PDF.", Toast.LENGTH_SHORT).show()
        }
    } ?: run {
        // If URL is null, show error
        Toast.makeText(context, "PDF URL is invalid.", Toast.LENGTH_SHORT).show()
    }
}

private fun savePdfFileToUri(context: Context, uri: Uri, file: File) {
    try {
        // Open an input stream from the cached PDF file
        val inputStream: InputStream = file.inputStream()

        // Open an output stream to the URI where the file will be saved
        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            // Copy the data from the input stream (PDF file) to the output stream (URI location)
            inputStream.copyTo(outputStream)
        }

        // Notify the user that the file was saved successfully
        Toast.makeText(context, "File saved successfully!", Toast.LENGTH_SHORT).show()

        // Open the PDF after saving
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Open PDF with"))

    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Failed to save file: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}
