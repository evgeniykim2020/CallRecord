package com.evgeniykim.callrecord.utils

import android.content.Context
import android.util.TypedValue
import android.view.View

class RippleEffect {
    fun setRippleEffect(context: Context, view: View) {
        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        view.setBackgroundResource(outValue.resourceId)
    }
}