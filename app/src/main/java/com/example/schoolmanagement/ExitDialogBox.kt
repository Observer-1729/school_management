package com.example.schoolmanagement

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun ExitAppDialog(
    onConfirmExit: () -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest = { onDismiss() },

        title = {
            Text("Exit App")
        },

        text = {
            Text("Are you sure you want to exit?")
        },

        confirmButton = {

            TextButton(
                onClick = { onConfirmExit() }
            ) {
                Text("Exit", color =Color.Red)

            }

        },

        dismissButton = {

            TextButton(
                onClick = { onDismiss() }
            ) {
                Text("Cancel")
            }

        }
    )
}

@Composable
fun ExitAppHandler() {

    val context = LocalContext.current
    val activity = context as? Activity

    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler {
        showExitDialog = true
    }

    if (showExitDialog) {
        ExitAppDialog(
            onConfirmExit = {
                activity?.finish()
            },
            onDismiss = {
                showExitDialog = false
            }
        )
    }
}