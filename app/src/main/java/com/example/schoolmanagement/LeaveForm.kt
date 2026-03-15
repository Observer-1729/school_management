package com.example.schoolmanagement

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplyLeaveForm() {

    val leaveBalance = 8

    var fromDate by remember { mutableStateOf<Long?>(null) }
    var toDate by remember { mutableStateOf<Long?>(null) }

    var showFromPicker by remember { mutableStateOf(false) }
    var showToPicker by remember { mutableStateOf(false) }

    var reason by remember { mutableStateOf("") }

    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text("Employee ID", fontWeight = FontWeight.Medium)
        Text("TCH1023")

        Spacer(modifier = Modifier.height(10.dp))

        Text("Employee Name", fontWeight = FontWeight.Medium)
        Text("Mr. Sharma")

        Spacer(modifier = Modifier.height(10.dp))

        Text("Leave Balance", fontWeight = FontWeight.Medium)
        Text("$leaveBalance Days")

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Leave Details",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
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
                    interactionSource = interactionSource
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
                    interactionSource = toInteractionSource
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
                    onValueChange = { reason = it },
                    label = { Text("Reason for Leave") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {

                        // Submit leave request

                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Submit")
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