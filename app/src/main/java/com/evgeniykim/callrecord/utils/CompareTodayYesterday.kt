package com.evgeniykim.callrecord.utils

import android.text.format.DateUtils

class CompareTodayYesterday {

    fun isToday(whenInMillis: Long): Boolean {
        return DateUtils.isToday(whenInMillis)
    }

    fun isTomorrow(whenInMillis: Long): Boolean {
        return DateUtils.isToday(whenInMillis - DateUtils.DAY_IN_MILLIS)
    }

    fun isYesterday(whenInMillis: Long): Boolean {
        return DateUtils.isToday(whenInMillis + DateUtils.DAY_IN_MILLIS)
    }
}