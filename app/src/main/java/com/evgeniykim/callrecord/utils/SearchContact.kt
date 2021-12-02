package com.evgeniykim.callrecord.utils

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
    var callsAdapter = JournalAdapter(calls, this)



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
//                audioFile = context?.assets?.openFd("app_src_main_assets_jazz_in_paris.mp3")
                var calling_type: String? = null
                var missingRec: Int? = null
                var arrowSignCalls: Int? = null
                var missingCalltext: String? = null
                var missing: String? = null
                var incoming: String? = null
                var outgoing: String? = null
                var isMissingOutgoing = false
                var isMissingIncoming = false

                var findAudio = FindAudio()

                when (callTypeInt) {
                    CallLog.Calls.OUTGOING_TYPE -> {
//                        outgoing = OUTGOING_CALL
                        calling_type = Constants.OUTGOING_CALL
                        Log.e(Journal.TAG, "Call Type OUT: $callType")
//                        isOutgoing = true
                    }
                    CallLog.Calls.INCOMING_TYPE -> {
//                        incoming = Constants.INCOMING_CALL
                        calling_type = Constants.INCOMING_CALL
                        Log.e(Journal.TAG, "Call Type IN: $callType")
//                        isIncoming = true

                    }
                    CallLog.Calls.MISSED_TYPE -> {
//                        missing = MISSING_CALL
                        calling_type = Constants.MISSING_CALL
                        Log.e(Journal.TAG, "Call Type MISS: $callType")
//                        isMissing = true
                    }
                }

//                if (incomingCallsClicked) {
//                    calling_type = Constants.INCOMING_CALL
//                    incomingCallsClicked = false
//                    callHistory.add(JournalModels(name, convertTime(time), number, convertDate(date),
//                        findSimilarDates(date), calling_type))
//                }
//
//                if (outgoingCallsClicked) {
//                    calling_type = Constants.OUTGOING_CALL
//                    outgoingCallsClicked = false
//                    callHistory.add(JournalModels(name, convertTime(time), number, convertDate(date),
//                        findSimilarDates(date), calling_type))
//                }
//
//                if (missingCallsClicked) {
//                    calling_type = Constants.MISSING_CALL
//                    missingCallsClicked = false
//                    callHistory.add(JournalModels(name, convertTime(time), number, convertDate(date),
//                        findSimilarDates(date), calling_type))
//                }

//                else {
//
//                    callHistory.add(JournalModels(name, convertTime(time), number, convertDate(date), findAudio!!.findRecord(date, requireContext()), calling_type))
//
//                }
                var isCheckAll = 0

                GlobalScope.launch {
                    calls.add(JournalModels(name, convertTime(time), number, convertDate(date), findAudio.findRecord(date, context), calling_type))
                }

                callsAdapter = JournalAdapter(calls, this).also {
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