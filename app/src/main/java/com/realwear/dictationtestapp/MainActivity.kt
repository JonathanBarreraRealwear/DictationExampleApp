package com.realwear.dictationtestapp

import android.Manifest.permission
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Main Activity of the Dictation test app.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var mSpeechRecognizer: SpeechRecognizer

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Request permissions needed for dictation.
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(permission.RECORD_AUDIO, permission.INTERNET),
            5
        )
    }

    /**
     * Method to start dictation with a new SpeechRecognizer.
     */
    fun startDictation(v: View) {
        start_button.isEnabled = false
        default_dictation_button.isEnabled = false

        val intent = createRecognizerIntent()
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        mSpeechRecognizer.setRecognitionListener(TestListener())
        mSpeechRecognizer.startListening(intent)
    }

    /**
     * Method to print the current default recognition service in the textview
     */
    fun showCurrentDefaultDictation(v: View) {
        val serviceComponent: String = Settings.Secure.getString(
            applicationContext.contentResolver,
            "voice_recognition_service"
        )
        dictation_textview.text = serviceComponent
    }

    companion object {
        private fun createRecognizerIntent(): Intent {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            return intent
        }
    }

    /**
     * Required subclass of RecognitionListener to determine how to deal with callbacks
     * from RecognitionService.
     */
    inner class TestListener : RecognitionListener {
        override fun onReadyForSpeech(p0: Bundle?) {
            Log.d("TestListener", "onReadyForSpeech called.")
        }

        override fun onBeginningOfSpeech() {
            Log.d("TestListener", "onBeginningOfSpeech called.")
        }

        override fun onRmsChanged(p0: Float) {
            Log.d("TestListener", "onRmsChanged called.")
        }

        override fun onBufferReceived(p0: ByteArray?) {
            Log.d("TestListener", "onBufferReceived called.")
        }

        override fun onEndOfSpeech() {
            start_button.isEnabled = true
            default_dictation_button.isEnabled = true

            Log.d("TestListener", "onEndOfSpeech called.")
        }

        override fun onError(p0: Int) {
            Log.e("TestListener", "Error: $p0")
        }

        override fun onResults(p0: Bundle?) {
            if (p0 == null) {
                Log.d("TestListener", "onResults called with null.")
            } else {
                setResultsText(p0)
            }
        }

        override fun onPartialResults(p0: Bundle?) {
            if (p0 == null) {
                Log.d("TestListener", "onPartialResults called with null.")
            } else {
                setResultsText(p0)
            }
        }

        /**
         * Set recognition results from [bundle] to dictation textview.
         */
        private fun setResultsText(bundle: Bundle) {
            val results = bundle.get(SpeechRecognizer.RESULTS_RECOGNITION) as ArrayList<*>
            val text = results[0].toString()
            dictation_textview.text = text
        }

        override fun onEvent(p0: Int, p1: Bundle?) {
            Log.d("TestListener", "onEvent called.")
        }
    }
}