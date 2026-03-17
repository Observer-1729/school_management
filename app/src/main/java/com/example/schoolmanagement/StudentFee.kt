package com.example.schoolmanagement

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class StudentFee(
    val rollNo: Int,
    val name: String,
    var isPaid: MutableState<Boolean> = mutableStateOf(false),
    var reminderSent: MutableState<Boolean> = mutableStateOf(false)
)