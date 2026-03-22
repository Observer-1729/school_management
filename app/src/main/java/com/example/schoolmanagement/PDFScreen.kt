package com.example.schoolmanagement


import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.barteksc.pdfviewer.PDFView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

@Composable
fun PdfScreen(
    url: String   // 🔥 CHANGE: now we pass URL
) {

    var fileUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    var currentPage by remember { mutableStateOf(0) }
    var totalPages by remember { mutableStateOf(0) }

    val context = LocalContext.current


    // 🔥 DOWNLOAD FILE
    LaunchedEffect(url) {

        println("🔥 PDF URL: $url")

        try {

            val file = withContext(Dispatchers.IO) {

                val file = File.createTempFile("temp_pdf", ".pdf", context.cacheDir)

                URL(url).openStream().use { input ->
                    FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                }

                file
            }

            println("✅ DOWNLOAD SUCCESS")

            fileUri = Uri.fromFile(file)
            isLoading = false

        } catch (e: Exception) {
            println("❌ DOWNLOAD ERROR: ${e.message}")
            e.printStackTrace()
            isLoading = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        if (isLoading) {
            Text(
                text = "Loading PDF...",
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (fileUri != null) {

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    PDFView(context, null).apply {

                        fromUri(fileUri)
                            .enableSwipe(true)
                            .swipeHorizontal(false)
                            .enableDoubletap(true)
                            .spacing(10)
                            .autoSpacing(true)
                            .pageFling(true)
                            .pageSnap(true)
                            .onPageChange { page, pageCount ->
                                currentPage = page + 1
                                totalPages = pageCount
                            }
                            .load()
                    }
                }
            )
        } else {
            Text(
                text = "Failed to load PDF",
                modifier = Modifier.align(Alignment.Center),
                color = Color.Red
            )
        }

        // Page indicator
        if (!isLoading && totalPages > 0) {
            Text(
                text = "$currentPage / $totalPages",
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }
    }
}