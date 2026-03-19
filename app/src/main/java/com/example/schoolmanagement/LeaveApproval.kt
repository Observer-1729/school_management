package com.example.schoolmanagement

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaveRequestsContent(
    padding: PaddingValues
) {

    var leaveList by remember { mutableStateOf<List<LeaveRequest>>(emptyList()) }

    LaunchedEffect(Unit) {

        FirebaseFirestore.getInstance()
            .collection("leaveRequests")
            .get()
            .addOnSuccessListener { result ->

                val list = result.documents.map { doc ->
                    LeaveRequest(
                        id = doc.id,
                        teacherName = doc.getString("teacherName") ?: "",
                        teacherId = doc.getString("teacherId") ?: "",
                        days = (doc.getLong("days") ?: 0).toInt(),
                        reason = doc.getString("reason") ?: "",
                        status = doc.getString("status") ?: "pending",
                        fromDate = doc.getLong("fromDate") ?: 0,
                        toDate = doc.getLong("toDate") ?: 0
                    )
                }

                leaveList = list
            }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        items(leaveList) { leave ->

            LeaveRequestCard(
                leave = leave
            )
        }
    }
}

@Composable
fun LeaveRequestCard(
    leave: LeaveRequest
) {

    var status by remember { mutableStateOf(leave.status) }

    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    val cardColor = when (status) {
        "approved" -> Color(0xFFA5D6A7)
        "rejected" -> Color(0xFFEF9A9A)
        else -> Color.White
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        border = BorderStroke(1.dp, Color.LightGray),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = leave.teacherName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text("Days Requested: ${leave.days}")

            Text(
                "From: ${formatter.format(Date(leave.fromDate))}"
            )

            Text(
                "To: ${formatter.format(Date(leave.toDate))}"
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Reason:",
                fontWeight = FontWeight.SemiBold
            )

            Text(leave.reason)

            Spacer(modifier = Modifier.height(16.dp))



            if (status == "pending") {

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Button(
                        onClick = {

                            if (status != "pending") return@Button
                            status = "approved"

                            FirebaseFirestore.getInstance()
                                .collection("leaveRequests")
                                .document(leave.id)
                                .update("status", "approved")
                            FirebaseFirestore.getInstance()
                                .collection("users")
                                .document(leave.teacherId)
                                .update("totalLeaves", FieldValue.increment(-leave.days.toLong()))
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF35E552)
                        )
                    ){
                        Text("Approve")
                    }

                    Button(
                        onClick = {
                            if (status != "pending") return@Button
                            status = "rejected"

                            FirebaseFirestore.getInstance()
                                .collection("leaveRequests")
                                .document(leave.id)
                                .update("status", "rejected")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE53935)
                        )
                    ) {
                        Text("Reject")
                    }

                }
            }

        }

    }
}