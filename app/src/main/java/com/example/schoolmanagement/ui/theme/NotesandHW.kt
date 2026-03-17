package com.example.schoolmanagement.ui.theme

import android.net.Uri
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

@Composable
fun SubjectScreen(
    modifier: Modifier,
    selectedTab: Int,
    onPdfOpen: (String, Uri) -> Unit   // 🔥 IMPORTANT
) {

    Column(modifier = modifier) {

        if (selectedTab == 0) {
            NotesSection(onPdfOpen)
        } else {
            HomeworkSection(onPdfOpen)
        }
    }
}

@Composable
fun NotesSection(
    onPdfOpen: (String, Uri) -> Unit,
    modifier: Modifier = Modifier
) {

    val placeholderNotes = List(6) { "Notes PDF ${it + 1}" }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(placeholderNotes) { title ->

            PdfCard(
                title = title,
                date = "DD MMM YYYY",
                onClick = {
                    val uri = Uri.parse(
                        "android.resource://com.example.schoolmanagement/raw/sample"
                    )
                    onPdfOpen(title, uri)   // 🔥 SEND EVENT UP
                }
            )
        }
    }
}

@Composable
fun HomeworkSection(
    onPdfOpen: (String, Uri) -> Unit,
    modifier: Modifier = Modifier
) {

    val homework = List(6) { "Homework ${it + 1}" }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(homework) { title ->

            PdfCard(
                title = title,
                date = "DD MMM YYYY",
                onClick = {
                    val uri = Uri.parse(
                        "android.resource://com.example.schoolmanagement/raw/sample"
                    )
                    onPdfOpen(title, uri)   // 🔥 SEND EVENT UP
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