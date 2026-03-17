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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaveRequestsContent(
padding: PaddingValues
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        items(3) {

            LeaveRequestCard(
                teacherName = "Ramesh Patel",
                leavesRemaining = 5,
                leaveDate = "18 March",
                reason = "Fever and doctor visit"
            )
        }

    }
}

@Composable
fun LeaveRequestCard(
    teacherName: String,
    leavesRemaining: Int,
    leaveDate: String,
    reason: String
) {

    var status by remember { mutableStateOf("pending") }

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
                text = teacherName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text("Leaves Remaining: $leavesRemaining")

            Text("Leave Date: $leaveDate")

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Reason:",
                fontWeight = FontWeight.SemiBold
            )

            Text(reason)

            Spacer(modifier = Modifier.height(16.dp))

            if (status == "pending") {

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Button(
                        onClick = { status = "approved" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        )
                    ) {
                        Text("Approve")
                    }

                    Button(
                        onClick = { status = "rejected" },
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