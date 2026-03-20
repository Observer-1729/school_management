package com.example.schoolmanagement

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StudentFeePage(
    padding: PaddingValues,
    feeViewModel: FeeViewModel
) {
    val students = feeViewModel.studentFeeList
    val context = LocalContext.current

    LaunchedEffect(feeViewModel.selectedStandard, feeViewModel.selectedQuarter) {

        feeViewModel.studentsLoaded = false
        feeViewModel.feesLoaded = false

        val standard = feeViewModel.selectedStandard
        val quarter = feeViewModel.selectedQuarter

        feeViewModel.fetchStudentsByStandard(standard)
        feeViewModel.fetchFeeRecords(standard, quarter)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        if (feeViewModel.isLoading) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(students) { student ->
                    StudentFeeRow(
                        student = student,
                        onPaidToggle = {
                            val newStatus = !student.isPaid

                            feeViewModel.updateFeeStatus(student)

                            Toast.makeText(
                                context,
                                if (newStatus) "Marked Paid" else "Marked Pending",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onReminderToggle = {
                            feeViewModel.updateReminderStatus(student)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun StudentFeeRow(
    student: StudentFeeUI,
    onPaidToggle: () -> Unit,
    onReminderToggle: () -> Unit
) {

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

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = student.studentId,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )

                Text(
                    text = student.name,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
            }

            Button(
                onClick = onPaidToggle,
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (student.isPaid) Color(0xFF4CAF50)
                        else Color(0xFFE53935)
                )
            ) {
                Text(
                    if (student.isPaid) "Paid" else "Pending",
                    color = Color.White
                )
            }

            if (!student.isPaid) {

                IconButton(
                    onClick = onReminderToggle
                ) {

                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Reminder",
                        tint =
                            if (student.reminderSent)
                                Color(0xFFFFC107)   // yellow
                            else
                                Color.DarkGray
                    )
                }

            }
        }
    }
}