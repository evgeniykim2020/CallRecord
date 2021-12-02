package com.evgeniykim.callrecord.recording

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import android.view.Gravity
import android.widget.Toast

class CallReceiver: BroadcastReceiver() {

    val LOG_TAG = "CallReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            showToast(context!!, "Call started")
            val intent = Intent(context, MyService::class.java)
            intent.putExtra("isOnline", "isonline")
            context.startService(intent)

        }
        else if (intent?.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            showToast(context!!, "Call ended")
            val intent = Intent(context, MyService::class.java)
            intent.putExtra("isOffline", "isoffline")
            context.startService(intent)
        }
        else if (intent?.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            showToast(context!!, "Incoming call")

        }
    }

    fun showToast(context: Context, message: String) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }




}