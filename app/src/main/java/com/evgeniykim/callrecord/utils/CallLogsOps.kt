package com.evgeniykim.callrecord.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.CallLog
import androidx.recyclerview.widget.RecyclerView
import com.evgeniykim.callrecord.adapters.CallerDetailsAdapter
import com.evgeniykim.callrecord.models.CallerDetailsModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class CallLogsOps() {

    var calls = ArrayList<CallerDetailsModel>()
    var callsAdapter = CallerDetailsAdapter(calls)

    @SuppressLint("Range")
    fun getCallLogData(context: Context, recyclerView: RecyclerView, pNumber: String){

        val cursor = context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,null, null, null,
            CallLog.Calls.DATE + " DESC")
        if (cursor!!.count > 0) {
            while (cursor.moveToNext()) {
                val name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME))
                val name2 = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME))
                val dateLong = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE))
                val number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))

                val findAudio = FindAudio()

                if (pNumber == number) {
                    GlobalScope.launch {
                        calls.add(CallerDetailsModel(name, name2, convertTime(dateLong), number, convertDate(dateLong), findAudio.findRecord(dateLong, context.contentResolver), null))
                    }
                    callsAdapter = CallerDetailsAdapter(calls).also {
                        recyclerView.adapter = it
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun convertTime(date: Long): String {
        val formater = SimpleDateFormat("HH:mm")
        val formatted: String = formater.format(date)
        return formatted
    }

    private fun convertDate(date: Long): String {
        val formater = SimpleDateFormat("dd-MM-yyyy")
        val formatted: String = formater.format(date)
        return formatted
    }


}