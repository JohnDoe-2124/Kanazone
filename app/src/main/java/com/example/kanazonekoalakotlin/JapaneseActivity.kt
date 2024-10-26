package com.example.kanazonekoalakotlin

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kanazonekoalakotlin.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView

class JapaneseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_japanese)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up the toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Set up back button functionality
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        // References to the MaterialCardView and MaterialTextView for each grid item
        val topLeftCard = findViewById<MaterialCardView>(R.id.topLeftCard)
        val topRightCard = findViewById<MaterialCardView>(R.id.topRightCard)

        // Set click listeners for top left and top right cards
        topLeftCard.setOnClickListener {
            startActivity(Intent(this, KanaActivity::class.java).apply {
                putExtra("KANA_TYPE", "hiragana")
            })
        }

        topRightCard.setOnClickListener {
            startActivity(Intent(this, KanaActivity::class.java).apply {
                putExtra("KANA_TYPE", "katakana")
            })
        }


        // References to the MaterialTextView for each grid item
        val topLeftTextView = findViewById<MaterialTextView>(R.id.topLeftTextView)
        val topRightTextView = findViewById<MaterialTextView>(R.id.topRightTextView)
        val bottomLeftTextView = findViewById<MaterialTextView>(R.id.bottomLeftTextView)
        val bottomRightTextView = findViewById<MaterialTextView>(R.id.bottomRightTextView)

        // Set text for each TextView
        topLeftTextView.text = "Hiragana"
        topRightTextView.text = "Katakana"
        bottomLeftTextView.text = "Typing"
        bottomRightTextView.text = "Writing"
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
