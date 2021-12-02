package com.evgeniykim.callrecord

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setupBackground()

    }

    private fun setupBackground(){
        val gradientDraw = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(Color.parseColor("#243AFE"),
            Color.parseColor("#1D2CB8"))
        )
        gradientDraw.cornerRadius = 0f
        relativeLayoutSplash.background = gradientDraw

    }
}