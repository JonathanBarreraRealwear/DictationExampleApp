package com.realwear.dictationtestapp

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var mSpeechRecognizer: SpeechRecognizer

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun asyncDictation(v: View) {
        async_button.isEnabled = false
        continuous_dictation.isEnabled = false
        stop_button.isEnabled = false
        default_dictation_button.isEnabled = false

        val intent = createRecognizerIntent()
        intent.putExtra(EXTRA_DICTATION_TYPE, ASYNC_DICTATION)
        startListening(intent)
    }

    fun startDictation(v: View) {
        async_button.isEnabled = false
        continuous_dictation.isEnabled = false
        stop_button.isEnabled = true
        default_dictation_button.isEnabled = false

        val intent = createRecognizerIntent()
        intent.putExtra(EXTRA_DICTATION_TYPE, CONTINUOUS_DICTATION)
        startListening(intent)
    }

    fun stopDictation(v: View) {
        async_button.isEnabled = true
        continuous_dictation.isEnabled = true
        stop_button.isEnabled = false
        default_dictation_button.isEnabled = true

        mSpeechRecognizer.destroy()
    }

    private fun startListening(intent: Intent) {
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        mSpeechRecognizer.setRecognitionListener(TestListener())
        mSpeechRecognizer.startListening(intent)
    }

    fun showCurrentDefaultDictation(v: View) {
        val serviceComponent: String = Settings.Secure.getString(
            applicationContext.contentResolver,
            "voice_recognition_service"
        )
        dictation_textview.text = serviceComponent
    }

    companion object {
        private const val EXTRA_DICTATION_TYPE = "extra_dictation_type"
        private const val ASYNC_DICTATION = 0
        private const val CONTINUOUS_DICTATION = 1
        private fun createRecognizerIntent(): Intent {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            return intent
        }
    }

    inner class TestListener: RecognitionListener {
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
            async_button.isEnabled = true
            continuous_dictation.isEnabled = true
            stop_button.isEnabled = false
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

        private fun setResultsText(bundle: Bundle) {
            val results = bundle.get(SpeechRecognizer.RESULTS_RECOGNITION) as ArrayList<String>
            var text = results[0]

            text = if (results.size > 1) {
                TextUtils.join(" ", results)
            } else {
                results[0]
            }

            dictation_textview.text = text

        }

        override fun onEvent(p0: Int, p1: Bundle?) {
            Log.d("TestListener", "onEvent called.")
        }
    }
}