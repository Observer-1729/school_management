package com.example.schoolmanagement.ui.theme

import android.R.attr.padding
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.schoolmanagement.PdfScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectScreen(
    subjectName: String,
    navController: NavController
) {

    var selectedTab by remember { mutableStateOf(0) }

    val tabs = listOf("Notes", "Homework")

    Scaffold(

        topBar = {

            Column {

                TopAppBar(
                    title = {
                        Text(
                            text = subjectName,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { navController.popBackStack() }
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFB17BF4)
                    )
                )

                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color(0xFFB17BF4),
                    contentColor = Color.White
                ) {

                    tabs.forEachIndexed { index, title ->

                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) }
                        )
                    }
                }
            }
        }

    ) { padding ->

        if (selectedTab == 0) {
            NotesSection(Modifier.padding(padding))
        } else {
            HomeworkSection(Modifier.padding(padding))
        }

    }
}

@Composable
fun NotesSection(
    modifier: Modifier = Modifier
) {

    val placeholderNotes = List(6) { "Notes PDF ${it + 1}" }

    var selectedPdf by remember { mutableStateOf<Uri?>(null) }

    if (selectedPdf != null) {

        PdfScreen(
            uri = selectedPdf!!,
            onBack = { selectedPdf = null }
        )

    } else {

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

                        // Placeholder URI (replace later with Firebase PDF)
                        selectedPdf = Uri.parse(
                            "android.resource://com.example.schoolmanagement/raw/sample"
                        )

                    }
                )

            }

        }

    }
}

@Composable
fun HomeworkSection(
    modifier: Modifier = Modifier
) {

    val homework = List(6) { "Homework ${it + 1}" }

    var selectedPdf by remember { mutableStateOf<Uri?>(null) }

    if (selectedPdf != null) {

        PdfScreen(
            uri = selectedPdf!!,
            onBack = { selectedPdf = null }
        )

    } else {

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

                        // Placeholder PDF Uri
                        selectedPdf = Uri.parse(
                            "android.resource://com.example.schoolmanagement/raw/sample"
                        )

                    }
                )

            }

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