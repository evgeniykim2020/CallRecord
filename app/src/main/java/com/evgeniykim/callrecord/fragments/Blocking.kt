package com.evgeniykim.callrecord.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.evgeniykim.callrecord.R
import com.evgeniykim.callrecord.databinding.FragmentBlockingBinding
import kotlinx.android.synthetic.main.fragment_blocking.*

class Blocking: Fragment() {

    private var callsBlocked = false
    private var settingsOff = false
    private var disturbOff = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentBlockingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        blockCalls()
        switchSettings()
        dontDisturb()
    }

    private fun dontDisturb() {
        switch_btn_calm_hours.setOnClickListener {
            if (!disturbOff) {
                switch_btn_calm_hours.setImageResource(R.drawable.btn_diactivate_blocking)
                switch_calm_hours_text_blocking.setTextColor(resources.getColor(R.color.grey))
                switch_calm_hours_text_blocking.text = "Деактивировано"
                disturbOff = true
            } else {
                switch_btn_calm_hours.setImageResource(R.drawable.activate_button_blocking)
                switch_calm_hours_text_blocking.setTextColor(resources.getColor(R.color.black))
                switch_calm_hours_text_blocking.text = "Активировано"
                disturbOff = false
            }
        }
    }

    private fun switchSettings() {
        btn_turn_gen_set_blocking.setOnClickListener {
            if (!settingsOff) {
                btn_turn_gen_set_blocking.setImageResource(R.drawable.btn_diactivate_blocking)
                general_settings_turn_on_text_blocking.setTextColor(resources.getColor(R.color.grey))
                general_settings_turn_on_text_blocking.text = "Выключено"
                settingsOff = true
            } else {
                btn_turn_gen_set_blocking.setImageResource(R.drawable.activate_button_blocking)
                general_settings_turn_on_text_blocking.setTextColor(resources.getColor(R.color.black))
                general_settings_turn_on_text_blocking.text = "Включено"
                settingsOff = false
            }
        }

    }

    private fun blockCalls() {

        activate_calls_block_blocking_btn.setOnClickListener {
            if (!callsBlocked) {
                activate_calls_block_blocking_btn.setImageResource(R.drawable.btn_diactivate_blocking)
                callsBlocked = true
            } else {
                activate_calls_block_blocking_btn.setImageResource(R.drawable.activate_button_blocking)
                callsBlocked = false
            }
        }
    }
}