package com.drawer.platform

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.drawer.platform.onboarding.SplashActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Redirect to SplashActivity — this activity is superseded
        startActivity(Intent(this, SplashActivity::class.java))
        finish()
    }
}