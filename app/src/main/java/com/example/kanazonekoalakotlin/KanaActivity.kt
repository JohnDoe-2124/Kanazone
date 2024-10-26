package com.example.kanazonekoalakotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton

class KanaActivity : AppCompatActivity() {

    // Define Hiragana and Katakana set mappings and button IDs
    private val hiraganaSetIndexMap = mapOf(
        "あいうえお" to 0, "かきくけこ" to 1, "さしすせそ" to 2, "たちつてと" to 3, "なにぬねの" to 4,
        "はひふへほ" to 5, "まみむめも" to 6, "やゆよ" to 7, "らりるれろ" to 8, "わをん" to 9
    )
    private val katakanaSetIndexMap = mapOf(
        "アイウエオ" to 0, "カキクケコ" to 1, "サシスセソ" to 2, "タチツテト" to 3, "ナニヌネノ" to 4,
        "ハヒフヘホ" to 5, "マミムメモ" to 6, "ヤユヨ" to 7, "ラリルレロ" to 8, "ワヲン" to 9
    )
    private val hiraganaButtonToSetMap = mapOf(
        R.id.button_set1 to R.string.Hiragana_Set1, R.id.button_set2 to R.string.Hiragana_Set2,
        R.id.button_set3 to R.string.Hiragana_Set3, R.id.button_set4 to R.string.Hiragana_Set4,
        R.id.button_set5 to R.string.Hiragana_Set5, R.id.button_set6 to R.string.Hiragana_Set6,
        R.id.button_set7 to R.string.Hiragana_Set7, R.id.button_set8 to R.string.Hiragana_Set8,
        R.id.button_set9 to R.string.Hiragana_Set9, R.id.button_set10 to R.string.Hiragana_Set10
    )
    private val katakanaButtonToSetMap = mapOf(
        R.id.kbutton_set1 to R.string.Katakana_Set1, R.id.kbutton_set2 to R.string.Katakana_Set2,
        R.id.kbutton_set3 to R.string.Katakana_Set3, R.id.kbutton_set4 to R.string.Katakana_Set4,
        R.id.kbutton_set5 to R.string.Katakana_Set5, R.id.kbutton_set6 to R.string.Katakana_Set6,
        R.id.kbutton_set7 to R.string.Katakana_Set7, R.id.kbutton_set8 to R.string.Katakana_Set8,
        R.id.kbutton_set9 to R.string.Katakana_Set9, R.id.kbutton_set10 to R.string.Katakana_Set10
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val kanaType = intent.getStringExtra("KANA_TYPE") ?: "hiragana"
        val (setIndexMap, buttonToSetMap, layoutResId) = if (kanaType == "hiragana") {
            Triple(hiraganaSetIndexMap, hiraganaButtonToSetMap, R.layout.activity_hiragana)
        } else {
            Triple(katakanaSetIndexMap, katakanaButtonToSetMap, R.layout.activity_katakana)
        }
        setContentView(layoutResId)
        setupToolbar(getString(R.string.app_name))

        // Set up click listeners for kana buttons
        buttonToSetMap.forEach { (buttonId, stringId) ->
            findViewById<MaterialButton>(buttonId).setOnClickListener {
                startChosenSetActivity(stringId, setIndexMap)
            }
        }
    }

    private fun setupToolbar(title: String) {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = title
        toolbar.setNavigationOnClickListener { redirectToJapaneseActivity() } // Redirect to JapaneseActivity
    }

    private fun redirectToJapaneseActivity() {
        val intent = Intent(this, JapaneseActivity::class.java)
        startActivity(intent)
        finish() // Optionally, call finish() if you don't want to keep this activity in the back stack
    }

    private fun startChosenSetActivity(setTextResId: Int, setIndexMap: Map<String, Int>) {
        val setText = getString(setTextResId)
        val intent = Intent(this, ChosenSetActivity::class.java)

        val setIndex = setIndexMap[setText] ?: 0
        // Log the values for debugging
        Log.d("KanaActivity", "Starting ChosenSetActivity with index: $setIndex for set: $setText")
        Log.d("KanaActivity", "Starting ChosenSetActivity with KANA_TYPE: ${if (setIndexMap == hiraganaSetIndexMap) "hiragana" else "katakana"}")
        intent.putExtra("CURRENT_SET_INDEX", setIndex)
        intent.putExtra("KANA_TYPE", if (setIndexMap == hiraganaSetIndexMap) "hiragana" else "katakana")
        Log.d("KanaActivity", "Button clicked for set: $setText with index: $setIndex")
        startActivity(intent)
    }
}
