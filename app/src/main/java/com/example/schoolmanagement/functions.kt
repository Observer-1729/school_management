package com.example.schoolmanagement

import android.net.Uri
import com.cloudinary.android.MediaManager
import com.google.firebase.firestore.FirebaseFirestore

fun uploadFileToCloudinary(
    fileUri: Uri,
    standard: String,
    onComplete: (String) -> Unit
) {

    MediaManager.get().upload(fileUri)
        .unsigned("school_app_upload")
        .callback(object : com.cloudinary.android.callback.UploadCallback {

            override fun onStart(requestId: String?) {
                println("Upload started")
            }

            override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}

            override fun onSuccess(requestId: String?, resultData: Map<*, *>?) {

                val fileUrl = resultData?.get("secure_url").toString()
                println("Uploaded URL: $fileUrl")

                // 🔥 Save to Firestore
                FirebaseFirestore.getInstance()
                    .collection("timetables")
                    .document(standard)
                    .set(
                        mapOf(
                            "fileUrl" to fileUrl,
                            "updatedAt" to System.currentTimeMillis()
                        )
                    )
                    .addOnSuccessListener {
                        onComplete(fileUrl)
                    }
            }

            override fun onError(requestId: String?, error: com.cloudinary.android.callback.ErrorInfo?) {
                println("Upload error: ${error?.description}")
            }

            override fun onReschedule(requestId: String?, error: com.cloudinary.android.callback.ErrorInfo?) {}
        })
        .dispatch()
}