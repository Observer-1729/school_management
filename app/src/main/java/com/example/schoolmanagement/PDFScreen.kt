package com.example.schoolmanagement


import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.barteksc.pdfviewer.PDFView

@Composable
fun PdfScreen(
    uri: Uri,
    onBack: () -> Unit
) {

    var currentPage by remember { mutableStateOf(0) }
    var totalPages by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                PDFView(context, null).apply {

                    setBackgroundColor(0xFCDBFF
                    )

                    fromUri(uri)
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

        //Close Button
        Icon(imageVector = Icons.Default.Close,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .clickable { onBack() }
                .size(22.dp))

        // Page number (Bottom Center)
        Text(
            text = "$currentPage / $totalPages",
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}
