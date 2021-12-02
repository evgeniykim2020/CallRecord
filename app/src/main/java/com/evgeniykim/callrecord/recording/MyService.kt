package com.evgeniykim.callrecord.recording

import android.accessibilityservice.AccessibilityService
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.evgeniykim.callrecord.MainActivity
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MyService: AccessibilityService() {

    lateinit var audioManager: AudioManager

    var recorder: MediaRecorder? = null
    private var TAG = "MyAccessibilityService"
    var online = false

    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
    }

    override fun onInterrupt() {

    }

    override fun onServiceConnected() {
        Log.i(TAG, "Service started")
        val intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        super.onStartCommand(intent, flags, startId)

        if (intent!!.hasExtra("isOnline") && !online) {
            online = true
            startRecording()
            Log.i(TAG, "We got correct extra with calling $online")
        }
        if (intent.hasExtra("isOffline") && online) {
            online = false
            stopRecording()
            Log.i(TAG, "Call stopping $online")
        }

        return START_NOT_STICKY
    }

    companion object{
        var filePath: ParcelFileDescriptor? = null
//        var fileName: String? = null
        var audioUri: Uri? = null
    }

    private fun startRecording() {

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        /**
         * Trying this one, saying it's keeping volume on 100 all the time
         */
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0)

        /* This was previously set, not working
        * audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)
        */


        val fileName = SimpleDateFormat("yyyyMMddhhmm").format(Date())
//        fileName = callId
//        var audioUri: Uri?
//        var filePath: ParcelFileDescriptor?

        recorder = MediaRecorder()
        val values = ContentValues()
        values.put(MediaStore.Audio.Media.DISPLAY_NAME, fileName)
        values.put(MediaStore.Audio.Media.DATE_ADDED, (System.currentTimeMillis()/ 1000))
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp4")
//        values.put(MediaStore.Audio.Media.RELATIVE_PATH, "Music/Recordings/")
        values.put(MediaStore.Audio.Media.RELATIVE_PATH, "")
        audioUri = contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values)
        filePath = contentResolver.openFileDescriptor(audioUri!!, "w")

        Log.i(TAG, "START REC: isonline = $online")

        // Media Recorder
        recorder!!.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION)
        recorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        recorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        recorder!!.setOutputFile(filePath?.fileDescriptor)
        recorder?.setAudioChannels(1)

        try {
            recorder!!.prepare()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        recorder!!.start()
        Log.i(TAG, "Recording started, saving at $audioUri")

//        val intent = Intent(this, Journal::class.java)
//        intent.putExtra(fileName, "recorded call")
//        startService(intent)


    }

    private fun stopRecording() {
        Log.i(TAG, "STOP REC: is online = $online")
        recorder?.stop()
        recorder?.release()
        recorder = null
        Log.i(TAG, "Recording stopped")

    }


}


