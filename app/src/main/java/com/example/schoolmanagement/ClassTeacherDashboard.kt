package com.example.schoolmanagement

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassTeacherScaffold(
    title: String,
    onBackClick: () -> Unit,
    actionText: String? = null,
    standard: String,
    onActionClick: (() -> Unit)? = null,
    content: @Composable (Modifier) -> Unit
) {

    Scaffold(

        topBar = {

            TopAppBar(

                title = { Text(title) },

                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },

                actions = {
                    if (actionText != null && onActionClick != null) {
                        Text(
                            text = actionText,
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .clickable { onActionClick() },
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )

            )
        }

    ) { paddingValues ->

        val modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)

        content(modifier)
    }
}


@Composable
fun ClassTeacherDashboardContent(
    teacherViewModel: TeacherViewModel,
    modifier: Modifier,
    onApplyLeaveClick: () -> Unit,
    onAttendanceClick: () -> Unit,
    onMarksClick: (String) -> Unit,
) {

    val teacher = teacherViewModel.teacherData

    val standard = teacher.classTeacherOf ?: ""
    val leavesLeft = teacher.totalLeaves.toInt()

    var showPicker by remember { mutableStateOf(false) }

    val context = LocalContext.current

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var timetableUrl by remember { mutableStateOf<String?>(null) }


    LaunchedEffect(standard) {
        FirebaseFirestore.getInstance()
            .collection("timetables")
            .document(standard)
            .get()
            .addOnSuccessListener { doc ->
                timetableUrl = doc.getString("fileUrl")
            }
    }

    val imageFile = remember {
        File.createTempFile("timetable_", ".jpg", context.cacheDir)
    }

    val imageUri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        imageFile
    )

    val cameraLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { success ->
            if (success) {
                selectedImageUri = imageUri

                uploadFileToCloudinary(imageUri, standard) { url ->

                    Toast.makeText(context, "Uploaded successfully", Toast.LENGTH_SHORT).show()

                    timetableUrl = url   // 🔥 instant UI update
                }
            }
        }

    val galleryLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->

            if (uri == null) {
                println("No image selected")
                return@rememberLauncherForActivityResult
            }

            println("Gallery image selected: $uri")

            selectedImageUri = uri

            uploadFileToCloudinary(uri, standard) { url ->

                Toast.makeText(context, "Uploaded successfully", Toast.LENGTH_SHORT).show()

                timetableUrl = url   // 🔥 instant UI update
            }
        }

    Column(
        modifier = modifier
    ) {

        TimeTableCard(
            imageUri = selectedImageUri,
            imageUrl = timetableUrl,
            onClick = { showPicker = true }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LeaveCard(
            leavesLeft = leavesLeft,
            onApplyLeaveClick = {
                onApplyLeaveClick()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        AttendanceCard {
            onAttendanceClick()
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "EXAMS",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        ExamGrid(
            onMarksClick = onMarksClick
        )
    }

    // Bottom Sheet stays OUTSIDE Column
    if (showPicker) {

        TimeTablePicker(

            onDismiss = { showPicker = false },

            onCameraClick = {
                showPicker = false
                cameraLauncher.launch(imageUri)
            },

            onGalleryClick = {
                showPicker = false
                galleryLauncher.launch("image/*")
            }

        )
    }
}

@Composable
fun TimeTableCard(
    imageUri: Uri?,
    imageUrl: String?,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            // Show image if timetable exists
            when {
                imageUri != null -> {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Time Table",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                imageUrl != null -> {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Time Table",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                else -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            modifier = Modifier.size(42.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Upload Time Table",
                            fontWeight = FontWeight.Medium,
                            fontSize = 17.sp
                        )
                    }
                }
            }

        }

    }
}

@Composable
fun AttendanceCard(onClick: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.HowToReg,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Take Attendance",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ExamGrid(
    onMarksClick: (String) -> Unit
) {

    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            ExamCard("PA1") { onMarksClick("PA1") }
            ExamCard("PA2") { onMarksClick("PA2") }

        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            ExamCard("PA3") { onMarksClick("PA3") }
            ExamCard("PA4") { onMarksClick("PA4") }

        }

    }

}

@Composable
fun ExamCard(
    examName: String,
    onMarksClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .width(160.dp)
            .height(100.dp)
            .clickable {
                onMarksClick()
            },
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = examName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeTablePicker(
    onDismiss: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            Text(
                text = "Upload Time Table",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onCameraClick
            ) {
                Text("Take Photo")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onGalleryClick
            ) {
                Text("Upload From Gallery")
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun LeaveCard(
    leavesLeft: Int,
    onApplyLeaveClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {

                Text(
                    text = "Leaves Left",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "$leavesLeft",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = onApplyLeaveClick
            ) {
                Text("Apply Leave")
            }

        }
    }
}