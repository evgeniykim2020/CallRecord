package com.evgeniykim.callrecord.models

import android.content.res.AssetFileDescriptor

data class JournalModels (
        val name: String? = null,
        val time: String,
        val phoneNum: String,
        val date: String,
        val recordedFile: AssetFileDescriptor?,
        val callType: String?
        )