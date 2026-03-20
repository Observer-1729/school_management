package com.example.schoolmanagement

import android.graphics.Rect
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.cos
import kotlin.math.sin


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentScaffold(
    title: String,
    onBackClick: () -> Unit,
    isPdfScreen: Boolean = false,
    showTabs: Boolean = false,
    selectedTabIndex: Int = 0,
    tabs: @Composable (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {

    Scaffold(
        topBar = {

            Column {

                // 🔵 Top Bar
                TopAppBar(
                    title = {
                        Text(title, color = Color.White)
                    },

                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = if (isPdfScreen)
                                    Icons.Default.Close
                                else
                                    Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    },

                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFB17BF4)
                    )
                )

                // 🔥 Tabs (ONLY when needed)
                if (showTabs && tabs != null) {
                    TabRow(
                        selectedTabIndex = selectedTabIndex,
                        containerColor = Color(0xFFB17BF4),
                        contentColor = Color.White
                    ) {
                        tabs()
                    }
                }
            }
        }
    ) { padding ->
        content(padding)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDashboard(
    studentViewModel: StudentViewModel,
    onSubjectClick: (String) -> Unit
) {


    val attendanceViewModel: AttendanceViewModel = viewModel()

    val attendancePercentage by attendanceViewModel.attendancePercentage
    val isLoading by attendanceViewModel.isLoading

    val student = studentViewModel.studentData
    var feeDue by remember { mutableStateOf(false) }

    val studentName = student.name
    val standard = student.standard


    LaunchedEffect(Unit) {
        attendanceViewModel.loadAttendance(
            standard = standard,
            rollNo = student.rollNo
        )
    }

    var showExitDialog by remember { mutableStateOf(false) }
    val activity = LocalActivity.current
    var subjects by remember { mutableStateOf<List<String>>(emptyList()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = studentName,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { showExitDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFB17BF4)
                )
            )
        }
    ) { paddingValues ->

        println("🔥 STUDENT ROLL NO: ${student.rollNo}")
        println("🔥 STANDARD: ${student.standard}")

        // EXIT APP DIALOG (unchanged)
        if (showExitDialog) {

            AlertDialog(
                onDismissRequest = { showExitDialog = false },

                title = { Text("Exit App") },

                text = { Text("Do you want to exit the app?") },

                confirmButton = {
                    TextButton(
                        onClick = {
                            showExitDialog = false
                            activity?.finish()
                        }
                    ) {
                        Text("Yes", modifier = Modifier.padding(2.dp))
                    }
                },

                dismissButton = {
                    TextButton(
                        onClick = { showExitDialog = false }
                    ) {
                        Text(
                            "No",
                            modifier = Modifier.padding(2.dp),
                            color = Color(0xFFFF0000)
                        )
                    }
                }
            )
        }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // 🔴 Fee Due Notification (appears only when needed)
            if (feeDue) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFD32F2F))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⚠ Fee Payment Pending",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                var timetableUrl by remember { mutableStateOf<String?>(null) }

                LaunchedEffect(standard) {

                    val db = FirebaseFirestore.getInstance()

                    // 🔵 TIMETABLE
                    db.collection("timetables")
                        .document(standard)
                        .get()
                        .addOnSuccessListener { doc ->
                            timetableUrl = doc.getString("fileUrl")
                        }

                    // 🟢 SUBJECTS
                    db.collection("standards")
                        .document(standard)
                        .collection("subjects")
                        .get()
                        .addOnSuccessListener { result ->

                            val subjectList = result.documents.map { it.id }

                            println("Subjects fetched: $subjectList")

                            subjects = subjectList
                        }

                    val studentId = student.userId


                    db.collection("fees")
                        .whereEqualTo("studentId", studentId)
                        .whereEqualTo("standard", standard)
                        .get()
                        .addOnSuccessListener { result ->

                            if (!result.isEmpty) {

                                // 🔥 Find latest quarter
                                val latestDoc = result.documents.maxByOrNull { doc ->
                                    doc.getString("quarter")?.removePrefix("Q")?.toIntOrNull() ?: 0
                                }

                                latestDoc?.let { doc ->

                                    val isPaid = doc.getBoolean("isPaid") ?: false
                                    val reminderSent = doc.getBoolean("reminderSent") ?: false

                                    feeDue = !isPaid && reminderSent
                                }

                            } else {
                                feeDue = false
                            }
                        }
                }

                // 🕒 Timetable Section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {


                        if (timetableUrl != null) {

                            AsyncImage(
                                model = timetableUrl,
                                contentDescription = "Timetable",
                                modifier = Modifier.fillMaxSize()
                            )

                        } else {
                            Text("No timetable uploaded yet")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 📊 Two Side-by-Side Boxes
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(150.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Speedometer(
                                score = 98.2f,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        if (isLoading) {
                            AttendanceBox(0) // or show loading later
                        } else {
                            AttendanceBox(attendancePercentage)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 📚 Subjects Title
                Text(
                    text = "Subjects",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(subjects) { subject ->
                        SubjectCard(subject) {
                            onSubjectClick(subject)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
@Composable
fun AttendanceBox(attendancePercentage: Int) {

    val backgroundColor = when {
        attendancePercentage < 80 -> Color(0xFFE57373) // Red
        attendancePercentage < 90 -> Color(0xFFFFF176) // Yellow
        else -> Color(0xFF81C784) // Green
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "$attendancePercentage%",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SubjectCard(
    subjectName: String,
    onClick: () -> Unit
) {

    val icon = getSubjectIcon(subjectName)

    Card(
        onClick = onClick,
        modifier = Modifier.size(130.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // ICON AREA (takes most space)
            Box(
                modifier = Modifier
                    .weight(0.75f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = subjectName,
                    modifier = Modifier.size(100.dp)   // Bigger icon
                )
            }

            Divider(
                thickness = 1.dp,
                color = Color.LightGray
            )

            // TEXT AREA (small space)
            Box(
                modifier = Modifier
                    .weight(0.25f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = subjectName,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun Speedometer(score: Float, modifier: Modifier = Modifier) {

    Box(modifier = modifier, contentAlignment = Alignment.Center) {

        var targetScore by remember { mutableFloatStateOf(0f) }
        val animatedScore by animateFloatAsState(
            targetValue = targetScore,
            animationSpec = tween(
                durationMillis = 2000,
                easing = FastOutSlowInEasing
            ),
            label = "speedAnimation"
        )
        LaunchedEffect(Unit) {
            targetScore = score
        }

        Canvas(modifier = Modifier.fillMaxSize()) {

            val startAngle = 120f
            val sweepAngle = 300f

            val strokeWidth = 20f

            val center = Offset(size.width / 2, size.height / 2)

            // REAL radius used for everything
            val radius = size.minDimension / 2 - strokeWidth

            val arcRect = Rect(
                (center.x - radius).toInt(),
                (center.y - radius).toInt(),
                (center.x + radius).toInt(),
                (center.y + radius).toInt()
            )

            val arcStyle = Stroke(width = strokeWidth, cap = StrokeCap.Round)

            // RED
            drawArc(
                color = Color.Red,
                startAngle = startAngle,
                sweepAngle = 150f,
                useCenter = false,
                style = arcStyle,
                topLeft = Offset(arcRect.left.toFloat(), arcRect.top.toFloat()),
                size = Size(radius * 2, radius * 2)
            )

            // YELLOW
            drawArc(
                color = Color.Yellow,
                startAngle = startAngle + 150f,
                sweepAngle = 90f,
                useCenter = false,
                style = arcStyle,
                topLeft = Offset(arcRect.left.toFloat(), arcRect.top.toFloat()),
                size = Size(radius * 2, radius * 2)
            )

            // GREEN
            drawArc(
                color = Color.Green,
                startAngle = startAngle + 240f,
                sweepAngle = 60f,
                useCenter = false,
                style = arcStyle,
                topLeft = Offset(arcRect.left.toFloat(), arcRect.top.toFloat()),
                size = Size(radius * 2, radius * 2)
            )

            // TICKS
            val tickCount = 100

            for (i in 0..tickCount) {

                val angle = startAngle + (i / tickCount.toFloat()) * sweepAngle
                val rad = Math.toRadians(angle.toDouble())

                val tickStart = radius - if (i % 10 == 0) 30f else 18f
                val tickEnd = radius + strokeWidth / 2

                val startX = center.x + tickStart * cos(rad).toFloat()
                val startY = center.y + tickStart * sin(rad).toFloat()

                val endX = center.x + tickEnd * cos(rad).toFloat()
                val endY = center.y + tickEnd * sin(rad).toFloat()

                drawLine(
                    color = Color.Black,
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = if (i % 10 == 0) 5f else 2f
                )
            }

            // NEEDLE
            val needleAngle = startAngle + (animatedScore / 100f) * sweepAngle
            val rad = Math.toRadians(needleAngle.toDouble())

            val needleLength = radius * 0.85f

            val needleX = center.x + needleLength * cos(rad).toFloat()
            val needleY = center.y + needleLength * sin(rad).toFloat()

            drawLine(
                color = Color.Red,
                start = center,
                end = Offset(needleX, needleY),
                strokeWidth = 8f,
                cap = StrokeCap.Round
            )

            drawCircle(
                color = Color.Black,
                radius = 10f,
                center = center
            )
            drawContext.canvas.nativeCanvas.apply {

                val textPaint = android.graphics.Paint().apply {
                    color = android.graphics.Color.BLACK
                    textAlign = android.graphics.Paint.Align.CENTER
                    textSize = radius / 2
                    isFakeBoldText = true
                }

                drawText(
                    score.toInt().toString(),
                    center.x,
                    center.y + radius * 0.6f,
                    textPaint
                )
            }
        }
    }
}