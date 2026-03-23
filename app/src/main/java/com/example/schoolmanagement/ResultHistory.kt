package com.example.schoolmanagement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResultHistoryScreen(
    modifier: Modifier,
    results: List<Pair<String, Float>>
) {

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(results) { (exam, percentage) ->

            ResultCardEnhanced(
                exam = exam,
                percentage = percentage
            )

        }
    }
}

@Composable
fun ResultCardEnhanced(
    exam: String,
    percentage: Float
) {

    val (bgColor, message) = when {
        percentage < 50 -> Color(0xFFFFCDD2) to "Needs improvement 💪"
        percentage < 60 -> Color(0xFFFFE0B2) to "Keep working 👍"
        percentage < 75 -> Color(0xFFFFF9C4) to "Good effort 👏"
        percentage < 90 -> Color(0xFFC8E6C9) to "Great job 🔥"
        else -> Color(0xFFA5D6A7) to "Excellent work 🚀"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp),   // 🔥 Bigger → only ~4 cards visible
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = bgColor
        ),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🔹 Left side (Exam + Message)
            Column(
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                Text(
                    text = exam,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = message,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }

            // 🔹 Right side (Percentage)
            Text(
                text = "${percentage.toInt()}%",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}