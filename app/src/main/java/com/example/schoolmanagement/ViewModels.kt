package com.example.schoolmanagement

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore



data class TeacherData(

    // 🔹 Basic Info
    val userId: String = "",
    val name: String = "",
    val role: String = "",
    val teacherRoles: List<String> = emptyList(),

    // 🔹 Class Teacher
    val classTeacherOf: String? = null,

    // 🔹 Subject Teacher
    val subjectsTaught: List<String> = emptyList(),
    val standardsTaught: List<String> = emptyList(),

    // 🔹 Leaves
    val totalLeaves: Long = 0
)

class TeacherViewModel : ViewModel() {

    var teacherData by mutableStateOf(TeacherData())
        private set

    private val db = FirebaseFirestore.getInstance()

    var subjectAssignments = mutableStateListOf<SubjectAssignment>()
        private set

    fun loadTeacher(userId: String, onComplete: () -> Unit) {




        // 🔹 1. Load basic user data
        db.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { doc ->

                if (doc.exists()) {
                    println("🔥 loadTeacher called with ID: $userId")

                    teacherData = TeacherData(
                        userId = userId,
                        name = doc.getString("name") ?: "",
                        role = doc.getString("role") ?: "",
                        teacherRoles = doc.get("teacherRoles") as? List<String> ?: emptyList(),
                        totalLeaves = doc.getLong("totalLeaves") ?: 0
                    )

                    // 🔥 Load extra data AFTER base load
                    loadClassTeacher(userId)
                    loadSubjectAssignments(userId)
                    println("🔥 RAW totalLeaves: ${doc.get("totalLeaves")}")
                    println("🔥 getLong totalLeaves: ${doc.getLong("totalLeaves")}")

                    onComplete()
                }
            }
    }

    // 🔹 2. Find if teacher is class teacher
    private fun loadClassTeacher(userId: String) {

        db.collection("standards")
            .get()
            .addOnSuccessListener { result ->

                var assignedStandard: String? = null

                result.documents.forEach { doc ->
                    if (doc.getString("classTeacherId") == userId) {
                        assignedStandard = doc.id
                    }
                }

                teacherData = teacherData.copy(
                    classTeacherOf = assignedStandard
                )
            }
    }

    // 🔹 3. Load subject assignments
    private fun loadSubjectAssignments(userId: String) {

        db.collection("subjectAssignments")
            .whereEqualTo("teacherId", userId)
            .get()
            .addOnSuccessListener { result ->

                val list = mutableListOf<SubjectAssignment>()

                for (doc in result) {

                    val subject = doc.getString("subject") ?: ""
                    val standards = doc.get("standards") as? List<String> ?: emptyList()

                    list.add(
                        SubjectAssignment(subject, standards)
                    )
                }

                subjectAssignments.clear()
                subjectAssignments.addAll(list)

                println("🔥 SUBJECT ASSIGNMENTS: $subjectAssignments")
            }
    }


    // 🔥 Helper functions (optional but useful)

    fun isClassTeacher(): Boolean {
        return teacherData.classTeacherOf != null
    }

    fun isSubjectTeacher(): Boolean {
        return teacherData.subjectsTaught.isNotEmpty()
    }

    fun getLeaves(): Long {
        return teacherData.totalLeaves
    }
}

data class StudentData(

    // 🔹 Basic Info
    val userId: String = "",
    val name: String = "",
    val role: String = "",
    val standard: String = "",
    val rollNo: Int = 0

)

class StudentViewModel : ViewModel() {

    var studentData by mutableStateOf(StudentData())
        private set

    private val db = FirebaseFirestore.getInstance()

    fun loadStudent(userId: String, onComplete: () -> Unit) {

        db.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { userDoc ->

                if (userDoc.exists()) {

                    db.collection("students")
                        .document(userId)   // 🔥 THIS IS THE FIX
                        .get()
                        .addOnSuccessListener { studentDoc ->

                            studentData = StudentData(
                                userId = userId,
                                name = userDoc.getString("name") ?: "",
                                role = userDoc.getString("role") ?: "",
                                standard = userDoc.getString("standard") ?: "",
                                rollNo = studentDoc.getLong("rollNumber")?.toInt() ?: 0
                            )

                            println("🔥 Student RollNo: ${studentData.rollNo}")

                            onComplete()
                        }
                }
            }
    }
    // 🔥 Helper functions

    fun getName(): String {
        return studentData.name
    }

    fun getStandard(): String {
        return studentData.standard
    }

}

data class SubjectAssignment(
    val subject: String = "",
    val standards: List<String> = emptyList()
)

