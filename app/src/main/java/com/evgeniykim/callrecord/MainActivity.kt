package com.evgeniykim.callrecord

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.evgeniykim.callrecord.fragments.*
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val PERMISSIONS_REQUEST = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            checkPermission()
        }
    }

    fun setFragment(fragment: Fragment){
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val frag = supportFragmentManager.beginTransaction()
                frag.replace(R.id.frag_container, fragment)
                frag.commit()
            }
        }

    }

    private fun checkPermission() {
        val permission1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
        val permission2 = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
        val permission3 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
        val permission4 = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        val permission5 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
        val permission6 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val permission7 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permission8 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_MEDIA_LOCATION)

        if (
                permission1 != PackageManager.PERMISSION_GRANTED &&
                permission2 != PackageManager.PERMISSION_GRANTED &&
                permission3 != PackageManager.PERMISSION_GRANTED &&
                permission4 != PackageManager.PERMISSION_GRANTED &&
                permission5 != PackageManager.PERMISSION_GRANTED &&
                permission6 != PackageManager.PERMISSION_GRANTED &&
                permission7 != PackageManager.PERMISSION_GRANTED &&
                permission8 != PackageManager.PERMISSION_GRANTED
                ) {
            requestPermission()
        } else {
            setContentView(R.layout.activity_main)
            setFragment(Journal())
            setButtonsChars(journal_btn, contacts_btn, blocking_btn, settings_btn, premium_btn)
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this,
                arrayOf(
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_MEDIA_LOCATION
                ),
                PERMISSIONS_REQUEST)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSIONS_REQUEST -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    requestPermission()
                } else {
                    goToAccessibilityService()
                    setContentView(R.layout.activity_main)
                    setFragment(Journal())
                    setButtonsChars(journal_btn, contacts_btn, blocking_btn, settings_btn, premium_btn)
                }
            }
        }
    }

    private fun goToAccessibilityService(){
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val intent = Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.journal_btn ->
            {
                setFragment(Journal())
                setButtonsChars(journal_btn, contacts_btn, blocking_btn, settings_btn, premium_btn)
            }
            R.id.contacts_btn ->
            {
                setFragment(Contacts())
                Log.e("Contacts button", "Button Clicked")
                setButtonsChars(contacts_btn, journal_btn, blocking_btn, settings_btn, premium_btn)
            }
            R.id.blocking_btn ->
            {
                setFragment(Blocking())
                setButtonsChars(blocking_btn, journal_btn, contacts_btn, settings_btn, premium_btn)
            }
            R.id.settings_btn ->
            {
                setFragment(Settings())
                setButtonsChars(settings_btn, journal_btn, contacts_btn, blocking_btn, premium_btn)
            }
            R.id.premium_btn ->
            {
                setFragment(Premium())
                setButtonsChars(premium_btn, journal_btn, contacts_btn, blocking_btn, settings_btn)
            }

        }
    }

    private fun setButtonsChars(button: MaterialButton, button2: MaterialButton, button3: MaterialButton, button4: MaterialButton, button5: MaterialButton) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                button.background.setTint(resources.getColor(R.color.grey))
                button.setTextColor(resources.getColor(R.color.white))

                button2.background.setTint(resources.getColor(R.color.upper_buttons))
                button2.setTextColor(resources.getColor(R.color.black))

                button3.background.setTint(resources.getColor(R.color.upper_buttons))
                button3.setTextColor(resources.getColor(R.color.black))

                button4.background.setTint(resources.getColor(R.color.upper_buttons))
                button4.setTextColor(resources.getColor(R.color.black))

                button5.background.setTint(resources.getColor(R.color.upper_buttons))
                button5.setTextColor(resources.getColor(R.color.black))
            }
        }
    }


}