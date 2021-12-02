package com.evgeniykim.callrecord.fragments

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.CallLog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.evgeniykim.callrecord.R
import com.evgeniykim.callrecord.adapters.AdapterListener
import com.evgeniykim.callrecord.adapters.JournalAdapter
import com.evgeniykim.callrecord.databinding.FragmentJournalBinding
import com.evgeniykim.callrecord.listeners.OnCallListener
import com.evgeniykim.callrecord.listeners.Utility
import com.evgeniykim.callrecord.models.JournalModels
import com.evgeniykim.callrecord.utils.*
import kotlinx.android.synthetic.main.fragment_journal.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat

class Journal: Fragment(), OnCallListener<JournalModels>, AdapterListener {

    private var adapter: JournalAdapter? = null
    private lateinit var RV: RecyclerView
    private var filterClicked = false
    private var callsList = ArrayList<JournalModels>()
    private var incomingCallsClicked = false
    private var missingCallsClicked = false
    private var outgoingCallsClicked = false
    private var findAudio: FindAudio? = null
    private var searchContact: SearchContact? = null
    private var allChecked = false
    private var quantityItemsChecked = "0"
    private var filterIncoming = false
    private var filterOutgoing = false
    private var filterMissing = false
    private var filterAllCalls = false
    private lateinit var requireContext: Context
    private var waveform: Waveform? = null

    companion object{
        const val TAG = "CALL_RECORD"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentJournalBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).setSupportActionBar(journal_toolbar)
        RV = binding.recyclerJournal
        RV.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        requireContext = requireContext()
        binding.progressBar.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = JournalAdapter(callsList, this)
        adapter?.setCallListener(this)
        RV.adapter = adapter

//        viewLifecycleOwner.lifecycleScope.launch {
//            delay(1000)
//            showHistory()
//            adapter.notifyDataSetChanged()
//        }

//        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main){
//            delay(1000)
//            getHistory()
//            adapter.notifyDataSetChanged()
//        }
        doJournalView()
        /**
         * filter
         */
        clickFiltrBtn()
        setinitialCols()

        sortCalls()

        searchClicked()
        closeSearch()

        checkAllBtn()
        unCheckAllBtn()

//        selectedItems()

