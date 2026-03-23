package com.example.schoolmanagement.ui.theme

data class Student(
    val rollNo: Int,
    val name: String,
    val isPresent: Boolean = true
)

data class ResultStudent(
    val studentId: String,
    val rollNo: Int,
    val name: String
)