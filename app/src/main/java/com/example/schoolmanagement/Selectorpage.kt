package com.example.schoolmanagement

import android.util.Size
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun SelectionTeacher() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Select Your Role",
            style = MaterialTheme.typography.headlineLarge,
            fontFamily = FontFamily.Cursive,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFB17BF4)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "You can change it later in Profile.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            fontFamily = FontFamily.Serif
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                RoleCard(
                    title = "Class Teacher",
                    imageRes = R.drawable.class_teacher,
                    modifier = Modifier.weight(1f),
                    enabled = true
                )

                RoleCard(
                    title = "Subject Teacher",
                    imageRes = R.drawable.subject_teacher,
                    modifier = Modifier.weight(1f),
                    enabled = true
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                RoleCard(
                    title = "Administrator",
                    imageRes = R.drawable.administrator,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .aspectRatio(1f),
                    enabled = true
                )
            }

        }
    }
}


@Composable
fun RoleCard(
    title: String,
    imageRes: Int,
    enabled: Boolean = true,   // NEW
    modifier: Modifier = Modifier
) {

    val containerColor =
        if (enabled)
            MaterialTheme.colorScheme.surfaceVariant
        else
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)

    val contentAlpha = if (enabled) 1f else 0.4f

    Card(
        onClick = { if (enabled) { /* handle click */ } },
        modifier = modifier.aspectRatio(1f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier.size(90.dp),
                alpha = contentAlpha   // Image becomes gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Divider(
                modifier = Modifier.fillMaxWidth(0.6f),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = contentAlpha)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = contentAlpha)
            )
        }
    }
}
