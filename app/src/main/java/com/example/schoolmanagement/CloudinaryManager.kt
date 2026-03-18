package com.example.schoolmanagement

import android.content.Context
import com.cloudinary.android.MediaManager

fun initCloudinary(context: Context) {
    val config = mapOf(
        "cloud_name" to "dxtcoprmr",
        "api_key" to "CLOUDINARY_URL=cloudinary://<your_api_key>:<your_api_secret>@dxtcoprmr",
        "api_secret" to "389177921854244"
    )

    MediaManager.init(context, config)
}