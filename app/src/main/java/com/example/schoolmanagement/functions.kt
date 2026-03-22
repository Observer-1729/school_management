package com.example.schoolmanagement

import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
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

fun uploadPdfToCloudinary(
    fileUri: Uri,
    pdfItem: PdfItem,
    onComplete: (String) -> Unit
) {

    MediaManager.get().upload(fileUri)
        .unsigned("school_app_upload")
        .option("resource_type", "raw")
        .callback(object : com.cloudinary.android.callback.UploadCallback {

            override fun onStart(requestId: String?) {
                println("Upload started")
            }

            override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}

            override fun onSuccess(requestId: String?, resultData: Map<*, *>?) {

                val fileUrl = resultData?.get("secure_url").toString()

                println("Uploaded URL: $fileUrl")

                // 🔥 Save to Firestore (NEW COLLECTION)
                FirebaseFirestore.getInstance()
                    .collection("pdfs")
                    .document(pdfItem.id)
                    .set(
                        mapOf(
                            "id" to pdfItem.id,
                            "title" to pdfItem.title,
                            "url" to fileUrl,
                            "subject" to pdfItem.subject,
                            "standard" to pdfItem.standard.replace("Class ", ""),
                            "type" to pdfItem.type,
                            "timestamp" to System.currentTimeMillis()
                        )
                    )
                    .addOnSuccessListener {
                        onComplete(fileUrl)
                    }
            }

            override fun onError(requestId: String?, error: ErrorInfo?) {
                println("Upload error: ${error?.description}")
                onComplete("")   // or handle error
            }

            override fun onReschedule(requestId: String?, error: com.cloudinary.android.callback.ErrorInfo?) {}
        })
        .dispatch()
}