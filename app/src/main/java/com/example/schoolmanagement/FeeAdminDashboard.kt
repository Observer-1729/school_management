package com.example.schoolmanagement

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeeAdminScaffold(
    navController: NavController,
    title: String,
    showSubmit: Boolean = false,
    onSubmitClick: (() -> Unit)? = null,
    onBackClick: (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },

                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (onBackClick != null) {
                                onBackClick()
                            } else {
                                navController.popBackStack()
                            }
                        }
                    )  {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },

                actions = {
                    if (showSubmit && onSubmitClick != null) {
                        Button(
                            onClick = { onSubmitClick() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color(0xFF1976D2)
                            )
                        ) {
                            Text("Submit")
                        }
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1976D2),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        content(padding)
    }
}


@Composable
fun FeeAdminDashboard(
    adminName: String,
    onLeaveApprovalClick: () -> Unit,
    onQuarterSelectionClick: () -> Unit,
    paddingValues: PaddingValues,
    onBackClick: () -> Unit
) {

    ExitAppHandler()

    BackHandler {
        onBackClick()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // TEACHER LEAVE CARD
        Card(
            onClick = { onLeaveApprovalClick() },
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF63A6EB)
            ),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(2.dp, Color(0xFF3A7BD5)),
            elevation = CardDefaults.cardElevation(0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    imageVector = Icons.Default.EventNote,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp),
                    tint = Color.White
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Teacher Leave Requests",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // STUDENT FEES CARD
        Card(
            onClick = { onQuarterSelectionClick() },
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF26A69A)
            ),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(2.dp, Color(0xFF1C8077)),
            elevation = CardDefaults.cardElevation(0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp),
                    tint = Color.White
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Student Fee Management",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}