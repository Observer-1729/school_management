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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.schoolmanagement.ui.theme.AttendanceScreen
import com.example.schoolmanagement.ui.theme.SubjectScreen

@Composable
fun AppNavigation(navController: NavHostController) {

    val context = LocalContext.current
    val activity = context as Activity

    var showExitDialog by remember { mutableStateOf(false) }

    if (showExitDialog) {
        ExitAppDialog(
            onConfirmExit = { activity.finish() },
            onDismiss = { showExitDialog = false }
        )
    }

    NavHost(navController, startDestination = "Subject Dashboard") {

        composable("splash") {
            SplashScreen(navController)
        }

        composable("login") {
            LoginScreen(
                onNavigateToWelcome = { navController.navigate("welcome") }
            )
        }

        composable("welcome") {
            WelcomePage()
        }

        composable("selection") {
        }

        // DASHBOARD
        composable("feeAdmin Dashboard") {

            FeeAdminScaffold(
                navController = navController,
                title = "Ramesh Patel",
                onBackClick = { showExitDialog = true }
            ) { padding ->

                FeeAdminDashboard(
                    adminName = "Ramesh Patel",
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
                    onQuarterSelected = {
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
                    onStandardSelected = {
                        navController.navigate("Student Fee")
                    }
                )

            }
        }

        // STUDENT FEES
        composable("Student Fee") {

            var feePageChanged by remember { mutableStateOf(false) }

            FeeAdminScaffold(
                navController = navController,
                title = "Student List",
                showSubmit = feePageChanged,
                onSubmitClick = {
                    // TODO: Save changes to database later
                    feePageChanged = false
                }
            ) { padding ->

                StudentFeePage(
                    padding = padding,
                    onChange = { feePageChanged = true }
                )

            }
        }





        composable("Subject Dashboard") {

            SubjectTeacherDashboard(
                teacherName = "Ramesh Patel",
                onBackClick = { showExitDialog = true }
            ) { modifier ->

                SubjectTeacherDashboardContent(
                    modifier = modifier,

                    onLeaveClick = {
                        navController.navigate("Leave Form")
                    },

                    onSubjectClick = {
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

                ApplyLeaveForm()

            }

        }


//        composable("HW and Notes") {
//
//            TeacherUploadScreen(
//                teacherName = "Add Notes / Homework",
//                onBackClick = { navController.popBackStack() }
//            )
//
//        }

        composable("HW and Notes") {

            var selectedTab by remember { mutableStateOf(0) }

            val context = LocalContext.current

            val pdfPicker = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri ->

                if (uri != null) {
                    Toast.makeText(context, "PDF Selected", Toast.LENGTH_SHORT).show()
                }

            }

            SubjectTeacherDashboard(
                teacherName = "Add Notes / Homework",
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

                Column(modifier = modifier) {

                    when (selectedTab) {

                        0 -> NotesContent()

                        1 -> HomeworkContent()

                    }

                }

            }

        }



        composable("Class Dashboard") {

            ClassTeacherScaffold(
                title = "Class Teacher",
                onBackClick = { showExitDialog = true }
            ) { modifier ->

                ClassTeacherDashboardContent(
                    modifier = modifier,

                    onApplyLeaveClick = {
                        navController.navigate("Leave Form")
                    },

                    onAttendanceClick = {
                        navController.navigate("Attendance Page")
                    },

                    onMarksClick = { exam ->
                        navController.navigate("Subject selection/$exam")
                    }
                )

            }
        }
        composable("Attendance Page") {

            val context = LocalContext.current

            ClassTeacherScaffold(
                title = "Attendance",
                onBackClick = { navController.popBackStack() },

                actionText = "Submit",   // ✅ NEW
                onActionClick = {
                    Toast.makeText(
                        context,
                        "Attendance submitted successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            ) { modifier ->

                AttendanceScreen(
                    modifier = modifier
                )

            }
        }

        composable("Subject selection/{examName}") { backStackEntry ->

            val examName = backStackEntry.arguments?.getString("examName") ?: "PA1"

            val subjects = listOf("Maths","Science","English","Hindi")

            ClassTeacherScaffold(
                title = "Select Subject",
                onBackClick = { navController.popBackStack() }
            ) { modifier ->

                SubjectSelectionScreen(
                    modifier = modifier,   // ✅ ADD THIS
                    examName = examName,
                    subjects = subjects,
                    onSubjectClick = { subject ->
                        navController.navigate("Marks Entry/$examName/$subject")
                    }
                )

            }
        }

        composable("Marks Entry/{exam}/{subject}") { backStackEntry ->

            val context = LocalContext.current

            val exam = backStackEntry.arguments?.getString("exam") ?: ""
            val subject = backStackEntry.arguments?.getString("subject") ?: ""

            val students = listOf(
                Student(1,"Rahul"),
                Student(2,"Priya")
            )

            ClassTeacherScaffold(
                title = "$subject - $exam",   // 🔥 Better title
                onBackClick = { navController.popBackStack() },

                actionText = "Save",   // ✅ NEW
                onActionClick = {
                    Toast.makeText(
                        context,
                        "Marks updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            ) { modifier ->

                MarksEntryScreen(
                    modifier = modifier,
                    subject = subject,
                    exam = exam,
                    students = students
                )

            }
        }

        composable("student_dashboard") {

            StudentDashboard(
                studentName = "Sahil",
                attendancePercentage = 85,
                subjects = listOf("Maths", "Science", "English"),
                feeDue = true,

                onSubjectClick = { subject ->
                    navController.navigate("student_subject/${Uri.encode(subject)}")
                }
            )
        }

        composable("student_subject/{subject}") { backStackEntry ->

            val subject = backStackEntry.arguments?.getString("subject") ?: ""

            var selectedTab by remember { mutableStateOf(0) }

            // 🔥 PDF STATE
            var selectedPdfUri by remember { mutableStateOf<Uri?>(null) }
            var selectedPdfTitle by remember { mutableStateOf("") }

            val isPdfOpen = selectedPdfUri != null

            StudentScaffold(
                title = if (isPdfOpen) selectedPdfTitle else subject,

                isPdfScreen = isPdfOpen,

                onBackClick = {
                    if (isPdfOpen) {
                        selectedPdfUri = null   // 🔥 close PDF
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

                    PdfScreen(
                        uri = selectedPdfUri!!
                    )

                } else {

                    SubjectScreen(
                        modifier = Modifier.padding(padding),
                        selectedTab = selectedTab,

                        onPdfOpen = { title, uri ->
                            selectedPdfTitle = title
                            selectedPdfUri = uri
                        }
                    )
                }
            }
        }
    }
}