package com.evgeniykim.callrecord.utils

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import androidx.annotation.RequiresApi
import space.siy.waveformview.WaveFormData
import space.siy.waveformview.WaveFormView

class Waveform {

    val context: Context? = null
    lateinit var player: MediaPlayer

    @RequiresApi(Build.VERSION_CODES.N)
    fun waveformStart(audioFile: AssetFileDescriptor, waveFormView: WaveFormView) {

//        WaveFormData.Factory(audioFile.fileDescriptor, audioFile.startOffset, audioFile.length)
        WaveFormData.Factory(audioFile.fileDescriptor, audioFile.startOffset, audioFile.length)

            .build(object : WaveFormData.Factory.Callback {
                override fun onComplete(waveFormData: WaveFormData) {

                    waveFormView.data = waveFormData

                    //Initialize MediaPlayer
                    player = MediaPlayer()
//                    player.setDataSource(audioFile.fileDescriptor, audioFile.startOffset, audioFile.length)
                    player.setDataSource(audioFile.fileDescriptor, audioFile.startOffset, audioFile.length)
                    player.prepare()
                    player.start()

                    //Synchronize with MediaPlayer using WaveFormView.Callback
                    waveFormView.callback = object : WaveFormView.Callback {
                        override fun onPlayPause() {
                            if (player.isPlaying)
                                player.pause()
                            else
                                player.start()
                        }
                        override fun onSeek(pos: Long) {
                            player.seekTo(pos.toInt())
                        }
                    }

                    //You have to notify current position to the view
                    Handler().postDelayed(object : Runnable {
                        override fun run() {
                            waveFormView.position = player.currentPosition.toLong()
                            Handler().postDelayed(this, 10)
                        }
                    }, 10)


                }

                override fun onProgress(progress: Float) {
//                    progressBar.progress = (progress*10).toInt()
                }

            })
    }

    fun playerPause(){
        if (player.isPlaying) {
            player.pause()
        } else {
            player.start()
        }
    }

    fun playerStart(){
        player.start()
    }

    fun playerStop(){
        player.stop()
        player.release()
    }

    fun playerSeekForward(){
        player.seekTo(player.currentPosition + 5000)
    }

    fun playerSeekBackward(){
        player.seekTo(player.currentPosition - 5000)
    }
}