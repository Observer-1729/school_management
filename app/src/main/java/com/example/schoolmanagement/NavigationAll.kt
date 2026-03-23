package com.example.schoolmanagement

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.schoolmanagement.ui.theme.AttendanceScreen
import com.example.schoolmanagement.ui.theme.ResultStudent
import com.example.schoolmanagement.ui.theme.Student
import com.example.schoolmanagement.ui.theme.SubjectScreen
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@Composable
fun AppNavigation(navController: NavHostController) {
    val teacherViewModel: TeacherViewModel = viewModel()
    val studentViewModel: StudentViewModel = viewModel()
    val feeViewModel: FeeViewModel = viewModel()

    val context = LocalContext.current
    val activity = context as Activity

    var showExitDialog by remember { mutableStateOf(false) }

    if (showExitDialog) {
        ExitAppDialog(
            onConfirmExit = { activity.finish() },
            onDismiss = { showExitDialog = false }
        )
    }

    NavHost(navController, startDestination = "splash") {

        composable("splash") {
            SplashScreen(navController)
        }

        composable("login") {

            LoginScreen(
                teacherViewModel = teacherViewModel,
                studentViewModel = studentViewModel,
                onNavigate = {
                    navController.navigate("welcome")
                }
            )
        }

        composable("welcome") { backStackEntry ->

            WelcomePage(
                teacherViewModel = teacherViewModel,
                studentViewModel = studentViewModel,
                onNavigateNext = {

                    if (studentViewModel.studentData.name.isNotEmpty()) {
                        navController.navigate("student_dashboard")
                    } else {
                        navController.navigate("selection")
                    }
                }
            )
        }

        composable("selection") { backStackEntry ->

            SelectionTeacher(
                teacherViewModel = teacherViewModel,
                onRoleSelected = { selectedRole ->

                    when (selectedRole) {

                        "class_teacher" -> {
                            navController.navigate("Class Dashboard")
                        }

                        "subject_teacher" -> {
                            navController.navigate("Subject Dashboard")
                        }

                        "fee_admin" -> {
                            navController.navigate("feeAdmin Dashboard")
                        }
                    }
                }
            )
        }

        // DASHBOARD
        composable("feeAdmin Dashboard") {backStackEntry->
            val teacher = teacherViewModel.teacherData

            FeeAdminScaffold(
                navController = navController,
                title = teacher.name,
                onBackClick = { showExitDialog = true }
            ) { padding ->

                FeeAdminDashboard(
                    adminName = teacher.name,
                    paddingValues = padding,
                    onLeaveApprovalClick = {
                        navController.navigate("leave Approval")
                    },
                    onQuarterSelectionClick = {
                        navController.navigate("Quarter Selection")
                    },
                    onBackClick = { showExitDialog = true }
                )

            }
        }

        // LEAVE APPROVAL
        composable("leave Approval") {

            FeeAdminScaffold(
                navController = navController,
                title = "Leave Requests"
            ) { padding ->

                LeaveRequestsContent(padding)

            }
        }

        // QUARTER SELECTION
        composable("Quarter Selection") {

            FeeAdminScaffold(
                navController = navController,
                title = "Select Quarter"
            ) { padding ->

                QuarterSelectionPage(
                    padding = padding,
                    onQuarterSelected = { quarter ->

                        feeViewModel.setQuarter(quarter)   // ✅ STORE

                        navController.navigate("Standard Selection")
                    }
                )

            }
        }

        // STANDARD SELECTION
        composable("Standard Selection") {

            FeeAdminScaffold(
                navController = navController,
                title = "Select Standard"
            ) { padding ->

                StandardSelectionPage(
                    padding = padding,
                    onStandardSelected = { standard ->

                        feeViewModel.setStandard(standard)   // ✅ STORE (already String)

                        navController.navigate("Student Fee")
                    }
                )

            }
        }

        // STUDENT FEES
        composable("Student Fee") {


            FeeAdminScaffold(
                navController = navController,
                title = "Student List"
            ) { padding ->

                StudentFeePage(
                    padding = padding,
                    feeViewModel = feeViewModel
                )

            }
        }





        composable("Subject Dashboard") { backStackEntry ->
            val teacher = teacherViewModel.teacherData
            var selectedTab by remember { mutableStateOf(0) }
            var selectedUri by remember { mutableStateOf<Uri?>(null) }
            var showDialog by remember { mutableStateOf(false) }

            SubjectTeacherDashboard(
                teacherName = teacher.name,
                onBackClick = { showExitDialog = true }
            ) { modifier ->

                SubjectTeacherDashboardContent(
                    modifier = modifier,
                    teacherViewModel = teacherViewModel,   // 🔥 ADD THIS

                    onLeaveClick = {
                        navController.navigate("Leave Form")
                    },

                    onSubjectClick = { subjectClass ->

                        teacherViewModel.selectSubjectClass(subjectClass)

                        navController.navigate("HW and Notes")
                    }
                )

            }

        }
        composable("Leave Form") {

            SubjectTeacherDashboard(
                teacherName = "Leave Form",
                onBackClick = { navController.popBackStack() }
            ) { modifier ->

                ApplyLeaveForm(teacherViewModel = teacherViewModel,modifier = modifier)

            }

        }



        composable("HW and Notes") {

            var selectedTab by remember { mutableStateOf(0) }

            val selected = teacherViewModel.selectedSubjectClass

            val context = LocalContext.current

            var selectedPdfUri by remember { mutableStateOf<Uri?>(null) }
            var selectedFileName by remember { mutableStateOf("") }

            val notesList = teacherViewModel.notesList
            val homeworkList = teacherViewModel.homeworkList

            var lastPickedUri by remember { mutableStateOf<Uri?>(null) }


            val pdfPicker = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri ->

                if (uri != null && uri != lastPickedUri) {

                    lastPickedUri = uri   // 🔥 prevents duplicate trigger

                    val fileName = getFileName(context, uri)

                    val newPdf = PdfItem(
                        id = UUID.randomUUID().toString(),
                        title = fileName,
                        pdfUrl = "",
                        subject = selected?.subject ?: "",
                        standard = selected?.className ?: "",
                        type = if (selectedTab == 0) "notes" else "homework",
                        isUploading = true
                    )

                    teacherViewModel.addPdf(newPdf)

                    uploadPdfToCloudinary(uri, newPdf) { url ->
//                        teacherViewModel.updatePdf(newPdf.id, url)
//
//                        Toast.makeText(context, "Uploaded successfully", Toast.LENGTH_SHORT).show()

                        if (url.isNotEmpty()) {
                            teacherViewModel.updatePdf(newPdf.id, url)
                            Toast.makeText(context, "Uploaded successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Upload failed", Toast.LENGTH_SHORT).show()
                        }
                    }

//
//                    if (selectedTab == 0) {
//                        teacherViewModel.addPdf(newPdf)
//                    } else {
//                        teacherViewModel.addPdf(newPdf)
//                    }
                }
            }

            SubjectTeacherDashboard(
                teacherName = selected?.let {
                    "${it.subject} - ${it.className}"
                } ?: "Add Notes / Homework",

                onBackClick = { navController.popBackStack() },

                tabs = {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Color(0xFF1976D2),
                        contentColor = Color.White
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text("Notes") }
                        )

                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text("Homework") }
                        )
                    }
                },

                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        onClick = {
                            pdfPicker.launch("application/pdf")
                        },
                        containerColor = Color(0xFF1976D2)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White
                        )

                        Spacer(Modifier.width(6.dp))

                        Text(
                            if (selectedTab == 0) "Upload Note" else "Upload Homework",
                            color = Color.White
                        )
                    }
                }

            ) { modifier ->
                LaunchedEffect(selected) {
                    selected?.let {
                        teacherViewModel.loadPdfs(
                            subject = it.subject,
                            standard = it.className.replace("Class ", "")
                        )
                    }
                }

                Column(modifier = modifier) {

                    TeacherMaterialScreen(
                        selectedTab = selectedTab,
                        notes = notesList,
                        homework = homeworkList
                    )

                }
            }

        }



        composable("Class Dashboard") {backStackEntry ->
            val teacher = teacherViewModel.teacherData

            ClassTeacherScaffold(
                title = teacher.name,
                standard = teacher.classTeacherOf ?: "",
                onBackClick = { showExitDialog = true }
            ) { modifier ->

                ClassTeacherDashboardContent(
                    modifier = modifier,
                    teacherViewModel = teacherViewModel,

                    onApplyLeaveClick = {
                        navController.navigate("Leave Form")
                    },

                    onAttendanceClick = {
                        navController.navigate("Attendance Page")
                    },

                    onMarksClick = { exam ->
                        navController.navigate("marksScreen/$exam")
                    }
                )

            }
        }
        composable("Attendance Page") {

            val teacher = teacherViewModel.teacherData
            val standard = teacher.classTeacherOf ?: ""
            val context = LocalContext.current

            val studentsState = remember { mutableStateListOf<Student>() }

            ClassTeacherScaffold(
                title = "Attendance",
                onBackClick = { navController.popBackStack() },
                standard = standard,

                actionText = "Submit",
                onActionClick = {

                    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                    val map = studentsState.associate {
                        it.rollNo.toString() to if (it.isPresent) "present" else "absent"
                    }

                    FirebaseFirestore.getInstance()
                        .collection("attendance")
                        .document(standard)
                        .collection(today)
                        .document("data")
                        .set(map)

                    Toast.makeText(
                        context,
                        "Attendance submitted successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            ) { modifier ->

                AttendanceScreen(
                    modifier = modifier,
                    teacherViewModel = teacherViewModel,
                    students = studentsState   // 🔥 shared state
                )
            }
        }


        composable("marksScreen/{exam}") { backStackEntry ->

            val context = LocalContext.current
            val teacher = teacherViewModel.teacherData
            val standard = teacher.classTeacherOf ?: ""

            val exam = backStackEntry.arguments?.getString("exam") ?: ""

            val marks = remember { mutableStateMapOf<Int, String>() }

            val studentsState = remember { mutableStateListOf<ResultStudent>() }

            LaunchedEffect(standard) {

                val db = FirebaseFirestore.getInstance()

                db.collection("students")
                    .whereEqualTo("standard", standard)
                    .get()
                    .addOnSuccessListener { result ->

                        studentsState.clear()

                        for (doc in result.documents) {

                            val student = ResultStudent(
                                studentId = doc.id,   // 🔥 IMPORTANT
                                rollNo = doc.getLong("rollNumber")?.toInt() ?: 0,
                                name = doc.getString("name") ?: ""
                            )

                            studentsState.add(student)
                        }

                        println("✅ Students Loaded: ${studentsState.size}")
                    }
                    .addOnFailureListener {
                        println("❌ Error fetching students: ${it.message}")
                    }
            }

            ClassTeacherScaffold(
                title = exam,   // 🔥 Better title
                onBackClick = { navController.popBackStack() },
                standard = standard,


                actionText = "Save",   // ✅ NEW
                onActionClick = {

                    val db = FirebaseFirestore.getInstance()

                    for (student in studentsState) {

                        val percentage = marks[student.rollNo]?.toFloatOrNull()

                        if (percentage != null) {

                            db.collection("results")
                                .document(student.studentId)   // ✅ now valid
                                .collection("exams")
                                .document(exam)
                                .set(
                                    mapOf(
                                        "percentage" to percentage
                                    )
                                )
                        }
                    }

                    Toast.makeText(
                        context,
                        "Results saved successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            ) { modifier ->

                MarksEntryScreen(
                    modifier = modifier,
                    exam = exam,
                    students = studentsState,
                    marks = marks
                )

            }
        }

        composable("student_dashboard") {backStackEntry->
            val name = backStackEntry.arguments?.getString("name") ?: ""
//            val standard = backStackEntry.arguments?.getString("standard") ?: ""
            val standard = studentViewModel.studentData.standard

            StudentDashboard(
                studentViewModel = studentViewModel,
                onSubjectClick = { subject ->
                    navController.navigate("student_subject/${Uri.encode(subject)}/$standard")
                },
                onSpeedoMeterClick ={
                    navController.navigate("resultHistory")
                }
            )
        }

        composable("student_subject/{subject}/{standard}") { backStackEntry ->

            val subject = backStackEntry.arguments?.getString("subject") ?: ""

            val studentViewModel: StudentViewModel = viewModel()

            val standard = backStackEntry.arguments?.getString("standard") ?: ""

            var selectedTab by remember { mutableStateOf(0) }

            // 🔥 LOAD DATA
            LaunchedEffect(subject) {
                studentViewModel.loadPdfs(subject, standard)
            }

            val notes = studentViewModel.notesList
            val homework = studentViewModel.homeworkList

            var selectedPdfUrl by remember { mutableStateOf<String?>(null) }
            var selectedPdfTitle by remember { mutableStateOf("") }

            val isPdfOpen = selectedPdfUrl != null

            StudentScaffold(
                title = if (isPdfOpen) selectedPdfTitle else subject,
                isPdfScreen = isPdfOpen,

                onBackClick = {
                    if (isPdfOpen) {
                        selectedPdfUrl = null   // 🔥 instead of Uri
                    } else {
                        navController.popBackStack()
                    }
                },

                showTabs = !isPdfOpen,
                selectedTabIndex = selectedTab,

                tabs = {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Notes") }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Homework") }
                    )
                }

            ) { padding ->

                if (isPdfOpen) {

                    PdfScreen(url = selectedPdfUrl!!)

                } else {

                    SubjectScreen(
                        modifier = Modifier.padding(padding),
                        selectedTab = selectedTab,
                        notes = notes,
                        homework = homework,
                        onPdfOpen = { title, url ->
                            selectedPdfTitle = title
                            selectedPdfUrl = url
                        }
                    )
                }
            }
        }
        composable("resultHistory") {

            val student = studentViewModel.studentData
            val studentId = student.userId

            var results by remember { mutableStateOf<List<Pair<String, Float>>>(emptyList()) }

            LaunchedEffect(Unit) {

                FirebaseFirestore.getInstance()
                    .collection("results")
                    .document(studentId)
                    .collection("exams")
                    .get()
                    .addOnSuccessListener { result ->

                        val tempList = mutableListOf<Pair<String, Float>>()

                        for (doc in result.documents) {

                            val exam = doc.id
                            val percentage = doc.getDouble("percentage")?.toFloat() ?: 0f

                            tempList.add(exam to percentage)
                        }

                        // 🔥 Sort by PA number (PA1, PA2…)
                        results = tempList.sortedBy {
                            it.first.removePrefix("PA").toIntOrNull() ?: 0
                        }
                    }
            }

            StudentScaffold(
                title = "Exam Results",
                onBackClick = { navController.popBackStack() }
            ) { padding ->

                ResultHistoryScreen(
                    modifier = Modifier.padding(padding),
                    results = results
                )
            }
        }
    }
}