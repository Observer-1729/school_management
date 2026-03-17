package com.example.schoolmanagement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class SubjectClass(
    val subject: String,
    val className: String
)

val teacherAssignments = listOf(
    SubjectClass("Maths", "Class 1"),
    SubjectClass("Maths", "Class 3"),
    SubjectClass("Maths", "Class 5"),
    SubjectClass("Maths", "Class 7"),
    SubjectClass("Maths", "Class 9"),
    SubjectClass("Science", "Class 2"),
    SubjectClass("Science", "Class 4")
)

@Composable
fun SubjectClassCard(
    subject: String,
    className: String,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.MenuBook,
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {

                Text(
                    text = subject,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = className,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectTeacherDashboard(
    teacherName: String,
    onBackClick: () -> Unit,
    tabs: (@Composable () -> Unit)? = null,
    floatingActionButton: (@Composable () -> Unit)? = null,
    content: @Composable (Modifier) -> Unit
){

    Scaffold(

        topBar = {

            Column {

                TopAppBar(

                    title = {
                        Text(text = teacherName)
                    },

                    navigationIcon = {
                        IconButton(onClick = onBackClick) {

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },

                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF1976D2),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )

                tabs?.let {

                    Surface(
                        color = Color(0xFF1976D2)
                    ) {
                        it()
                    }

                }

            }

        },
        floatingActionButton = {
            floatingActionButton?.invoke()
        }

    ) { paddingValues ->

        content(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        )

    }
}

@Composable
fun SubjectTeacherDashboardContent(
    modifier: Modifier,
    onLeaveClick: () -> Unit,
    onSubjectClick: (SubjectClass) -> Unit
){

    Column(
        modifier = modifier
    ) {

        LeaveCard(8) {
            onLeaveClick()
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Your Classes",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn {

            items(teacherAssignments) { item ->

                SubjectClassCard(
                    subject = item.subject,
                    className = item.className,
                    onClick = {

                        onSubjectClick(item)


                    }
                )

            }

        }

    }
}