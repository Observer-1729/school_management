package com.example.schoolmanagement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
    val title: String,
    val fileName: String
)

val notesList = listOf(
    PdfItem("Algebra Notes", "algebra.pdf"),
    PdfItem("Trigonometry Notes", "trigonometry.pdf"),
    PdfItem("Geometry Notes", "geometry.pdf")
)

val homeworkList = listOf(
    PdfItem("Homework 1", "hw1.pdf"),
    PdfItem("Homework 2", "hw2.pdf")
)

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TeacherUploadScreen(
//    teacherName: String,
//    onBackClick: () -> Unit
//) {
//
//    var selectedTab by remember { mutableStateOf(0) }
//
//    val pdfPicker = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri ->
//
//        if (uri != null) {
//
//            println("Selected PDF: $uri")
//
//            // Later we upload this URI to Firebase
//
//        }
//
//    }
//
//    Scaffold(
//
//        topBar = {
//
//            TopAppBar(
//
//                title = { Text(teacherName) },
//
//                navigationIcon = {
//
//                    IconButton(onClick = onBackClick) {
//
//                        Icon(
//                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                            contentDescription = "Back"
//                        )
//
//                    }
//
//                },
//
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = Color(0xFF1976D2),
//                    titleContentColor = Color.White,
//                    navigationIconContentColor = Color.White
//                )
//
//            )
//
//        },
//
//        floatingActionButton = {
//
//            ExtendedFloatingActionButton(
//
//                onClick = {
//                    pdfPicker.launch("application/pdf")
//                },
//
//                containerColor = Color(0xFF1976D2)
//
//            ) {
//
//                Icon(
//                    imageVector = Icons.Default.Add,
//                    contentDescription = null,
//                    tint = Color.White
//                )
//
//                Spacer(Modifier.width(6.dp))
//
//                Text(
//                    if (selectedTab == 0) "Upload Note" else "Upload Homework",
//                    color = Color.White
//                )
//
//            }
//
//        }
//
//    ) { padding ->
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//        ) {
//
//            TabRow(selectedTabIndex = selectedTab) {
//
//                Tab(
//                    selected = selectedTab == 0,
//                    onClick = { selectedTab = 0 },
//                    text = { Text("Notes") }
//                )
//
//                Tab(
//                    selected = selectedTab == 1,
//                    onClick = { selectedTab = 1 },
//                    text = { Text("Homework") }
//                )
//
//            }
//
//            when (selectedTab) {
//
//                0 -> NotesContent()
//
//                1 -> HomeworkContent()
//
//            }
//
//        }
//
//    }
//
//}

@Composable
fun NotesContent() {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        items(notesList) { pdf ->

            PdfCard(
                title = pdf.title,
                fileName = pdf.fileName
            )

        }

    }

}

@Composable
fun HomeworkContent() {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        items(homeworkList) { pdf ->

            PdfCard(
                title = pdf.title,
                fileName = pdf.fileName
            )

        }

    }

}

@Composable
fun PdfCard(
    title: String,
    fileName: String
) {

    Card(

        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),

        shape = RoundedCornerShape(14.dp),

        elevation = CardDefaults.cardElevation(4.dp)

    ) {

        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),

            verticalAlignment = Alignment.CenterVertically

        ) {

            Icon(

                imageVector = Icons.Default.PictureAsPdf,

                contentDescription = null,

                tint = Color.Red,

                modifier = Modifier.size(32.dp)

            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {

                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )

                Text(
                    text = fileName,
                    fontSize = 13.sp,
                    color = Color.Gray
                )

            }

        }

    }

}