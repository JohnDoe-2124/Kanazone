package com.example.kanazonekoalakotlin

import android.util.Log
import android.view.MotionEvent
import android.widget.TextView
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.DigitalInkRecognition
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier
import com.google.mlkit.vision.digitalink.DigitalInkRecognizerOptions
import com.google.mlkit.vision.digitalink.Ink

object StrokeManager {

    private var model: DigitalInkRecognitionModel? = null
    private var inkBuilder = Ink.builder()
    private var strokeBuilder: Ink.Stroke.Builder? = null
    private var isModelDownloaded = false

    fun addNewTouchEvent(event: MotionEvent) {
        val x = event.x
        val y = event.y
        val t = System.currentTimeMillis()

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                strokeBuilder = Ink.Stroke.builder()
                strokeBuilder!!.addPoint(Ink.Point.create(x, y, t))
            }
            MotionEvent.ACTION_MOVE -> strokeBuilder!!.addPoint(Ink.Point.create(x, y, t))
            MotionEvent.ACTION_UP -> {
                strokeBuilder!!.addPoint(Ink.Point.create(x, y, t))
                inkBuilder.addStroke(strokeBuilder!!.build())
                strokeBuilder = null
            }
        }
    }

    fun download() {
        val modelIdentifier: DigitalInkRecognitionModelIdentifier? = try {
            DigitalInkRecognitionModelIdentifier.fromLanguageTag("ja-JP")
        } catch (e: Exception) {
            null
        }

        if (modelIdentifier != null) {
            model = DigitalInkRecognitionModel.builder(modelIdentifier).build()
            val remoteModelManager = RemoteModelManager.getInstance()
            remoteModelManager.download(model!!, DownloadConditions.Builder().build())
                .addOnSuccessListener {
                    Log.d("StrokeManager", "Model downloaded successfully")
                    isModelDownloaded = true
                }
                .addOnFailureListener { exception ->
                    Log.e("StrokeManager", "Model download failed: ${exception.message}")
                }
        } else {
            Log.e("StrokeManager", "Model identifier is null.")
        }
    }

    fun recognize(textView: TextView, callback: (Char?, Float?) -> Unit) {
        if (!isModelDownloaded) {
            Log.e("StrokeManager", "Model not downloaded yet.")
            callback(null, null)
            return
        }

        val recognizer = DigitalInkRecognition.getClient(
            DigitalInkRecognizerOptions.builder(model!!).build()
        )
        val ink = inkBuilder.build()

        // Check if ink has strokes before proceeding
        if (ink.strokes.isEmpty()) {
            Log.e("StrokeManager", "No strokes to recognize.")
            callback(null, null)
            return
        }

        recognizer.recognize(ink)
            .addOnSuccessListener { result ->
                if (result.candidates.isNotEmpty()) {
                    // Log all candidates for debugging
                    result.candidates.forEach { candidate ->
                        Log.d("StrokeManager", "Candidate: ${candidate.text}, Score: ${candidate.score}")
                    }

                    // Get the best candidate
                    val bestCandidate = result.candidates[0]
                    val recognizedChar = filterHiragana(bestCandidate.text.firstOrNull()?.toString() ?: "").firstOrNull()
                    val score = bestCandidate.score // Fetching the score directly
                    Log.d("StrokeManager", "Recognized character: $recognizedChar, Score: $score")
                    callback(recognizedChar, score)
                } else {
                    Log.d("StrokeManager", "No candidates found.")
                    callback(null, null)
                }
            }
            .addOnFailureListener {
                Log.e("StrokeManager", "Recognition failed: ${it.message}")
                callback(null, null)
            }
    }

    private fun filterHiragana(text: String): String {
        val hiraganaRegex = Regex("[\\p{InHiragana}\\p{InKatakana}ー]*")
        return text.filter { it.toString().matches(hiraganaRegex) }
    }

    fun clear() {
        inkBuilder = Ink.builder()
    }
}

