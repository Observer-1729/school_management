package com.example.schoolmanagement

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

data class FeeRecord(
    val studentId: String = "",
    val studentName: String = "",
    val standard: String = "",
    val quarter: String = "",
    val isPaid: Boolean = false,
    val reminderSent: Boolean = false
)

class FeeViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    var students = mutableStateListOf<StudentData>()
        private set

    var feeRecords = mutableStateListOf<FeeRecord>()
        private set

    var studentFeeList = mutableStateListOf<StudentFeeUI>()
        private set

    var selectedQuarter by mutableStateOf("")
        private set

    var selectedStandard by mutableStateOf("")
        private set

    fun setQuarter(q: String) {
        selectedQuarter = q
    }

    fun setStandard(s: String) {
        selectedStandard = s
    }
    var isLoading by mutableStateOf(false)
    var studentsLoaded by mutableStateOf(false)
    var feesLoaded by mutableStateOf(false)

    fun fetchStudentsByStandard(standard: String) {
        isLoading = true

        db.collection("users")
            .whereEqualTo("role", "student")
            .whereEqualTo("standard", standard)
            .get()
            .addOnSuccessListener { result ->

                val list = mutableListOf<StudentData>()

                for (doc in result) {
                    list.add(
                        StudentData(
                            userId = doc.getString("userId") ?: "",
                            name = doc.getString("name") ?: "",
                            role = doc.getString("role") ?: "",
                            standard = doc.getString("standard") ?: ""
                        )
                    )
                }

                students.clear()
                students.addAll(list)

                studentsLoaded = true

                if (feesLoaded) {
                    buildStudentFeeList(standard, selectedQuarter)
                }

                println("🔥 STUDENTS: $students")
            }
    }
    fun fetchFeeRecords(standard: String, quarter: String) {
        isLoading = true

        db.collection("fees")
            .whereEqualTo("standard", standard)
            .whereEqualTo("quarter", quarter)
            .get()
            .addOnSuccessListener { result ->

                val list = mutableListOf<FeeRecord>()

                for (doc in result) {
                    list.add(
                        FeeRecord(
                            studentId = doc.getString("studentId") ?: "",
                            studentName = doc.getString("studentName") ?: "",
                            standard = doc.getString("standard") ?: "",
                            quarter = doc.getString("quarter") ?: "",
                            isPaid = doc.getBoolean("isPaid") ?: false,
                            reminderSent = doc.getBoolean("reminderSent") ?: false
                        )
                    )
                }

                feeRecords.clear()
                feeRecords.addAll(list)

                feesLoaded = true

                if (studentsLoaded) {
                    buildStudentFeeList(standard, quarter)
                }

                println("🔥 FEES: $feeRecords")

                // ✅ IMPORTANT
                buildStudentFeeList(standard, quarter)
            }
    }
    fun buildStudentFeeList(standard: String, quarter: String) {

        val tempList = mutableListOf<StudentFeeUI>()

        for (student in students) {

            val fee = feeRecords.find { it.studentId == student.userId }

            if (fee != null) {
                // ✅ Existing record
                tempList.add(
                    StudentFeeUI(
                        studentId = student.userId,
                        name = student.name,
                        standard = student.standard,
                        isPaid = fee.isPaid,
                        reminderSent = fee.reminderSent
                    )
                )
            } else {
                // ❗ No record → default
                tempList.add(
                    StudentFeeUI(
                        studentId = student.userId,
                        name = student.name,
                        standard = student.standard,
                        isPaid = false,
                        reminderSent = false
                    )
                )
            }
            isLoading = false
        }

        studentFeeList.clear()
        studentFeeList.addAll(tempList)

        println("🔥 FINAL LIST: $studentFeeList")
    }

    fun updateFeeStatus(student: StudentFeeUI) {

        val newStatus = !student.isPaid

        db.collection("fees")
            .whereEqualTo("studentId", student.studentId)
            .whereEqualTo("quarter", selectedQuarter)
            .whereEqualTo("standard", student.standard)
            .get()
            .addOnSuccessListener { result ->

                if (!result.isEmpty) {
                    val docId = result.documents[0].id

                    db.collection("fees").document(docId)
                        .update(
                            mapOf(
                                "isPaid" to newStatus,
                                "reminderSent" to false
                            )
                        )
                } else {
                    db.collection("fees").add(
                        mapOf(
                            "studentId" to student.studentId,
                            "studentName" to student.name,
                            "standard" to student.standard,
                            "quarter" to selectedQuarter,
                            "isPaid" to newStatus,
                            "reminderSent" to false
                        )
                    )
                }

                val index = studentFeeList.indexOfFirst { it.studentId == student.studentId }

                if (index != -1) {
                    studentFeeList[index] = student.copy(
                        isPaid = newStatus,
                        reminderSent = false
                    )
                }
            }
    }
    fun updateReminderStatus(student: StudentFeeUI) {

        val newStatus = !student.reminderSent

        db.collection("fees")
            .whereEqualTo("studentId", student.studentId)
            .whereEqualTo("quarter", selectedQuarter)
            .whereEqualTo("standard", student.standard)
            .get()
            .addOnSuccessListener { result ->

                if (!result.isEmpty) {

                    val docId = result.documents[0].id

                    db.collection("fees").document(docId)
                        .update("reminderSent", newStatus)

                } else {
                    // 🔥 CREATE NEW RECORD
                    db.collection("fees").add(
                        mapOf(
                            "studentId" to student.studentId,
                            "studentName" to student.name,
                            "standard" to student.standard,
                            "quarter" to selectedQuarter,
                            "isPaid" to false,
                            "reminderSent" to newStatus
                        )
                    )
                }

                val index = studentFeeList.indexOfFirst { it.studentId == student.studentId }

                if (index != -1) {
                    studentFeeList[index] = student.copy(
                        reminderSent = newStatus
                    )
                }
            }
    }
}

data class StudentFeeUI(
    val studentId: String,
    val name: String,
    val standard: String,
    var isPaid: Boolean,
    var reminderSent: Boolean
)
