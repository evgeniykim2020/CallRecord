package com.evgeniykim.callrecord.models

import android.content.res.AssetFileDescriptor

data class CallerDetailsModel(
    val name: String? = null,
    val secondName: String? = null,
    val time: String?,
    val phoneNum: String?,
    val date: String?,
    val recordedFile: AssetFileDescriptor?,
    val callType: String?
)