package com.realwear.dictationtestapp

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val mIntent = createRecognizerIntent()
    private lateinit var mSpeechRecognizer: SpeechRecognizer

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startDictation(v: View) {
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        mSpeechRecognizer.setRecognitionListener(TestListener())
        mSpeechRecognizer.startListening(mIntent)
    }

    fun stopDictation(v:View) {
        mSpeechRecognizer.stopListening()
    }

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

    class TestListener: RecognitionListener {
        override fun onReadyForSpeech(p0: Bundle?) {
            Log.d("TestListener", "On ready for speech called.")
        }

        override fun onBeginningOfSpeech() {
            Log.d("TestListener", "On beginning of speech called.")
        }

        override fun onRmsChanged(p0: Float) {
            Log.d("TestListener", "On rmc called.")
        }

        override fun onBufferReceived(p0: ByteArray?) {
            Log.d("TestListener", "On buffer called.")
        }

        override fun onEndOfSpeech() {
            Log.d("TestListener", "On end of speech called.")
        }

        override fun onError(p0: Int) {
            Log.e("TestListener error", p0.toString() + " called")
        }

        override fun onResults(p0: Bundle?) {
            if (p0 == null) {
                Log.d("TestListener", "On results called with null")
            }
            val results = p0?.get(SpeechRecognizer.RESULTS_RECOGNITION) as ArrayList<String>
            val text = results[0]
            Log.d("TestListener", "On results called. " + text)
        }

        override fun onPartialResults(p0: Bundle?) {
            Log.d("TestListener", "On partial results called.")
        }

        override fun onEvent(p0: Int, p1: Bundle?) {
            Log.d("TestListener", "On event called.")
        }

    }
}