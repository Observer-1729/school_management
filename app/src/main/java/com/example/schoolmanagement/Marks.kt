package com.example.schoolmanagement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun MarksEntryScreen(
    modifier: Modifier,
    subject: String,
    exam: String,
    students: List<Student>
) {

    val marks = remember {
        mutableStateMapOf<Int, String>()
    }

    Column(
        modifier = modifier

    ) {


        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {

            items(students) { student ->

                StudentMarksCard(
                    student = student,
                    marks = marks
                )

            }

        }

        Spacer(modifier = Modifier.height(16.dp))


    }
}

@Composable
fun StudentMarksCard(
    student: Student,
    marks: MutableMap<Int, String>
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
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Roll No: ${student.rollNo}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = student.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

            }

            OutlinedTextField(
                value = marks[student.rollNo] ?: "",
                onValueChange = { value ->

                    if (value.all { it.isDigit() }) {

                        val num = value.toIntOrNull()

                        if (num == null || num <= 100) {
                            marks[student.rollNo] = value
                        }

                    }

                },
                modifier = Modifier.width(90.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                label = { Text("Marks") }
            )

        }

    }
}
