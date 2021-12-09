package com.evgeniykim.callrecord.data

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.provider.CallLog
import android.util.Log
import android.widget.Toast
import com.evgeniykim.callrecord.fragments.Journal
import com.evgeniykim.callrecord.models.JournalModels
import com.evgeniykim.callrecord.utils.CompareTodayYesterday
import com.evgeniykim.callrecord.utils.Constants
import com.evgeniykim.callrecord.utils.FindAudio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.text.SimpleDateFormat
import java.util.*

class CallHistoryService(val contentResolver: ContentResolver, val context: Context) {

    @SuppressLint("Range")
    suspend fun getCallsData(prevIndex: Int, pageSize: Int): Flow<List<JournalModels>> = flow {
        val callHistory = arrayListOf<JournalModels>()
        val cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER)
        //TODO this is how was done in sample: CallLog.Calls.DATE + " DESC"

        if (cursor!!.count > 0) {
            while (cursor.moveToNext() && cursor.position < (prevIndex + pageSize)) {
                val number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))
                var name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME))
                if (name == null) { name = number }
                val date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE))
                val timeStr = SimpleDateFormat("HH:mm").format(Date(date))
                val dateStr = SimpleDateFormat("dd-MM-yyyy").format(Date(date))
                val callType = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE))


                val callTypeInt = Integer.parseInt(callType)
                var calling_type: String? = null

                val findAudio = FindAudio()
                val audioRecord = contentResolver.let { findAudio.findRecord(date, it) }

                when (callTypeInt) {
                    CallLog.Calls.OUTGOING_TYPE -> {
                        calling_type = Constants.OUTGOING_CALL
                        Log.e(Journal.TAG, "Call Type OUT: $callType")
                    }
                    CallLog.Calls.INCOMING_TYPE -> {
                        calling_type = Constants.INCOMING_CALL
                        Log.e(Journal.TAG, "Call Type IN: $callType")

                    }
                    CallLog.Calls.MISSED_TYPE -> {
                        calling_type = Constants.MISSING_CALL
                        Log.e(Journal.TAG, "Call Type MISS: $callType")
                    }
                }
                /**
                 * Метод выдачи СЕГОДНЯ/ВЧЕРА
                 */
                /**
                 * Метод выдачи СЕГОДНЯ/ВЧЕРА
                 */
                var day = ""
                val showDay = CompareTodayYesterday()
                if (showDay.isToday(date)) { day = "Сегодня" }
                else if (showDay.isYesterday(date)) { day = "Вчера" } else { day = dateStr }

                callHistory.add(JournalModels(name , timeStr, number, day, audioRecord, calling_type))
            }
        } else {
            Toast.makeText(context, "Нет звонков", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
        emit(callHistory)
    }.flowOn(Dispatchers.IO).catch { e ->
        Log.e("CallHistoryService", "Load from: ", e)
    }
}