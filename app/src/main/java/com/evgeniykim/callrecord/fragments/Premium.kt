package com.evgeniykim.callrecord.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.evgeniykim.callrecord.databinding.FragmentPremiumBinding

class Premium: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPremiumBinding.inflate(inflater, container, false)

        return binding.root
    }
}