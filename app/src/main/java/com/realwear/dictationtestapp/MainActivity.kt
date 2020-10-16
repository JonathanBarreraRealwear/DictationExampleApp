package com.realwear.dictationtestapp

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognizerIntent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val mIntent = createRecognizerIntent()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startDictation(v: View) {
        startActivityForResult(mIntent, 1234)
    }

    fun showCurrentDefaultDictation(v: View) {
        val serviceComponent: String = Settings.Secure.getString(
            applicationContext.contentResolver,
            "voice_recognition_service"
        )
        dictation_textview.text = serviceComponent
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && data != null) {
            onSuccess(data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun onSuccess(data: Intent) {
        val stringArray = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
        val results = stringArray?.joinToString()
        dictation_textview.text = results
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
}