        showDialer()
        hideDialer()
    }

    private fun doJournalView(){
        lifecycleScope.launch {
            delay(500)
            withContext(Dispatchers.Default) {
                showHistory()
            }

        }
    }

    private fun showDialer() {
        fab.setOnClickListener {
            block_dialer_journal.visibility = View.VISIBLE
            fab.visibility = View.GONE
        }
        val rippleEffect = RippleEffect()
        number_0.setOnClickListener {
            rippleEffect.setRippleEffect(requireContext(), it)
            number_text.setText("0")}
        number_1.setOnClickListener {
            rippleEffect.setRippleEffect(requireContext(), it)
            number_text.setText("1")}
        number_2.setOnClickListener {
            rippleEffect.setRippleEffect(requireContext(), it)
            number_text.setText("2")}
        number_3.setOnClickListener {
            rippleEffect.setRippleEffect(requireContext(), it)
            number_text.setText("3")}
        number_4.setOnClickListener {
            rippleEffect.setRippleEffect(requireContext(), it)
            number_text.setText("4")}
        number_5.setOnClickListener {
            rippleEffect.setRippleEffect(requireContext(), it)
            number_text.setText("5")}
        number_6.setOnClickListener {
            rippleEffect.setRippleEffect(requireContext(), it)
            number_text.setText("6")}
        number_7.setOnClickListener {
            rippleEffect.setRippleEffect(requireContext(), it)
            number_text.setText("7")}
        number_8.setOnClickListener {
            rippleEffect.setRippleEffect(requireContext(), it)
            number_text.setText("8")}
        number_9.setOnClickListener {
            rippleEffect.setRippleEffect(requireContext(), it)
            number_text.setText("9")}
        number_star.setOnClickListener {
            rippleEffect.setRippleEffect(requireContext(), it)
            number_text.setText("*")}
        number_resh.setOnClickListener {
            rippleEffect.setRippleEffect(requireContext(), it)
            number_text.setText("#")}
    }

    private fun hideDialer() {
        hide_dialer_btn.setOnClickListener {
            block_dialer_journal.visibility = View.GONE
            fab.visibility = View.VISIBLE
        }
    }

    fun selectedItems() {
        frame_for_delete?.visibility = View.VISIBLE
        quantity_delete_txt.text = quantityItemsChecked
    }

    private fun checkAllBtn() {
        checkAll.setOnClickListener {
            allChecked = true
            adapter?.selectAll(allChecked)
            checkAll.visibility = View.GONE
            unCheckAll.visibility = View.VISIBLE
            frame_for_delete.visibility = View.VISIBLE
            quantity_delete_txt.text = adapter?.itemCount.toString()
            Log.e(TAG, "Button check all clicked")
        }
    }
    private fun unCheckAllBtn(){
        unCheckAll.setOnClickListener {
            allChecked = false
            adapter?.selectAll(allChecked)
            checkAll.visibility = View.VISIBLE
            unCheckAll.visibility = View.GONE
            frame_for_delete.visibility = View.GONE
            Log.e(TAG, "Button uncheck all clicked")
        }
    }

    private fun closeSearch(){
        search.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                allCalls.visibility = View.VISIBLE
                checkAll.visibility = View.VISIBLE
                return false
            }

        })
    }

    private fun searchClicked(){

        var searchIsClicked = false
        searchContact = SearchContact()

        search.setOnSearchClickListener {
            allCalls.visibility = View.GONE
            checkAll.visibility = View.GONE
            searchIsClicked = true
            searchContact!!.searchContact(requireContext(), RV, search)
        }

    }

    private fun sortCalls() {

        // All calls filter
        linear1.setOnClickListener {

            all_calls_txt.setTextColor(resources.getColor(R.color.toolbar_background))
            radio_all.setImageDrawable(resources.getDrawable(R.drawable.galka_drop_down))

            filtrCardDropDown.visibility = View.GONE
            allCalls.text = "Все вызовы"

//            showHistory()
//            getCallsLogs()

            filterIncoming = false
            filterOutgoing = false
            filterMissing = false
            filterAllCalls = true
//            adapter.selectAllCalls(filterAllCalls)

            incoming_calls_txt.setTextColor(resources.getColor(R.color.black))
            radio_incoming.setImageDrawable(resources.getDrawable(R.drawable.circle_drop_down))

            missed_calls_txt.setTextColor(resources.getColor(R.color.black))
            radio_missed.setImageDrawable(resources.getDrawable(R.drawable.circle_drop_down))

            outcoming_calls_txt.setTextColor(resources.getColor(R.color.black))
            radio_outcoming.setImageDrawable(resources.getDrawable(R.drawable.circle_drop_down))

        }

        // Incoming filter
        linear2.setOnClickListener {

            progressBar.visibility = View.VISIBLE

            filtrCardDropDown.visibility = View.GONE
            allCalls.text = "Входящие"

            Log.e(TAG, "Sorting incoming clicked")
            incomingCallsClicked = true
            incoming_calls_txt.setTextColor(resources.getColor(R.color.toolbar_background))
            radio_incoming.setImageDrawable(resources.getDrawable(R.drawable.galka_drop_down))

            filterIncoming = true
            filterOutgoing = false
            filterMissing = false
            filterAllCalls = false
            adapter?.selectIncoming(filterIncoming)

            all_calls_txt.setTextColor(resources.getColor(R.color.black))
            radio_all.setImageDrawable(resources.getDrawable(R.drawable.circle_drop_down))

            missed_calls_txt.setTextColor(resources.getColor(R.color.black))
            radio_missed.setImageDrawable(resources.getDrawable(R.drawable.circle_drop_down))

            outcoming_calls_txt.setTextColor(resources.getColor(R.color.black))
            radio_outcoming.setImageDrawable(resources.getDrawable(R.drawable.circle_drop_down))

        }

        // Outgoing filter
        linear3.setOnClickListener {

            outgoingCallsClicked = true
            outcoming_calls_txt.setTextColor(resources.getColor(R.color.toolbar_background))
            radio_outcoming.setImageDrawable(resources.getDrawable(R.drawable.galka_drop_down))

            filtrCardDropDown.visibility = View.GONE
            allCalls.text = "Исходящие"

//            showHistory()
//            getCallsLogs()

            filterIncoming = false
            filterOutgoing = true
            filterMissing = false
            filterAllCalls = false
            adapter?.selectOutgoing(filterOutgoing)
//            adapter.notifyDataSetChanged()

            incoming_calls_txt.setTextColor(resources.getColor(R.color.black))
            radio_incoming.setImageDrawable(resources.getDrawable(R.drawable.circle_drop_down))

            all_calls_txt.setTextColor(resources.getColor(R.color.black))
            radio_all.setImageDrawable(resources.getDrawable(R.drawable.circle_drop_down))

            missed_calls_txt.setTextColor(resources.getColor(R.color.black))
            radio_missed.setImageDrawable(resources.getDrawable(R.drawable.circle_drop_down))
        }

        // Missing filter
        linear4.setOnClickListener {
            Log.e(TAG, "Sorting missing clicked")
            missingCallsClicked = true
            missed_calls_txt.setTextColor(resources.getColor(R.color.toolbar_background))
            radio_missed.setImageDrawable(resources.getDrawable(R.drawable.galka_drop_down))

            filtrCardDropDown.visibility = View.GONE
            allCalls.text = "Пропущенные"
            filterIncoming = false
            filterOutgoing = false
            filterMissing = true
            filterAllCalls = false
            adapter?.selectMissing(filterMissing)

            all_calls_txt.setTextColor(resources.getColor(R.color.black))
            radio_all.setImageDrawable(resources.getDrawable(R.drawable.circle_drop_down))

            incoming_calls_txt.setTextColor(resources.getColor(R.color.black))
            radio_incoming.setImageDrawable(resources.getDrawable(R.drawable.circle_drop_down))

            outcoming_calls_txt.setTextColor(resources.getColor(R.color.black))
            radio_outcoming.setImageDrawable(resources.getDrawable(R.drawable.circle_drop_down))
        }
    }

    private fun setinitialCols() {
        all_calls_txt.setTextColor(resources.getColor(R.color.toolbar_background))
        radio_all.setImageDrawable(resources.getDrawable(R.drawable.galka_drop_down))
    }

    private fun clickFiltrBtn() {
        allCalls.setOnClickListener {
            if (filterClicked == false)
            {
                filtrCardDropDown.visibility = View.VISIBLE
                filterClicked = true
            }
            else {
                filtrCardDropDown.visibility = View.GONE
                filterClicked = false
            }
        }
    }

    private suspend fun showHistory() {
        adapter = JournalAdapter(getHistory(), this)
//        adapter?.setCallListener(this)
//        RV.adapter = adapter
//        adapter.notifyDataSetChanged()
    }

    /**
     * Method to form RV and adapter
     */
    private suspend fun getHistory(): ArrayList<JournalModels>{
        val callHistory = ArrayList<JournalModels>()
        val cursor = context?.contentResolver?.query(CallLog.Calls.CONTENT_URI,null, null, null, CallLog.Calls.DATE + " DESC")

        if (cursor!!.count > 0) {
            while (cursor.moveToNext()) {
                val number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))
                var name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME))
                val date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE))
                val callType = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE))

                val callTypeInt = Integer.parseInt(callType)
                var calling_type: String? = null

                findAudio = FindAudio()
                val audioRecord = findAudio?.findRecord(date, requireContext)

                val dateStr = convertDate(date)
                val timeStr = convertTime(date)

                when (callTypeInt) {
                    CallLog.Calls.OUTGOING_TYPE -> {
                        calling_type = Constants.OUTGOING_CALL
                        Log.e(TAG, "Call Type OUT: $callType")
                    }
                    CallLog.Calls.INCOMING_TYPE -> {
                        calling_type = Constants.INCOMING_CALL
                        Log.e(TAG, "Call Type IN: $callType")

                    }
                    CallLog.Calls.MISSED_TYPE -> {
                        calling_type = Constants.MISSING_CALL
                        Log.e(TAG, "Call Type MISS: $callType")
                    }
                }

                if (name == null) { name = number }

                /**
                 * Метод выдачи СЕГОДНЯ/ВЧЕРА
                 */
                var day = ""
                val showDay = CompareTodayYesterday()
                if (showDay.isToday(date)) { day = "Сегодня" }
                else if (showDay.isYesterday(date)) { day = "Вчера" } else { day = convertDate(date) }

