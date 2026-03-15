package com.example.schoolmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.schoolmanagement.ui.theme.AttendanceScreen
import com.example.schoolmanagement.ui.theme.SchoolManagementTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SchoolManagementTheme {
                val navController = rememberNavController()
                val pd = PaddingValues()

                val subjects = listOf(
                    "Math",
                    "Science",
                    "English",
                    "Physics",
                    "Chemistry",
                    "Computer"
                )
//                LoginScreen()
//                WelcomePage()
//               SelectionTeacher()
//                StudentDashboard(
//                    studentName = "Sahil",
//                    attendancePercentage = 88,
//                    subjects = subjects,
//                    onSubjectClick = { subject ->
//                        // Navigation later
//                    }
//                )
                AttendanceScreen()
//               AppNavigation(navController = navController, pd =pd)

            }
        }


    }
}
