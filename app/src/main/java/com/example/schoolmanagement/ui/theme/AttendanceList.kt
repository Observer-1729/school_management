package com.example.schoolmanagement.ui.theme

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AttendanceScreen(modifier: Modifier) {

    val students = remember {
        mutableStateListOf(
            Student(1, "Aarav Raval Chutiya asdfghjkhytresxcvhrd"),
            Student(2, "Riya ASDFGHJRESDHHRDNRRVBHRTBVBTRDBVBFGFVBV"),
            Student(3, "Kabir"),
            Student(4, "Ananya"),
            Student(5, "Vihaan")
        )
    }

    LazyColumn(modifier = modifier) {

        itemsIndexed(students) { index, student ->

            AttendanceRow(
                student = student,
                onToggle = {
                    students[index] =
                        student.copy(isPresent = !student.isPresent)
                }
            )

        }

    }
}


@Composable
fun AttendanceRow(
    student: Student,
    onToggle: () -> Unit
) {


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Roll Number
            Text(
                text = student.rollNo.toString(),
                modifier = Modifier.width(40.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Scrollable Name Area
            Box(
                modifier = Modifier
                    .width(180.dp)
                    .basicMarquee()
            ) {
                Text(student.name)
            }

            Spacer(modifier = Modifier.weight(1f))

            // Attendance Button
            Button(
                onClick = onToggle,
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (student.isPresent) Color(0xFF4CAF50)
                        else Color.Red
                )
            ) {
                Text(if (student.isPresent) "Present" else "Absent")
            }
        }
    }
}