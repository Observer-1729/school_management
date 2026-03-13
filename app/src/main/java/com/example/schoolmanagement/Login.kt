package com.example.schoolmanagement

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph
import androidx.navigation.compose.rememberNavController

@Composable

fun LoginScreen(onNavigateToWelcome: () -> Unit){
    val navController = rememberNavController()
//    NavGraph(navController = navController)
    var id by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
    ){
        Column(modifier =Modifier.fillMaxSize()
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center){

            Text(
                "Login Page",
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
                fontSize = 24.sp,  // Increase the font size
                fontWeight = FontWeight.Bold,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Cursive,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "Employee ID/Student ID",
                modifier = Modifier.padding(start = 20.dp, top = 20.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
            OutlinedTextField(
                value = id,
                onValueChange = { id = it },
                label = { Text("ID") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(

                    focusedBorderColor =MaterialTheme.colorScheme.onBackground,  // Border when focused
                    unfocusedBorderColor = Color.Gray,  // Border when not focused
                    cursorColor = MaterialTheme.colorScheme.onBackground,  // Cursor color
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    errorPlaceholderColor = Color.Red,
                    ),
                singleLine = true
            )
            Text(
                "Password",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                visualTransformation = PasswordVisualTransformation(),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.onBackground,  // Border when focused
                    unfocusedBorderColor = Color.Gray,  // Border when not focused
                    cursorColor = MaterialTheme.colorScheme.onBackground,  // Cursor color
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    errorPlaceholderColor = Color.Red,
                ),
                singleLine = true
            )
            Button(
                onClick = { onNavigateToWelcome() },
                modifier = Modifier
                    .wrapContentSize()
                    .padding(16.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFB17BF4),
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text("Login")
            }
        }

    }




}
