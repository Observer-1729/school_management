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


              AppNavigation(navController = navController)








            }
        }


    }
}
