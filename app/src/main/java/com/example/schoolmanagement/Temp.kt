package com.example.schoolmanagement

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun FixStudentNamesScreen() {

    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {

        Button(onClick = {
            syncStudentNames {
                Toast.makeText(context, "Student names synced!", Toast.LENGTH_LONG).show()
            }
        }) {
            Text("Fix Student Names")
        }
    }
}



fun syncStudentNames(onComplete: () -> Unit) {

    val db = FirebaseFirestore.getInstance()
    val tasks = mutableListOf<com.google.android.gms.tasks.Task<Void>>()

    db.collection("students").get().addOnSuccessListener { result ->

        result.forEach { doc ->

            val studentId = doc.getString("studentId") ?: return@forEach
            val name = doc.getString("name") ?: "Student"

            val task = db.collection("users")
                .document(studentId)
                .update("name", name) // 🔥 ONLY updating name

            tasks.add(task)
        }

        com.google.android.gms.tasks.Tasks.whenAllComplete(tasks)
            .addOnSuccessListener {
                onComplete()
            }
    }
}