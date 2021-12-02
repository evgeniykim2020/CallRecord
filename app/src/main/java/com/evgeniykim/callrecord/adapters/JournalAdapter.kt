package com.evgeniykim.callrecord.adapters

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.provider.BlockedNumberContract
import android.telecom.TelecomManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.evgeniykim.callrecord.R
import com.evgeniykim.callrecord.activities.CallerDetailsActivity
import com.evgeniykim.callrecord.databinding.JournalItemsRecordedBinding
import com.evgeniykim.callrecord.fragments.Journal
import com.evgeniykim.callrecord.listeners.OnCallListener
import com.evgeniykim.callrecord.models.JournalModels
import com.evgeniykim.callrecord.utils.Animations
import com.evgeniykim.callrecord.utils.Constants.Companion.INCOMING_CALL
import com.evgeniykim.callrecord.utils.Constants.Companion.MISSING_CALL
import com.evgeniykim.callrecord.utils.Constants.Companion.OUTGOING_CALL
import com.evgeniykim.callrecord.utils.RippleEffect
import com.evgeniykim.callrecord.utils.Waveform

class JournalAdapter(var journalList: ArrayList<JournalModels>, val adapterListener: AdapterListener): RecyclerView.Adapter<JournalAdapter.MyViewHolder>() {

    val context: Context? = null
    val player: Waveform = Waveform()
    var isPlaying: Boolean? = null
    private val TAG = "JournalAdapter"

    interface FilterCallingType{
        fun sortCalls(callingType: String, )
    }

    private var onCallListener: OnCallListener<JournalModels>? = null
    fun setCallListener(onCallListener: OnCallListener<JournalModels>){
        this.onCallListener = onCallListener
    }

    private var allSelected = false
    fun selectAll(selected: Boolean) {
        if (selected) {
            allSelected = true
            notifyDataSetChanged()
        } else {
            allSelected = false
            notifyDataSetChanged()
        }
    }

    /**
     * FILTERING CALL TYPES
     */
    private var filterIncoming = false
    private var filterOutgoing = false
    private var filterMissing = false
    private var filterAllCalls = false
    fun selectIncoming(incomingOnly: Boolean) {
        if (incomingOnly) {
            filterIncoming = true
            filterOutgoing = false
            filterMissing = false
            filterAllCalls = false
            notifyDataSetChanged()
        }
    }

    fun selectOutgoing(outgoingOnly: Boolean) {
        if (outgoingOnly) {
            filterIncoming = false
            filterOutgoing = true
            filterMissing = false
            filterAllCalls = false
            notifyDataSetChanged()
        }
    }

    fun selectMissing(missingOnly: Boolean) {
        if (missingOnly) {
            filterIncoming = false
            filterOutgoing = false
            filterMissing = true
            filterAllCalls = false
            notifyDataSetChanged()
        }
    }
    fun selectAllCalls(allCallsOnly: Boolean) {
        if (allCallsOnly) {
            filterIncoming = false
            filterOutgoing = false
            filterMissing = false
            filterAllCalls = true
            notifyDataSetChanged()
        }
    }



    var isLongClicked = false
    private var journalFragment: Journal? = null

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = JournalItemsRecordedBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val journal = journalList[position]

        holder.binding.nameJournal.text = journal.name
        holder.binding.callTime.text = journal.time
        holder.binding.dayToDay.text = journal.date
        holder.binding.phoneJournal.setOnClickListener{
            if (onCallListener != null) {
                onCallListener!!.onCall(journalList[position])
            }
        }
//        notifyDataSetChanged()
        val audioFile = journal.recordedFile

        /**
         * hiding play button if there is no audio file for call
         */
        if (audioFile != null) {
            holder.binding.playRecordJournal.visibility = View.VISIBLE
        }

