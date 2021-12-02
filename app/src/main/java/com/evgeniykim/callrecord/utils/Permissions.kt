package com.evgeniykim.callrecord.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

object Permissions {

//    fun requestPermissions(context: Context, callback: PermissionsCallback) {
//        Dexter.withActivity(context as Activity)
//            .withPermissions(listOf(Manifest.permission.READ_CALL_LOG, Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS))
//            .withListener(object : MultiplePermissionsListener {
//                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
//                    if (report?.areAllPermissionsGranted()) {
//                        callback.onPermissionRequest(true)
//                    } else {
//                        callback.onPermissionRequest(false)
//                    }
//                }
//
//                override fun onPermissionRationaleShouldBeShown(
//                    permissions: MutableList<PermissionRequest>?,
//                    token: PermissionToken?
//                ) {
//                    token?.continuePermissionRequest()
//                }
//            })
//
//    }



}