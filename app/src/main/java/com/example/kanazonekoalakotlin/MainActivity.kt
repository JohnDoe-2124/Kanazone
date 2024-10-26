package com.example.kanazonekoalakotlin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // Function to navigate to HomeActivity
    fun navigateToHomeActivity(view: android.view.View) {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish() // Finish the current activity to prevent user from coming back to splash screen
    }
}
