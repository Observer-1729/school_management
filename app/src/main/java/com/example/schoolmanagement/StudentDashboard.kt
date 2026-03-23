package com.example.schoolmanagement

import android.graphics.Rect
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.schoolmanagement.ui.theme.CardGlass
import com.example.schoolmanagement.ui.theme.DarkPurple
import com.example.schoolmanagement.ui.theme.LightPurple
import com.example.schoolmanagement.ui.theme.PrimaryPurple
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
    onSubjectClick: (String) -> Unit,
    onSpeedoMeterClick: () -> Unit
) {


    val attendanceViewModel: AttendanceViewModel = viewModel()

    val attendancePercentage by attendanceViewModel.attendancePercentage
    val isLoading by attendanceViewModel.isLoading

    val student = studentViewModel.studentData
    var feeDue by remember { mutableStateOf(false) }

    val studentName = student.name
    val standard = student.standard

    var latestPercentage by remember { mutableStateOf(0f) }
    var isLoadingResult by remember { mutableStateOf(true) }


    LaunchedEffect(Unit) {
        attendanceViewModel.loadAttendance(
            standard = standard,
            rollNo = student.rollNo
        )
    }
    LaunchedEffect(Unit) {

        val db = FirebaseFirestore.getInstance()
        val studentId = student.userId   // 🔥 IMPORTANT

        db.collection("results")
            .document(studentId)
            .collection("exams")
            .get()
            .addOnSuccessListener { result ->

                if (!result.isEmpty) {

                    // 🔥 Find latest exam (PA1, PA2, PA3…)
                    val latestDoc = result.documents.maxByOrNull { doc ->
                        doc.id.removePrefix("PA").toIntOrNull() ?: 0
                    }

                    latestPercentage =
                        latestDoc?.getDouble("percentage")?.toFloat() ?: 0f
                }

                isLoadingResult = false
            }
    }

    var showExitDialog by remember { mutableStateOf(false) }
    val activity = LocalActivity.current
    var subjects by remember { mutableStateOf<List<String>>(emptyList()) }

    Scaffold(
        topBar = {
            Column {

                // 🔹 DARK PURPLE (Status Bar Area)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkPurple) // Dark Purple
                        .statusBarsPadding() // 🔥 IMPORTANT
                )

                // 🔹 LIGHT PURPLE (App Bar)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PrimaryPurple) // Your existing purple
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(onClick = { showExitDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Text(
                        text = studentName,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
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

                Text(
                    text = "Timetable",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // 🕒 Timetable Section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp),
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
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )

                        } else {
                            Text("No timetable uploaded yet")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Performance",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // 📊 Two Side-by-Side Boxes
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(150.dp)
                            .clickable { onSpeedoMeterClick() },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = LightPurple // 🔥 soft background
                        ),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (isLoadingResult) {
                                Speedometer(score = 0f)
                            } else {
                                Speedometer(score = latestPercentage)
                            }
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
        attendancePercentage < 80 -> Color(0xFFFFCDD2) // soft red
        attendancePercentage < 90 -> Color(0xFFFFF9C4) // soft yellow
        else -> Color(0xFFC8E6C9) // soft green
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔹 Heading
            Text(
                text = "Attendance",
                style = MaterialTheme.typography.titleMedium
            )

            // 🔹 Percentage
            Text(
                text = "$attendancePercentage%",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
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
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardGlass // 🔥 translucent
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
            ) {

                // 🔹 Image fills everything
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = subjectName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // 🔹 Gradient overlay (VERY IMPORTANT)
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.5f)
                                )
                            )
                        )
                )

                // 🔹 Text on image (clean)
                Text(
                    text = subjectName,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(8.dp)
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