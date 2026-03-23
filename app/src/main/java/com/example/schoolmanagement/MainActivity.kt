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
                initCloudinary(this)

                val dummyResults = listOf(
                    "PA1" to 72.5f,
                    "PA2" to 81.0f,
                    "PA3" to 76.3f
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
//                ResultHistoryScreen(Modifier, dummyResults)







            }
        }


    }
}
