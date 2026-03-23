package com.example.schoolmanagement

import android.widget.Toast
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplyLeaveForm(
    teacherViewModel: TeacherViewModel,
    modifier: Modifier
) {
    val teacher = teacherViewModel.teacherData

    val leaveBalance = teacher.totalLeaves.toInt()
    val teacherId = teacher.userId
    val teacherName = teacher.name


    var fromDate by remember { mutableStateOf<Long?>(null) }
    var toDate by remember { mutableStateOf<Long?>(null) }

    var showFromPicker by remember { mutableStateOf(false) }
    var showToPicker by remember { mutableStateOf(false) }

    var reason by remember { mutableStateOf("") }

    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    Column(
        modifier = modifier
    ) {

        Text("Employee ID",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground)
        Text(teacherId,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground)

        Spacer(modifier = Modifier.height(10.dp))

        Text("Employee Name", fontWeight = FontWeight.Medium)
        Text(teacherName)

        Spacer(modifier = Modifier.height(10.dp))

        Text("Leave Balance", fontWeight = FontWeight.Medium)
        Text("$leaveBalance Days",
                style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground)

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Leave Details",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                // FROM DATE FIELD
                val interactionSource = remember { MutableInteractionSource() }

                OutlinedTextField(
                    value = fromDate?.let { formatter.format(Date(it)) } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("From Date") },
                    modifier = Modifier.fillMaxWidth(),
                    interactionSource = interactionSource,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    )
                )

                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            showFromPicker = true
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // TO DATE FIELD
                val toInteractionSource = remember { MutableInteractionSource() }

                OutlinedTextField(
                    value = toDate?.let { formatter.format(Date(it)) } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("To Date") },
                    modifier = Modifier.fillMaxWidth(),
                    interactionSource = toInteractionSource,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    )
                )

                LaunchedEffect(toInteractionSource) {
                    toInteractionSource.interactions.collect {
                        if (it is PressInteraction.Release && fromDate != null) {
                            showToPicker = true
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // REASON FIELD
                OutlinedTextField(
                    value = reason,
                    onValueChange = {
                        if (it.length <= 50) {
                            reason = it
                        }
                    },
                    label = { Text("Reason for Leave") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 2,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                val context = LocalContext.current

                Button(
                    onClick = {

                        if (fromDate == null || toDate == null || reason.isEmpty()) {
                            Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        val days = ((toDate!! - fromDate!!) / (1000 * 60 * 60 * 24)).toInt() + 1

                        val leaveData = mapOf(
                            "teacherId" to teacherId,
                            "teacherName" to teacherName,
                            "fromDate" to fromDate,
                            "toDate" to toDate,
                            "reason" to reason,
                            "status" to "pending",
                            "days" to days,
                            "standard" to teacher.classTeacherOf,
                            "role" to teacher.role,
                            "timestamp" to System.currentTimeMillis()
                        )

                        val db = FirebaseFirestore.getInstance()

                        db.collection("leaveRequests")
                            .document()
                            .set(leaveData)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Leave Applied", Toast.LENGTH_SHORT).show()
                                println("✅ Leave submitted successfully")
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Error submitting leave", Toast.LENGTH_SHORT).show()
                                println("❌ Error submitting leave")
                            }

                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text  = "Submit")
                }

            }

        }

    }

    // FROM DATE PICKER
    if (showFromPicker) {

        val datePickerState = rememberDatePickerState(
            selectableDates = object : SelectableDates {

                override fun isSelectableDate(utcTimeMillis: Long): Boolean {

                    val tomorrow =
                        System.currentTimeMillis() + 86400000

                    return utcTimeMillis >= tomorrow
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = { showFromPicker = false },
            confirmButton = {

                TextButton(
                    onClick = {

                        fromDate = datePickerState.selectedDateMillis
                        showFromPicker = false

                    }
                ) { Text("OK") }

            }
        ) {

            DatePicker(state = datePickerState)

        }
    }

    // TO DATE PICKER
    if (showToPicker && fromDate != null) {

        val maxLeaveMillis =
            fromDate!! + (leaveBalance * 86400000L)

        val datePickerState = rememberDatePickerState(
            selectableDates = object : SelectableDates {

                override fun isSelectableDate(utcTimeMillis: Long): Boolean {

                    return utcTimeMillis >= fromDate!! &&
                            utcTimeMillis <= maxLeaveMillis

                }
            }
        )

        DatePickerDialog(
            onDismissRequest = { showToPicker = false },
            confirmButton = {

                TextButton(
                    onClick = {

                        toDate = datePickerState.selectedDateMillis
                        showToPicker = false

                    }
                ) { Text("OK") }

            }
        ) {

            DatePicker(state = datePickerState)

        }
    }

}