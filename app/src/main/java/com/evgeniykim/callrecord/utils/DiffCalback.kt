package com.evgeniykim.callrecord.utils

import androidx.recyclerview.widget.DiffUtil
import com.evgeniykim.callrecord.models.JournalModels

class DiffCallback : DiffUtil.ItemCallback<JournalModels>() {
    override fun areItemsTheSame(oldItem: JournalModels, newItem: JournalModels): Boolean {
        return oldItem.phoneNum == newItem.phoneNum
    }

    override fun areContentsTheSame(oldItem: JournalModels, newItem: JournalModels): Boolean {
        return oldItem.phoneNum == newItem.phoneNum
    }
}