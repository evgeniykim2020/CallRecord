package com.evgeniykim.callrecord.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.CallLog
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.evgeniykim.callrecord.adapters.AdapterListener
import com.evgeniykim.callrecord.adapters.JournalAdapter
import com.evgeniykim.callrecord.fragments.Journal
import com.evgeniykim.callrecord.models.JournalModels
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class SearchContact: AdapterListener {

    var calls = ArrayList<JournalModels>()
    var matchedCalls = ArrayList<JournalModels>()
    val diffCallback = DiffCallback()
    var callsAdapter = JournalAdapter(diffCallback, this)

    @SuppressLint("Range")
    fun searchContact(context: Context, recyclerView: RecyclerView, searchView: SearchView) {
        val cursor = context.contentResolver?.query(
            CallLog.Calls.CONTENT_URI,null, null, null,
            CallLog.Calls.DATE + " DESC")
        if (cursor!!.count > 0) {

            while (cursor.moveToNext()) {
                val number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))
                val name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME))
                val time = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE))
                val date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE))
                val callType = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE))

                val callTypeInt = Integer.parseInt(callType)
                var calling_type: String? = null

                var findAudio = FindAudio()

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

                var isCheckAll = 0

                GlobalScope.launch {
                    calls.add(JournalModels(name, convertTime(time), number, convertDate(date), findAudio.findRecord(date, context.contentResolver), calling_type))
                }

                callsAdapter = JournalAdapter(diffCallback, this).also {
                    recyclerView.adapter = it
                    it.notifyDataSetChanged()
                }
                searchView.isSubmitButtonEnabled = true
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        search(query, context, recyclerView)
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        search(newText, context, recyclerView)
                        return true
                    }

                })
            }

        } else {
            Toast.makeText(context, "Нет звонков", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
    }

    private fun search(text: String?, context: Context, recyclerView: RecyclerView) {
        matchedCalls = arrayListOf()

        text?.let {
            calls.forEach { call ->
                if ( call.name?.contains(text, true) == true || call.phoneNum.contains(text, true)) {
                    matchedCalls.add(call)
                }
            }
            updateRecyclerView(recyclerView)

            if (matchedCalls.isEmpty()) {
                Toast.makeText(context, "No match found", Toast.LENGTH_SHORT).show()
            }
            updateRecyclerView(recyclerView)
        }
    }

    private fun updateRecyclerView(recyclerView: RecyclerView) {
        recyclerView.apply {
            callsAdapter.journalList = matchedCalls
            callsAdapter.notifyDataSetChanged()
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

    override fun selectItem() {
        //TODO: make selections if required
    }

    override fun unselectItem() {
    }


}