package com.neuroos.app

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class SpeechManager(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var isReady = false
    private val appContext = context.applicationContext

    init {
        android.util.Log.d("SpeechManager", "Initializing TTS...")
        tts = TextToSpeech(appContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                android.util.Log.e("SpeechManager", "Language US not supported. Result: $result")
            } else {
                isReady = true
                android.util.Log.d("SpeechManager", "TTS Initialized and Ready")
            }
        } else {
            android.util.Log.e("SpeechManager", "TTS Initialization failed (Status $status). This usually means no TTS engine (like Google Speech) is installed on this device/emulator.")
        }
    }

    fun speak(text: String) {
        if (isReady) {
            android.util.Log.d("SpeechManager", "Speaking: $text")
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "NeuroOS_TTS")
        } else {
            android.util.Log.w("SpeechManager", "Speak called but TTS not ready")
        }
    }

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.shutdown()
    }
}
