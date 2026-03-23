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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.schoolmanagement.ui.theme.ResultStudent


@Composable
fun MarksEntryScreen(
    modifier: Modifier,
    exam: String,
    students: List<ResultStudent>,
    marks: MutableMap<Int, String>   // 🔥 ADD THIS
) {



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
    student: ResultStudent,
    marks: MutableMap<Int, String>
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),   // 🔥 smoother
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F3FF) // 🔥 light purple translucent vibe
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
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
                    fontWeight = FontWeight.SemiBold
                )
            }

            // 🔥 NEW INPUT STYLE
            OutlinedTextField(
                value = marks[student.rollNo] ?: "",
                onValueChange = { value ->

                    if (value.matches(Regex("^\\d*\\.?\\d*\$"))) {

                        val num = value.toFloatOrNull()

                        if (num == null || (num in 0f..100f)) {
                            marks[student.rollNo] = value
                        }
                    }
                },
                modifier = Modifier.width(90.dp),
                singleLine = true,
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                placeholder = { Text("%") },
                shape = RoundedCornerShape(12.dp),

                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF7B61FF),
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = Color(0xFF7B61FF),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
        }
    }
}