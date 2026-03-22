package com.example.schoolmanagement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class PdfItem(
    val id: String = "",
    val title: String = "",
    val pdfUrl: String = "",
    val subject: String = "",
    val standard: String = "",
    val type: String = "",
    val isUploading: Boolean = true   // 🔥 NEW
)




@Composable
fun PdfList(
    list: List<PdfItem>
) {


    LazyColumn {
        items(
            items = list,
            key = { it.id + it.type}   // 🔥 VERY IMPORTANT
        ) { pdf ->
            PdfCard(pdf)
        }
    }
}

@Composable
fun PdfCard(pdf: PdfItem) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {

        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(Icons.Default.PictureAsPdf, contentDescription = null)

            Spacer(Modifier.width(10.dp))

            Column {

                Text(pdf.title, fontWeight = FontWeight.Bold)

                if (pdf.isUploading) {
                    Text(
                        text = "Uploading...",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
@Composable
fun TeacherMaterialScreen(
    selectedTab: Int,
    notes: List<PdfItem>,
    homework: List<PdfItem>
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        if (selectedTab == 0) {
            PdfList(notes)
        } else {
            PdfList(homework)
        }
    }
}