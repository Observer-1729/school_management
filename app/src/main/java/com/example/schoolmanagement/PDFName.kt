package com.example.schoolmanagement

import android.content.Context
import android.net.Uri

fun getFileName(context: Context, uri: Uri): String {
    var name = "PDF"

    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        val nameIndex = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
        if (it.moveToFirst() && nameIndex != -1) {
            name = it.getString(nameIndex)
        }
    }

    return name
}