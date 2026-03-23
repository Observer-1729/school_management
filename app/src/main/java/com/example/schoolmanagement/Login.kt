

package com.example.schoolmanagement

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun LoginScreen(
    teacherViewModel: TeacherViewModel,
    studentViewModel: StudentViewModel,
    onNavigate: () -> Unit
) {

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    var id by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Welcome Back",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A3AFF)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Login to continue",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 🔹 CARD STARTS HERE
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "ID",
                        fontWeight = FontWeight.Medium
                    )

                    OutlinedTextField(
                        value = id,
                        onValueChange = { id = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Password",
                        fontWeight = FontWeight.Medium
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (id.isEmpty() || password.isEmpty()) {
                                Toast.makeText(context, "Enter all fields", Toast.LENGTH_SHORT)
                                    .show()
                                return@Button
                            }

                            val email = id.lowercase() + "@school.com"

                            db.collection("users")
                                .document(id) // ✅ DIRECT lookup (FAST + CORRECT)
                                .get()
                                .addOnSuccessListener { document ->

                                    if (document.exists()) {

                                        val name = document.getString("name") ?: "User"
                                        val role = document.getString("role")
                                        val standard = document.getString("standard") ?: ""


//                                onNavigate(name, role, teacherRoles, standard)

                                        if (role == null) {
                                            Toast.makeText(
                                                context,
                                                "Role missing",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            return@addOnSuccessListener
                                        }

                                        val teacherRoles =
                                            document.get("teacherRoles") as? List<String>
                                                ?: emptyList()

                                        auth.signInWithEmailAndPassword(email, password)
                                            .addOnSuccessListener {

                                                Toast.makeText(
                                                    context,
                                                    "Login Successful",
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                                // ✅ SAME NAVIGATION LOGIC (UNCHANGED)
                                                if (role == "teacher") {
                                                    teacherViewModel.loadTeacher(id) {
                                                        println("✅ Teacher stored in ViewModel")
                                                        onNavigate()
                                                    }
                                                }

                                                if (role == "student") {
                                                    studentViewModel.loadStudent(id) {
                                                        println("✅ Student stored in ViewModel")
                                                        onNavigate()
                                                    }
                                                }
                                            }
                                            .addOnFailureListener {
                                                Toast.makeText(
                                                    context,
                                                    "Wrong credentials",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                    } else {
                                        Toast.makeText(
                                            context,
                                            "User not found",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                                }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6C63FF)
                        )
                    ) {
                        Text("Login")
                    }
                }
            }
        }
    }
}