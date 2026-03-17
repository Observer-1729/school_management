package com.example.schoolmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.compose.rememberNavController
import com.example.schoolmanagement.ui.theme.SchoolManagementTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SchoolManagementTheme {
                val navController = rememberNavController()
                val pd = PaddingValues()

                val students = listOf(
                    Student(1,"Rahul Patel"),
                    Student(2,"Amit Shah"),
                    Student(3,"Riya Patel"),
                    Student(4,"Neha Sharma")
                )

                val subjects = listOf(
                    "Maths",
                    "English",
                    "Hindi",
                    "Science",
                    "Physics",
                    "Chemistry",
                    "Biology",
                    "Computer",
                    "History",
                    "Geography",
                    "Economics",
                    "Civics",
                    "Moral Values",
                    "General Knowledge"
                )

                val teacherAssignments = listOf(
                    SubjectClass("Maths", "Class 1"),
                    SubjectClass("Maths", "Class 3"),
                    SubjectClass("Maths", "Class 5"),
                    SubjectClass("Maths", "Class 7"),
                    SubjectClass("Maths", "Class 9"),
                    SubjectClass("Science", "Class 2"),
                    SubjectClass("Science", "Class 4")
                )

//                LoginScreen()
//                WelcomePage()
////               SelectionTeacher()
//                StudentDashboard(
//                    studentName = "Sahil",
//                    attendancePercentage = 88,
//                    subjects = subjects,
//                    feeDue = true,
//                    onSubjectClick = { subject ->
//                        // Navigation later
//                    }
//                )
//                AttendanceScreen()
              AppNavigation(navController = navController)




            }
        }


    }
}