//                callHistory.add(JournalModels(name , convertTime(date), number, day, audioRecord, calling_type))

                callsList.add(JournalModels(name , timeStr, number, day, audioRecord, calling_type))

//                if (!RV.isComputingLayout && RV.scrollState == SCROLL_STATE_IDLE) {
//                    adapter.notifyDataSetChanged()
//                }

                withContext(Dispatchers.Main) {
                    if (!RV.isComputingLayout && RV.scrollState == SCROLL_STATE_IDLE) {
                        adapter?.notifyDataSetChanged()
                    }
                }
            }

        } else {
            Toast.makeText(requireContext(), "Нет звонков", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
        return callHistory
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

    override fun onCall(journal: JournalModels) {
        Utility.makeCall(requireContext(), journal.phoneNum)
    }

//    override fun onResume() {
//        super.onResume()
//        doJournalView()
//    }

    override fun onPause() {
        super.onPause()
        waveform?.player = MediaPlayer()
        waveform?.playerPause()
    }

    override fun selectItem() {
        frame_for_delete?.visibility = View.VISIBLE
        quantity_delete_txt.text = quantityItemsChecked
    }

    override fun unselectItem() {
        frame_for_delete?.visibility = View.GONE
        quantity_delete_txt.text = quantityItemsChecked
    }
}


