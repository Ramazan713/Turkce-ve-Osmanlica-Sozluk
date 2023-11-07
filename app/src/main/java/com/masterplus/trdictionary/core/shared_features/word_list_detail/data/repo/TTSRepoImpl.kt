package com.masterplus.trdictionary.core.shared_features.word_list_detail.data.repo

import android.app.Application
import android.speech.tts.TextToSpeech
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.repo.TTSRepo
import java.util.*
import javax.inject.Inject

class TTSRepoImpl @Inject constructor(
    application: Application
): TTSRepo {

    private val textToSpeech: TextToSpeech =  TextToSpeech(application) { }
        .apply {
            language = Locale("tr")
        }

    override fun speak(text: String) {
        textToSpeech.stop()
        textToSpeech.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            null
        )
    }

    override fun dispose() {
        textToSpeech.shutdown()
    }
}