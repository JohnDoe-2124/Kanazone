package com.example.kanazonekoalakotlin

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.kanazonekoalakotlin.StrokeManager.clear
import com.example.kanazonekoalakotlin.StrokeManager.download
import com.example.kanazonekoalakotlin.StrokeManager.recognize
import com.example.kanazonekoalakotlin.databinding.ActivityChosenSetBinding

class ChosenSetActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var gifImageView: ImageView
    private lateinit var dataBinding: ActivityChosenSetBinding
    private var isDrawing = false
    private var hasDrawingChanged = false
    private var currentCharIndex = 0
    private var currentSetIndex = 0 // Track which set we're on
    private lateinit var kanaType: String
    private val handler = Handler(Looper.getMainLooper())

    // Hiragana and Katakana sets
    private val hiraganaSets = listOf(
        "あいうえお", // Set 1
        "かきくけこ", // Set 2
        "さしすせそ", // Set 3
        "たちつてと", // Set 4
        "なにぬねの", // Set 5
        "はひふへほ", // Set 6
        "まみむめも", // Set 7
        "やゆよ",     // Set 8
        "らりるれろ", // Set 9
        "わをん"      // Set 10
    )
    private val katakanaSets = listOf(
        "アイウエオ", // Set 1
        "カキクケコ", // Set 2
        "サシスセソ", // Set 3
        "タチツテト", // Set 4
        "ナニヌネノ", // Set 5
        "ハヒフヘホ", // Set 6
        "マミムメモ", // Set 7
        "ヤユヨ",     // Set 8
        "ラリルレロ", // Set 9
        "ワヲン"      // Set 10
    )
    private val hiraganaMap = mapOf(
        'あ' to Pair(R.raw.a_audio, R.raw.set1_a),
        'い' to Pair(R.raw.set1_i_audio, R.raw.set1_i),
        'う' to Pair(R.raw.set1_u_audio, R.raw.set1_u),
        'え' to Pair(R.raw.set1_e_audio, R.raw.set1_e),
        'お' to Pair(R.raw.set1_o_audio, R.raw.set1_o),

        'か' to Pair(R.raw.set2_ka_audio, R.raw.set2_ka),
        'き' to Pair(R.raw.set2_ki_audio, R.raw.set2_ki),
        'く' to Pair(R.raw.set2_ku_audio, R.raw.set2_ku),
        'け' to Pair(R.raw.set2_ke_audio, R.raw.set2_ke),
        'こ' to Pair(R.raw.set2_ko_audio, R.raw.set2_ko),

        'さ' to Pair(R.raw.set3_sa_audio, R.raw.set3_sa),
        'し' to Pair(R.raw.set3_shi_audio, R.raw.set3_shi),
        'す' to Pair(R.raw.set3_su_audio, R.raw.set3_su),
        'せ' to Pair(R.raw.set3_se_audio, R.raw.set3_se),
        'そ' to Pair(R.raw.set3_so_audio, R.raw.set3_so),

        'た' to Pair(R.raw.set4_ta_audio, R.raw.set4_ta),
        'ち' to Pair(R.raw.set4_chi_audio, R.raw.set4_chi),
        'つ' to Pair(R.raw.set4_tsu_audio, R.raw.set4_tsu),
        'て' to Pair(R.raw.set4_te_audio, R.raw.set4_te),
        'と' to Pair(R.raw.set4_to_audio, R.raw.set4_to),

        'な' to Pair(R.raw.set5_na_audio, R.raw.set5_na),
        'に' to Pair(R.raw.set5_ni_audio, R.raw.set5_ni),
        'ぬ' to Pair(R.raw.set5_nu_audio, R.raw.set5_nu),
        'ね' to Pair(R.raw.set5_ne_audio, R.raw.set5_ne),
        'の' to Pair(R.raw.set5_no_audio, R.raw.set5_no),

        'は' to Pair(R.raw.set6_ha_audio, R.raw.set6_ha),
        'ひ' to Pair(R.raw.set6_hi_audio, R.raw.set6_hi),
        'ふ' to Pair(R.raw.set6_fu_audio, R.raw.set6_fu),
        'へ' to Pair(R.raw.set6_he_audio, R.raw.set6_he),
        'ほ' to Pair(R.raw.set6_ho_audio, R.raw.set6_ho),

        'ま' to Pair(R.raw.set7_ma_audio, R.raw.set7_ma),
        'み' to Pair(R.raw.set7_mi_audio, R.raw.set7_mi),
        'む' to Pair(R.raw.set7_mu_audio, R.raw.set7_mu),
        'め' to Pair(R.raw.set7_me_audio, R.raw.set7_me),
        'も' to Pair(R.raw.set7_mo_audio, R.raw.set7_mo),

        'や' to Pair(R.raw.set8_ya_audio, R.raw.set8_ya),
        'ゆ' to Pair(R.raw.set8_yu_audio, R.raw.set8_yu),
        'よ' to Pair(R.raw.set8_yo_audio, R.raw.set8_yo),

        'ら' to Pair(R.raw.set9_ra_audio, R.raw.set9_ra),
        'り' to Pair(R.raw.set9_ri_audio, R.raw.set9_ri),
        'る' to Pair(R.raw.set9_ru_audio, R.raw.set9_ru),
        'れ' to Pair(R.raw.set9_re_audio, R.raw.set9_re),
        'ろ' to Pair(R.raw.set9_ro_audio, R.raw.set9_ro),

        'わ' to Pair(R.raw.set10_wa_audio, R.raw.set10_wa),
        'を' to Pair(R.raw.set10_wo_audio, R.raw.set10_wo),
        'ん' to Pair(R.raw.set10_n_audio, R.raw.set10_n)
    )
    private val katakanaMap = mapOf(
        'ア' to Pair(R.raw.a_audio, R.raw.set1_a_kata),
        'イ' to Pair(R.raw.set1_i_audio, R.raw.set1_i_kata),
        'ウ' to Pair(R.raw.set1_u_audio, R.raw.set1_u_kata),
        'エ' to Pair(R.raw.set1_e_audio, R.raw.set1_e_kata),
        'オ' to Pair(R.raw.set1_o_audio, R.raw.set1_o_kata),

        'カ' to Pair(R.raw.set2_ka_audio, R.raw.set2_ka_kata),
        'キ' to Pair(R.raw.set2_ki_audio, R.raw.set2_ki_kata),
        'ク' to Pair(R.raw.set2_ku_audio, R.raw.set2_ku_kata),
        'ケ' to Pair(R.raw.set2_ke_audio, R.raw.set2_ke_kata),
        'コ' to Pair(R.raw.set2_ko_audio, R.raw.set2_ko_kata),

        'サ' to Pair(R.raw.set3_sa_audio, R.raw.set3_sa_kata),
        'シ' to Pair(R.raw.set3_shi_audio, R.raw.set3_shi_kata),
        'ス' to Pair(R.raw.set3_su_audio, R.raw.set3_su_kata),
        'セ' to Pair(R.raw.set3_se_audio, R.raw.set3_se_kata),
        'ソ' to Pair(R.raw.set3_so_audio, R.raw.set3_so_kata),

        'タ' to Pair(R.raw.set4_ta_audio, R.raw.set4_ta_kata),
        'チ' to Pair(R.raw.set4_chi_audio, R.raw.set4_chi_kata),
        'ツ' to Pair(R.raw.set4_tsu_audio, R.raw.set4_tsu_kata),
        'テ' to Pair(R.raw.set4_te_audio, R.raw.set4_te_kata),
        'ト' to Pair(R.raw.set4_to_audio, R.raw.set4_to_kata),

        'ナ' to Pair(R.raw.set5_na_audio, R.raw.set5_na_kata),
        'ニ' to Pair(R.raw.set5_ni_audio, R.raw.set5_ni_kata),
        'ヌ' to Pair(R.raw.set5_nu_audio, R.raw.set5_nu_kata),
        'ネ' to Pair(R.raw.set5_ne_audio, R.raw.set5_ne_kata),
        'ノ' to Pair(R.raw.set5_no_audio, R.raw.set5_no_kata),

        'ハ' to Pair(R.raw.set6_ha_audio, R.raw.set6_ha_kata),
        'ヒ' to Pair(R.raw.set6_hi_audio, R.raw.set6_hi_kata),
        'フ' to Pair(R.raw.set6_fu_audio, R.raw.set6_fu_kata),
        'ヘ' to Pair(R.raw.set6_he_audio, R.raw.set6_he_kata),
        'ホ' to Pair(R.raw.set6_ho_audio, R.raw.set6_ho_kata),

        'マ' to Pair(R.raw.set7_ma_audio, R.raw.set7_ma_kata),
        'ミ' to Pair(R.raw.set7_mi_audio, R.raw.set7_mi_kata),
        'ム' to Pair(R.raw.set7_mu_audio, R.raw.set7_mu_kata),
        'メ' to Pair(R.raw.set7_me_audio, R.raw.set7_me_kata),
        'モ' to Pair(R.raw.set7_mo_audio, R.raw.set7_mo_kata),

        'ヤ' to Pair(R.raw.set8_ya_audio, R.raw.set8_ya_kata),
        'ユ' to Pair(R.raw.set8_yu_audio, R.raw.set8_yu_kata),
        'ヨ' to Pair(R.raw.set8_yo_audio, R.raw.set8_yo_kata),

        'ラ' to Pair(R.raw.set9_ra_audio, R.raw.set9_ra_kata),
        'リ' to Pair(R.raw.set9_ri_audio, R.raw.set9_ri_kata),
        'ル' to Pair(R.raw.set9_ru_audio, R.raw.set9_ru_kata),
        'レ' to Pair(R.raw.set9_re_audio, R.raw.set9_re_kata),
        'ロ' to Pair(R.raw.set9_ro_audio, R.raw.set9_ro_kata),

        'ワ' to Pair(R.raw.set10_wa_audio, R.raw.set10_wa_kata),
        'ヲ' to Pair(R.raw.set10_wo_audio, R.raw.set10_wo_kata),
        'ン' to Pair(R.raw.set10_n_audio, R.raw.set10_n_kata)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_chosen_set)

        kanaType = intent.getStringExtra("KANA_TYPE") ?: "hiragana"
        currentSetIndex = intent.getIntExtra("CURRENT_SET_INDEX", 0)

        val currentSets = if (kanaType == "hiragana") hiraganaSets else katakanaSets
        val kanaMap = if (kanaType == "hiragana") hiraganaMap else katakanaMap
        val currentSetText = currentSets[currentSetIndex]
        val characters = currentSetText.toCharArray()

        setupToolbar("Set ${currentSetIndex + 1}")

        gifImageView = findViewById(R.id.gifImageView)
        if (characters.isNotEmpty()) {
            loadMediaForCharacter(characters[currentCharIndex], kanaMap)
        }

        dataBinding.nextButton.setOnClickListener {
            if (currentCharIndex < characters.size - 1) {
                currentCharIndex++
            } else {
                currentCharIndex = 0
            }
            loadMediaForCharacter(characters[currentCharIndex], kanaMap)
        }

        dataBinding.backButton.setOnClickListener {
            val intent = Intent(this, KanaActivity::class.java).apply {
                putExtra("KANA_TYPE", kanaType)
            }
            startActivity(intent)
        }

        dataBinding.practiceButton.setOnClickListener {
            currentSetIndex = (currentSetIndex + 1) % currentSets.size
            val intent = Intent(this, ChosenSetActivity::class.java).apply {
                putExtra("CURRENT_SET_INDEX", currentSetIndex)
                putExtra("KANA_TYPE", kanaType)
            }
            startActivity(intent)
        }

        hideTitleBar()
        download()

        dataBinding.buttonClear.setOnClickListener {
            dataBinding.drawView.clear()
            clear()
            dataBinding.textResult.text = "Cleared"
            gifImageView.visibility = View.VISIBLE
            mediaPlayer.start()
        }

        dataBinding.drawView.onDrawingStartListener = {
            gifImageView.visibility = View.GONE
            mediaPlayer.pause()
            isDrawing = true
            hasDrawingChanged = true
        }

        val recognizeRunnable = Runnable {
            recognize(dataBinding.textResult) { recognizedChar, score ->
                if (recognizedChar != null && characters.contains(recognizedChar)) {
                    dataBinding.textResult.text = recognizedChar.toString()
                    loadMediaForCharacter(recognizedChar, kanaMap)
                } else {
                    dataBinding.textResult.text = ""
                }
                Log.d("ChosenSetActivity", "Recognized character: $recognizedChar, Score: $score")
            }
        }

        dataBinding.drawView.onDrawingStopListener = {
            if (!isDrawing) {
                gifImageView.visibility = View.VISIBLE
                mediaPlayer.start()
            }
            if (hasDrawingChanged) {
                handler.postDelayed(recognizeRunnable, 500)
                hasDrawingChanged = false
            }
        }
    }

    private fun loadMediaForCharacter(character: Char, kanaMap: Map<Char, Pair<Int, Int>>) {
        val mediaResources = kanaMap[character]
        if (mediaResources != null) {
            val (audioRes, gifRes) = mediaResources
            if (::mediaPlayer.isInitialized) {
                mediaPlayer.release()
            }
            mediaPlayer = MediaPlayer.create(this, audioRes)
            mediaPlayer.start()
            Glide.with(this)
                .asGif()
                .load(gifRes)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(gifImageView)
        } else {
            Log.e("ChosenSetActivity", "No media found for character: $character")
        }
    }

    private fun hideTitleBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

    override fun onResume() {
        super.onResume()
        hideTitleBar()
    }

    override fun onPause() {
        super.onPause()
        hideTitleBar()
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun setupToolbar(title: String) {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = title
        toolbar.setNavigationOnClickListener {
            // Navigate back to KanaActivity
            val intent = Intent(this, KanaActivity::class.java).apply {
                putExtra("KANA_TYPE", kanaType) // Pass the kana type if needed
            }
            startActivity(intent)
            finish() // Optional: call finish() if you want to remove this activity from the stack
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Navigate back to KanaActivity
                val intent = Intent(this, KanaActivity::class.java).apply {
                    putExtra("KANA_TYPE", kanaType) // Pass the kana type if needed
                }
                startActivity(intent)
                finish() // Optional: call finish() if you want to remove this activity from the stack
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
