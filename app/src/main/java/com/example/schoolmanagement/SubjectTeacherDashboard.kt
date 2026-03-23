package com.example.schoolmanagement

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEDEBFF) // soft purple
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🔹 Icon container (important!)
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        Color.White.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = Color(0xFF4A3AFF)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = subject,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D2D2D)
                )

                Text(
                    text = className,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // 🔹 Optional arrow (feels clickable)
            Text(
                text = "→",
                fontSize = 18.sp,
                color = Color.Gray
            )
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

                // 🔹 Dark strip
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF4A3AFF))
                        .statusBarsPadding()
                )

                // 🔹 Main header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF6C63FF))
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Text(
                        text = teacherName,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                tabs?.let {
                    Surface(color = Color(0xFF6C63FF)) {
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
    teacherViewModel: TeacherViewModel,
    onLeaveClick: () -> Unit,
    onSubjectClick: (SubjectClass) -> Unit
){
    val leavesLeft = teacherViewModel.teacherData.totalLeaves.toInt()

    Column(
        modifier = modifier
    ) {

        LeaveCard(leavesLeft) {
            onLeaveClick()
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Your Classes",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2D2D2D)
        )

        LazyColumn {

            teacherViewModel.subjectAssignments.forEach { assignment ->

                assignment.standards.forEach { std ->

                    item {

                        SubjectClassCard(
                            subject = assignment.subject,
                            className = "Class $std",
                            onClick = {
                                onSubjectClick(
                                    SubjectClass(
                                        assignment.subject,
                                        "Class $std"
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }

    }
}