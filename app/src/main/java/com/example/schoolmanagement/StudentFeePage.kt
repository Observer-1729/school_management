package com.example.schoolmanagement

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun StudentFeePage(
    padding: PaddingValues,
    onChange: () -> Unit
) {

    val students = remember {
        mutableStateListOf(
            StudentFee(1, "Rohan Patel"),
            StudentFee(2, "Aman Shah"),
            StudentFee(3, "Neha Sharma"),
            StudentFee(4, "Priya Mehta"),
            StudentFee(5, "Rahul Verma")
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier
                .weight(1f).
                padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            items(students) { student ->
                StudentFeeRow(student,
                    onChange = onChange)

            }
        }

        // TEMP SUBMIT BUTTON

    }
}

@Composable
fun StudentFeeRow(student: StudentFee,onChange: () -> Unit) {

    Card(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = student.rollNo.toString(),
                modifier = Modifier.width(35.dp),
                fontWeight = FontWeight.Bold
            )

            Text(
                text = student.name,
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = {
                    student.isPaid.value = !student.isPaid.value
                    if (student.isPaid.value) {
                        student.reminderSent.value = false
                    }
                    onChange()

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (student.isPaid.value) Color(0xFF4CAF50)
                        else Color(0xFFE53935)
                )
            ) {
                Text(
                    if (student.isPaid.value) "Paid" else "Pending",
                    color = Color.White
                )
            }

            if (!student.isPaid.value) {

                IconButton(
                    onClick = {
                        student.reminderSent.value = !student.reminderSent.value
                        onChange()
                    }
                ) {

                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Reminder",
                        tint =
                            if (student.reminderSent.value)
                                Color(0xFFFFC107)   // yellow
                            else
                                Color.DarkGray
                    )
                }

            }
        }
    }
}