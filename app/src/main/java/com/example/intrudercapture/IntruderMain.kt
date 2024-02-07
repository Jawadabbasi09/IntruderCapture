package com.example.intrudercapture

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.intrudercapture.databinding.IntruderSelfieBinding

class IntruderMain : AppCompatActivity() {
    private val binding: IntruderSelfieBinding by lazy {
        IntruderSelfieBinding.inflate(layoutInflater)
    }
    private lateinit var pref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        editor = pref.edit()

        // Set the initial state of the button based on the value stored in SharedPreferences
        val buttonOn = pref.getBoolean("button_on", false)
        updateButtonState(buttonOn)
        /*with(binding){
            loadNativeAd(
                frame.adContainer,
                frame.adShimmerLayout,
                frame.adFrameLayout,
                R.layout.large_native_ad,
                getString(R.string.native_enabled)
            )
        }*/
        binding.back.setOnClickListener {
            finish()
        }
        binding.description.movementMethod = ScrollingMovementMethod()
        findViewById<View>(R.id.Show_Intruders).setOnClickListener {

                val intent = Intent(this@IntruderMain, Intruders::class.java)
                startActivity(intent)


        }
        //make sure you have device admin permisssion is enabled after enabling this code work
        findViewById<ImageView>(R.id.Enable).setOnClickListener { v ->
            val buttonOn = pref.getBoolean("button_on", false)
            val newButtonOn = !buttonOn
            editor.putBoolean("button_on", newButtonOn)
            editor.apply()

            // Update the UI to reflect the new state
            updateButtonState(newButtonOn)

            if (newButtonOn) {
                startBackgroundService()
            } else {
                stopBackgroundService()
            }
        }
    }
    private fun updateButtonState(buttonOn: Boolean) {
        if (buttonOn) {
            binding.popuptext.setTextColor(ContextCompat.getColorStateList(this, R.color.white))
            binding.tap.visibility=View.INVISIBLE
            binding.popuptext.text = "Enable"
            //<ImageView>(R.id.Enable).setBackgroundResource(R.drawable.poweroff)
        } else {
            binding.tap.visibility=View.VISIBLE
            binding.popuptext.setTextColor(ContextCompat.getColorStateList(this, R.color.black))
            binding.popuptext.text = "Disabled"
            //findViewById<ImageView>(R.id.Enable).setBackgroundResource(R.drawable.poweron)
        }
    }
    private  fun stopBackgroundService() {
        if (Utility.isMyServiceRunning(this, CameraService::class.java)) {
            stopService(Intent(this, CameraService::class.java))
        }
    }

    private  fun startBackgroundService() {
        if (!Utility.isMyServiceRunning(this, CameraService::class.java)) {
            ContextCompat.startForegroundService(this, Intent(this, CameraService::class.java))
        }
    }
}