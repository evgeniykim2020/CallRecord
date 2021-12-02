package com.evgeniykim.callrecord.listeners

interface OnCallListener<T> {

    fun onCall(t: T)
}