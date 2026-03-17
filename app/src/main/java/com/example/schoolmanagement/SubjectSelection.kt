package com.example.schoolmanagement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SubjectSelectionScreen(
    modifier : Modifier,
    examName: String,
    subjects: List<String>,
    onSubjectClick: (String) -> Unit
) {

    Column(
        modifier = modifier

    ) {


        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(subjects) { subject ->

                SubjectCard(
                    subjectName = subject,
                    onClick = { onSubjectClick(subject) }
                )

            }

        }
    }
}