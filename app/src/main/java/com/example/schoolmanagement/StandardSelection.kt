package com.example.schoolmanagement

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
fun StandardSelectionPage(
    onStandardSelected: (String) -> Unit,
    padding: PaddingValues
) {

    val colors = listOf(
        Color(0xFFBBDEFB), // light blue
        Color(0xFFC8E6C9), // light green
        Color(0xFFFFE0B2), // light orange
        Color(0xFFE1BEE7), // light purple
        Color(0xFFFFCDD2)  // light red
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .height(480.dp),   // controls vertical centering
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(9) { index ->

                val standard = index + 1

                StandardCard(
                    standard = standard,
                    color = colors[index % colors.size],
                    onClick = { onStandardSelected((standard).toString()) }
                )
            }

            // empty left
            item { Spacer(modifier = Modifier) }

            // Std 10 centered
            item {
                StandardCard(
                    standard = 10,
                    color = colors[9 % colors.size],
                    onClick = { onStandardSelected((10).toString()) }
                )
            }

            // empty right
            item { Spacer(modifier = Modifier) }
        }
    }
}

@Composable
fun StandardCard(
    standard: Int,
    color: Color,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f)),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "Std $standard",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}