package com.evgeniykim.callrecord.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.evgeniykim.callrecord.R
import com.evgeniykim.callrecord.utils.RippleEffect
import kotlinx.android.synthetic.main.activity_edit_contact.*

class EditContactActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_contact)

        buttonBack()
    }

    private fun buttonBack() {
        upper_block_caller_details_edit.setOnClickListener {
            val rippleEffect = RippleEffect()
            rippleEffect.setRippleEffect(this, upper_block_caller_details_edit)
            finish()
        }
    }
}