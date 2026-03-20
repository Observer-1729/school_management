package com.example.schoolmanagement

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AttendanceViewModel : ViewModel() {

    var attendancePercentage = mutableStateOf(0)
        private set

    var isLoading = mutableStateOf(false)
        private set

    private val db = FirebaseFirestore.getInstance()

    fun loadAttendance(standard: String, rollNo: Int) {

        viewModelScope.launch {

            isLoading.value = true

            val start = Calendar.getInstance().apply {
                set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR))
                set(Calendar.MONTH, Calendar.MARCH)
                set(Calendar.DAY_OF_MONTH, 1)
            }

            val end = Calendar.getInstance()

            val dates = generateDates(start, end)

            var presentDays = 0
            var totalDays = 0

            for (date in dates) {

                val parsedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date)
                val cal = Calendar.getInstance().apply { time = parsedDate!! }

                val isSunday = cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
                if (isSunday) continue

                try {
                    val doc = db.collection("attendance")
                        .document(standard)
                        .collection(date)
                        .document("data")
                        .get()
                        .await()

                    if (doc.exists()) {

                        val status = doc.getString(rollNo.toString())

                        if (status != null) {
                            totalDays++

                            if (status == "present") {
                                presentDays++
                            }
                        }
                    }
                    println("📄 Doc exists: ${doc.exists()}")
                    println("📄 Data: ${doc.data}")

                } catch (e: Exception) {
                    // skip (holiday / no data)
                }
            }

            attendancePercentage.value =
                if (totalDays == 0) 0
                else ((presentDays.toFloat() / totalDays) * 100).toInt()

            isLoading.value = false
        }

    }

    private fun generateDates(start: Calendar, end: Calendar): List<String> {

        val dates = mutableListOf<String>()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val cal = start.clone() as Calendar

        while (!cal.after(end)) {
            dates.add(sdf.format(cal.time))
            cal.add(Calendar.DATE, 1)
        }

        return dates
    }
}