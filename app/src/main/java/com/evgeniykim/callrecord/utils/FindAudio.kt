package com.evgeniykim.callrecord.utils

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.net.Uri
import android.provider.MediaStore
import java.text.SimpleDateFormat

class FindAudio {

    fun findRecord(date: Long, context: Context): AssetFileDescriptor? {
        var afd: AssetFileDescriptor? = null
        val cursor2 = context.contentResolver?.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null)

        if (cursor2!!.count > 0) {
            while (cursor2.moveToNext()) {
                val audioId = cursor2.getLong(cursor2.getColumnIndex(MediaStore.Audio.Media._ID))
                val audioName = cursor2.getString(cursor2.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                val audioTitle = cursor2.getString(cursor2.getColumnIndex(MediaStore.Audio.Media.TITLE))
                if (audioName == "${dateStamp(date)}.m4a") {
                    val contentUri: Uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, audioId)

                    val cr: ContentResolver = context.contentResolver
                    afd = cr.openAssetFileDescriptor(contentUri, "r")
                }
            }
        }
        cursor2.close()

        return afd
    }

    private fun dateStamp(date: Long): String {
        val formater = SimpleDateFormat("yyyyMMddhhmm")
        val formatted: String = formater.format(date)
        return formatted
    }
}