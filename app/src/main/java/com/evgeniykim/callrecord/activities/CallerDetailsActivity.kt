package com.evgeniykim.callrecord.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.evgeniykim.callrecord.R
import com.evgeniykim.callrecord.utils.CallLogsOps
import com.evgeniykim.callrecord.utils.RippleEffect
import kotlinx.android.synthetic.main.activity_caller_details.*
import java.text.SimpleDateFormat

class CallerDetailsActivity(
) : AppCompatActivity() {

    private var phoneNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caller_details)

        phoneNumber = intent.getStringExtra("number")
        RV_caller_details.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        editClick()
        buttonBack()
        initRV()
        getName()
    }

    private fun getName() {
        var name = ""
        name_caller_details.text = intent.getStringExtra("name")
        if (name_caller_details.text.isNotEmpty()) {
            name = name_caller_details.toString()
        } else {
            name = phoneNumber.toString()
        }
    }

    private fun initRV(){
        val callLogsOps = CallLogsOps()
        callLogsOps.getCallLogData(this, RV_caller_details, phoneNumber!!)
    }

    private fun editClick(){
        edit_button_caller_details.setOnClickListener {
            startActivity(Intent(this, EditContactActivity::class.java))
        }
    }

    private fun buttonBack() {
        upper_block_caller_details.setOnClickListener {
            val rippleEffect = RippleEffect()
            rippleEffect.setRippleEffect(this, upper_block_caller_details)
            finish()
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