        /**
         * opens row of buttons for file manipulation
         */
        holder.binding.playRecordJournal.setOnClickListener {
            holder.binding.cardRecordedJournal.visibility = View.VISIBLE
            holder.binding.playRecordJournal.visibility = View.INVISIBLE
            holder.binding.cardRecordedJournal.visibility = View.VISIBLE
            val animation = Animations()
            animation.expand(holder.binding.cardRecordedJournal)
            holder.binding.playerView.visibility = View.VISIBLE
            holder.binding.playRecordJournal.visibility = View.INVISIBLE
//            val audioFile: AssetFileDescriptor = journal.audioFile
//            player.waveformStart(audioFile, holder.binding.waveFormView)
            if (audioFile != null) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    player.waveformStart(audioFile, holder.binding.waveFormView)
                }
                isPlaying = true
            }
            Log.i("JournalAdapter", "The file is $audioFile")
        }

        // hide all views to previous state
        holder.binding.cardGeneralJournal.setOnClickListener {
            holder.binding.cardRecordedJournal.visibility = View.GONE
            holder.binding.playViewBtnsJournal.visibility = View.GONE
            holder.binding.playerView.visibility = View.GONE
            if (audioFile != null) {
                holder.binding.playRecordJournal.visibility = View.VISIBLE
            }
        }
        holder.binding.btnPause.setOnClickListener {
            player.playerPause()
        }

        holder.binding.btn5Plus.setOnClickListener {
            player.playerSeekForward()
        }

        holder.binding.btn5Minus.setOnClickListener {
            player.playerSeekBackward()
        }

        // Filter
        // Missing call
        if (journal.callType == MISSING_CALL) {
            holder.binding.missedRectangle.visibility = View.VISIBLE
            holder.binding.missedCallTxt.visibility = View.VISIBLE
            holder.binding.callType.setTextColor(Color.parseColor("#FF0033"))
            holder.binding.callTime.setTextColor(Color.parseColor("#FF0033"))
            holder.binding.arrowCall.drawable.setTint(Color.RED)


        } else {
            holder.binding.missedRectangle.visibility = View.INVISIBLE
            holder.binding.missedCallTxt.visibility = View.INVISIBLE
        }

        // Incoming call
        if (journal.callType == INCOMING_CALL) {
            holder.binding.arrowCall.setImageResource(R.drawable.arrow_down_white)
            holder.binding.arrowCall.drawable.setTint(Color.parseColor("#46828A"))
            holder.binding.callType.setTextColor(Color.parseColor("#46828A"))
            holder.binding.callTime.setTextColor(Color.parseColor("#46828A"))
        }

        // Outgoing call
        if (journal.callType == OUTGOING_CALL) {
            holder.binding.arrowCall.setImageResource(R.drawable.arrow_up_white)
            holder.binding.arrowCall.drawable.setTint(Color.BLUE)
            holder.binding.callType.setTextColor(Color.BLUE)
            holder.binding.callTime.setTextColor(Color.BLUE)
        }

        // Dates
        if (position > 0 && journalList.get(position).date == journalList.get(position - 1).date) {
            holder.binding.dayToDay.visibility = View.GONE
        } else {
            holder.binding.dayToDay.visibility = View.VISIBLE
        }

        // Check All
        if (allSelected) {
            holder.binding.checkAllImg.visibility = View.VISIBLE
            holder.binding.iconJournal.visibility = View.INVISIBLE
        } else {
            holder.binding.checkAllImg.visibility = View.GONE
            holder.binding.iconJournal.visibility = View.VISIBLE
        }
        /**
         * Select item by long click
         */
        holder.binding.cardGeneralJournal.setOnLongClickListener {
            adapterListener.selectItem()
            holder.binding.checkAllImg.visibility = View.VISIBLE
            holder.binding.iconJournal.visibility = View.INVISIBLE
            Log.e(TAG, "Item long click working")
            isLongClicked = true
            true
        }
        // UNSELECT item
        holder.binding.checkAllImg.setOnClickListener {
            adapterListener.unselectItem()
            holder.binding.checkAllImg.visibility = View.GONE
            holder.binding.iconJournal.visibility = View.VISIBLE
            Log.e(TAG, "Check img clicked")
//            isLongClicked = false
        }

        // More info btn
        holder.binding.moreInfoBtnJournal.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View?) {
                val rippleEffect = RippleEffect()
                rippleEffect.setRippleEffect(view!!.context, holder.binding.moreInfoBtnJournal)
                val intent = Intent(view.context, CallerDetailsActivity::class.java)
                intent.putExtra("number", journal.phoneNum)
                intent.putExtra("name", journal.name)
                view.context.startActivity(intent)
            }

        })

        // Send SMS
        holder.binding.sendSmsBtnJournal.setOnClickListener(
            object : View.OnClickListener{
                override fun onClick(view: View?) {
                    val rippleEffect = RippleEffect()
                    rippleEffect.setRippleEffect(view!!.context, holder.binding.sendSmsBtnJournal)
                    Log.e(TAG, "SMS button clicked")
                    val messageIntent = Intent(Intent.ACTION_VIEW)
                    messageIntent.data = Uri.parse("sms: " + journal.phoneNum)
                    view.context.startActivity(messageIntent)
                }
            }
        )

        // FILTER CALL TYPES
        if (filterIncoming) {
            when (journal.callType) {
                INCOMING_CALL -> { holder.binding.cardGeneralJournal.visibility = View.VISIBLE }
                OUTGOING_CALL -> { holder.binding.cardGeneralJournal.visibility = View.GONE }
                MISSING_CALL -> { holder.binding.cardGeneralJournal.visibility = View.GONE }
            }
        }
        if (filterOutgoing) {
            when (journal.callType) {
                INCOMING_CALL -> { holder.binding.cardGeneralJournal.visibility = View.GONE }
                OUTGOING_CALL -> { holder.binding.cardGeneralJournal.visibility = View.VISIBLE }
                MISSING_CALL -> { holder.binding.cardGeneralJournal.visibility = View.GONE }
            }
        }
        if (filterMissing) {
            when (journal.callType) {
                INCOMING_CALL -> { holder.binding.cardGeneralJournal.visibility = View.GONE }
                OUTGOING_CALL -> { holder.binding.cardGeneralJournal.visibility = View.GONE }
                MISSING_CALL -> { holder.binding.cardGeneralJournal.visibility = View.VISIBLE }
            }
        }
        if (filterAllCalls) {
            when (journal.callType) {
                INCOMING_CALL -> { holder.binding.cardGeneralJournal.visibility = View.VISIBLE }
                OUTGOING_CALL -> { holder.binding.cardGeneralJournal.visibility = View.VISIBLE }
                MISSING_CALL -> { holder.binding.cardGeneralJournal.visibility = View.VISIBLE }
            }
        }

        /**
         * Make a phone number block
         */
        holder.binding.blockNumberBtn.setOnClickListener {
            val telecomManager: TelecomManager? = null
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                context?.startActivity(telecomManager?.createManageBlockedNumbersIntent(), null)
                if (journal.phoneNum.isNotEmpty()) {
                    val values = ContentValues()
                    values.put(BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER, journal.phoneNum)
                    val uri = context?.contentResolver?.insert(BlockedNumberContract.BlockedNumbers.CONTENT_URI, values)
                }
            }

        }

    }

    override fun getItemCount() = journalList.size

    inner class MyViewHolder(val binding: JournalItemsRecordedBinding): RecyclerView.ViewHolder(binding.root)
}

interface AdapterListener{
    fun selectItem()
    fun unselectItem()
}