package com.example.schoolmanagement

data class LeaveRequest(
    val id: String = "",
    val teacherName: String = "",
    val teacherId: String = "",
    val days: Int = 0,
    val reason: String = "",
    val status: String = "pending",
    val fromDate: Long = 0,
    val toDate: Long = 0
)