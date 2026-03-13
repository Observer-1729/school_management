package com.example.schoolmanagement

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavigation(navController: NavHostController, pd : PaddingValues) {
//    val userInputViewModel: UserInputViewModel = viewModel()
//    val currentRoute = remember { mutableStateOf("") }
    NavHost(navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }

        composable("login") {
            LoginScreen(
                onNavigateToWelcome = { navController.navigate("welcome") }
            )
        }
        composable("welcome") {
            WelcomePage()
        }
    }
}
