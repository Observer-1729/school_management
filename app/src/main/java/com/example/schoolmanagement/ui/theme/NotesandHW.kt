package com.example.schoolmanagement.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.schoolmanagement.PdfItem

@Composable
fun SubjectScreen(
    modifier: Modifier,
    selectedTab: Int,
    notes: List<PdfItem>,        // 🔥 ADD
    homework: List<PdfItem>,     // 🔥 ADD
    onPdfOpen: (String, String) -> Unit
) {


    Column(modifier = modifier) {

        if (selectedTab == 0) {
            NotesSection(notes, onPdfOpen)
        } else {
            HomeworkSection(homework, onPdfOpen)
        }
    }
}

@Composable
fun NotesSection(
    notes: List<PdfItem>,   // 🔥 REAL DATA
    onPdfOpen: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(notes) { pdf ->   // 🔥 REAL LIST

            PdfCard(
                title = pdf.title,
                date = "Notes",
                onClick = {
                    onPdfOpen(
                        pdf.title,
                        pdf.pdfUrl   // 🔥 CLOUDINARY URL
                    )
                }
            )
        }
    }
}

@Composable
fun HomeworkSection(
    homework: List<PdfItem>,
    onPdfOpen: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(homework) { pdf ->

            PdfCard(
                title = pdf.title,
                date = "Homework",
                onClick = {
                    onPdfOpen(
                        pdf.title,
                        pdf.pdfUrl
                    )
                }
            )
        }
    }
}

@Composable
fun PdfCard(
    title: String,
    date: String,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .padding(8.dp)
            .height(130.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Icon(
                imageVector = Icons.Default.PictureAsPdf,
                contentDescription = null,
                tint = Color.Red
            )

            Text(
                text = title,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = date,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}