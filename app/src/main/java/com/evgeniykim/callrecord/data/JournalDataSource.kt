package com.evgeniykim.callrecord.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.evgeniykim.callrecord.models.JournalModels
import kotlinx.coroutines.flow.collect

class JournalDataSource(private val callHistoryService: CallHistoryService) :
    PagingSource<Int, JournalModels>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, JournalModels> {
        val nextPageNumber = params.key ?: 1
        return try {
            val callsHistory : ArrayList<JournalModels> = ArrayList()
            callHistoryService.getCallsData(nextPageNumber, params.loadSize).collect {
                callsHistory.addAll(it)
            }
            LoadResult.Page(
                    data = callsHistory,
                    prevKey = null,
                    nextKey = if (callsHistory.isEmpty()) null else nextPageNumber + params.loadSize)
        }
        catch (exception: Exception) { LoadResult.Error(exception) }
    }

    override fun getRefreshKey(state: PagingState<Int, JournalModels>): Int? {
        return state.anchorPosition
    }

}


//        PositionalDataSource<JournalModels>() {
//
//    companion object {
//        private val PROJECTION = arrayOf(
//            CallLog.Calls.CACHED_NAME,
//            CallLog.Calls.NUMBER,
//            CallLog.Calls.DATE,
//            CallLog.Calls.TYPE
//        )
//    }
//
//    override fun loadInitial(
//        params: LoadInitialParams, callback: LoadInitialCallback<JournalModels>)
//    {
//        callback.onResult(getHistory(params.requestedLoadSize, params.requestedStartPosition), 0)
//    }
//
//    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<JournalModels>) {
//        callback.onResult(getHistory(params.loadSize, params.startPosition))
//    }
//
//    @SuppressLint("Range")
//    fun getHistory(limit: Int, offset: Int): MutableList<JournalModels>{
//        val callHistory: MutableList<JournalModels> = mutableListOf()
//
//        val bundle = Bundle().apply {
//            // Limit & Offset
//            putInt(ContentResolver.QUERY_ARG_SQL_LIMIT, limit)
//            putInt(ContentResolver.QUERY_ARG_OFFSET, offset)
////            putString(ContentResolver.QUERY_ARG_SORT_DIRECTION, CallLog.Calls.DEFAULT_SORT_ORDER)
//            // Sort function
////            putString(
////                ContentResolver.QUERY_ARG_SORT_DIRECTION,
////                CallLog.Calls.DATE
////            )
////            putInt(
////                ContentResolver.QUERY_ARG_SORT_DIRECTION,
////                ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
////            )
//        }
//
//        var cursor = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
//            contentResolver.query(
//                CallLog.Calls.CONTENT_URI,
//                PROJECTION,
//                null,
//                null,
//                "${CallLog.Calls.DATE} DESC LIMIT $limit OFFSET $offset")
//        } else {
//            contentResolver.query(
//                CallLog.Calls.CONTENT_URI,
//                PROJECTION,
//                bundle,
//                null)
//        }
//
//        cursor!!.moveToFirst()
//        while (!cursor.isAfterLast) {
//            val number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))
//            var name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME))
//            if (name == null) { name = number }
//            val date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE))
//            val timeStr = SimpleDateFormat("HH:mm").format(Date(date))
//            val dateStr = SimpleDateFormat("dd-MM-yyyy").format(Date(date))
//            val callType = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE))
//
//            val callTypeInt = Integer.parseInt(callType)
//            var calling_type: String? = null
//
//            val findAudio = FindAudio()
//            val audioRecord = findAudio.findRecord(date, contentResolver)
//
//            when (callTypeInt) {
//                CallLog.Calls.OUTGOING_TYPE -> {
//                    calling_type = Constants.OUTGOING_CALL
//                    Log.e(Journal.TAG, "Call Type OUT: $callType")
//                }
//                CallLog.Calls.INCOMING_TYPE -> {
//                    calling_type = Constants.INCOMING_CALL
//                    Log.e(Journal.TAG, "Call Type IN: $callType")
//
//                }
//                CallLog.Calls.MISSED_TYPE -> {
//                    calling_type = Constants.MISSING_CALL
//                    Log.e(Journal.TAG, "Call Type MISS: $callType")
//                }
//            }
//
//            /**
//             * Метод выдачи СЕГОДНЯ/ВЧЕРА
//             */
//            var day = ""
//            val showDay = CompareTodayYesterday()
//            if (showDay.isToday(date)) { day = "Сегодня" }
//            else if (showDay.isYesterday(date)) { day = "Вчера" } else { day = dateStr }
//
//            callHistory.add(JournalModels(name , timeStr, number, day, audioRecord, calling_type))
//
//            cursor.moveToNext()
//        }
//        //TODO: "${CallLog.Calls.DATE} DESC LIMIT $limit OFFSET $offset"
//        //todo: CallLog.Calls.DATE + " DESC LIMIT " + limit + " OFFSET " + offset
//        //TODO this is how was done in sample: CallLog.Calls.DATE + " DESC"
//        cursor.close()
//        return callHistory
//    }
//}

