package com.example.learnstack.notes

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

@Composable
fun PdfView(fileName: String) {
    val context = LocalContext.current
    val isLoading = remember { mutableStateOf(true) }
    val errorOccurred = remember { mutableStateOf(false) }

    // Ensure that the file name includes the .pdf extension
    val cleanedFileName = if (fileName.endsWith(".pdf")) fileName else "$fileName.pdf"

    Box(modifier = Modifier.fillMaxSize()) {
        val pdfView = rememberPdfView(context, cleanedFileName, isLoading, errorOccurred)

        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 30.dp),
            factory = { pdfView }
        )

        if (isLoading.value) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        if (errorOccurred.value) {
            // Show error message like Snackbar or Toast
            Toast.makeText(context, "Error loading PDF", Toast.LENGTH_SHORT).show()
        }
    }
}


@Composable
private fun rememberPdfView(
    context: Context,
    fileName: String,
    isLoading: MutableState<Boolean>,
    errorOccurred: MutableState<Boolean>
): PDFView {
    val pdfView = remember { PDFView(context, null) }
    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(fileName) {
        val job = coroutineScope.launch {
            try {
                val url = getPdfUrlFromFirebase(fileName)
                if (url != null) {
                    val cachedFile = retrievePdfFromCacheOrUrl(context, url)
                    cachedFile?.let { file ->
                        val inputStream = context.contentResolver.openInputStream(file.toUri())
                        pdfView.fromStream(inputStream)
                            .enableSwipe(true)
                            .enableDoubletap(true)
                            .scrollHandle(DefaultScrollHandle(context))
                            .defaultPage(0)
                            .load()
                        isLoading.value = false
                    } ?: run {
                        errorOccurred.value = true
                        isLoading.value = false
                    }
                } else {
                    errorOccurred.value = true
                    isLoading.value = false
                }
            } catch (e: Exception) {
                errorOccurred.value = true
                isLoading.value = false
            }
        }

        onDispose {
            job.cancel()
            pdfView.recycle()
        }
    }

    return pdfView
}


internal suspend fun getPdfUrlFromFirebase(fileName: String): String? {
    return withContext(Dispatchers.IO) {
        Log.d(
            "Firebase",
            "Attempting to get PDF URL for file: $fileName"
        ) // Log when the function is called
        val storageRef: StorageReference =
            FirebaseStorage.getInstance().reference.child("pdfs/$fileName")

        try {
            Log.d(
                "Firebase",
                "Fetching download URL for: $fileName"
            ) // Log before attempting to fetch the URL
            val url = storageRef.downloadUrl.await() // Fetch the download URL
            Log.d(
                "Firebase",
                "Successfully fetched download URL: $url"
            ) // Log when the URL is successfully fetched
            return@withContext url.toString() // Return the URL as a string
        } catch (e: Exception) {
            Log.e(
                "Firebase",
                "Error fetching PDF URL for file $fileName: ${e.message}",
                e
            ) // Log error with stack trace
            return@withContext null // Return null if there is an error
        }
    }
}

internal suspend fun retrievePdfFromCacheOrUrl(context: Context, url: String?): File? {
    return withContext(Dispatchers.IO) { // Switch to the IO thread for network operations
        try {
            Log.d("PdfView", "Attempting to retrieve PDF from cache or URL: $url")
            // Create or use a cache directory in the app's internal storage
            val cacheDir = File(context.cacheDir, "pdf_cache")
            if (!cacheDir.exists()) {
                cacheDir.mkdirs() // Create the directory if it doesn't exist
                Log.d("PdfView", "Cache directory created: ${cacheDir.path}")
            }

            // Extract the filename from the URL or use a default name
            val fileName = url?.substringAfterLast("/") ?: "temp.pdf"
            val cachedFile = File(cacheDir, fileName)

            // Check if the file is already in the cache
            if (cachedFile.exists()) {
                Log.d(
                    "PdfView",
                    "Loading PDF from cache: ${cachedFile.path}"
                ) // Log message for debugging
                return@withContext cachedFile // Return the cached file
            }

            Log.d("PdfView", "PDF not found in cache, attempting download from URL")

            // Open a connection to download the PDF from the URL
            val urlConnection: HttpURLConnection = (URL(url).openConnection() as HttpsURLConnection)

            // If the response code is 200 (OK), download the file
            if (urlConnection.responseCode == 200) {
                val inputStream = BufferedInputStream(urlConnection.inputStream) // Read the data
                val outputStream = FileOutputStream(cachedFile) // Write to cache file

                // Copy the input stream to the output stream
                inputStream.copyTo(outputStream)
                outputStream.close()
                inputStream.close()

                Log.d(
                    "PdfView",
                    "PDF cached successfully: ${cachedFile.path}"
                ) // Log message for debugging
                return@withContext cachedFile // Return the cached file
            } else {
                Log.e(
                    "PdfView",
                    "Failed to download PDF, Response code: ${urlConnection.responseCode}"
                )
            }
        } catch (e: Exception) {
            Log.e("PdfView", "Error retrieving PDF: ${e.message}", e) // Log error with stack trace
        }
        null // Return null if the file could not be downloaded
    }
}