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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.schoolmanagement.TeacherViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun AttendanceScreen(
    modifier: Modifier,
    teacherViewModel: TeacherViewModel,
    students: MutableList<Student>   // 🔥 PASS STATE FROM OUTSIDE
) {

    val db = FirebaseFirestore.getInstance()

    val standard = teacherViewModel.teacherData.classTeacherOf ?: ""

    LaunchedEffect(Unit) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
        Date()
    )

        val calendar = Calendar.getInstance()
        val isSunday = calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY

        val attendanceRef = db.collection("attendance")
            .document(standard)
            .collection(today)
            .document("data")
        val updatedDoc = attendanceRef.get().await()
        val attendanceMapFinal = updatedDoc.data ?: emptyMap<String, Any>()

        if (!updatedDoc.exists() || isSunday) {

            val studentResult = db.collection("students")
                .whereEqualTo("standard", standard)
                .get()
                .await()

            val autoMap = studentResult.documents.associate { studentDoc ->
                val roll = (studentDoc.getLong("rollNumber") ?: 0).toInt()
                roll.toString() to "present"
            }

            attendanceRef.set(autoMap)
        }

        val result = db.collection("students")
            .whereEqualTo("standard", standard)
            .get()
            .await()

                val list = result.documents.map { doc ->
                    val roll = (doc.getLong("rollNumber") ?: 0).toInt()
                    val status = attendanceMapFinal[roll.toString()] ?: "present"

                    Student(
                        rollNo = roll,
                        name = doc.getString("name") ?: "",
                        isPresent = status == "present"
                    )
                }

                students.clear()
                students.addAll(list)
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