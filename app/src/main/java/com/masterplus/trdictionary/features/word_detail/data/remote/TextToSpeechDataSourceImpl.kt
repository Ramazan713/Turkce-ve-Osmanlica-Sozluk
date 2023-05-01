package com.masterplus.trdictionary.features.word_detail.data.remote

import android.util.Base64
import com.masterplus.trdictionary.BuildConfig
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.util.Resource
import com.masterplus.trdictionary.core.domain.util.UiText
import javax.inject.Inject


class TextToSpeechDataSourceImpl @Inject constructor(
    private val service: TextToSpeechApiService,
) : TextToSpeechDataSource {

    override suspend fun synthesize(text: String): Resource<ByteArray> {
        val request = TextToSpeechRequest(
            input = SynthesisInputDto(text = text),
            voice = VoiceSelectionParamsDto(
                languageCode = "tr",
                name = "tr-TR-Standard-D"
            ),
            audioConfig = AudioConfigDto(audioEncoding = "MP3")
        )
        try {
            val response = service.synthesize(
                authorization = BuildConfig.TTS_CLIENT_ID,
                key = BuildConfig.TTS_KEY,
                request = request
            )
            if(response.isSuccessful){
                val bytes = Base64.decode(response.body()?.audioContent, Base64.DEFAULT)
                return Resource.Success(bytes)
            }
        }catch (_: Exception){ }
        return Resource.Error(UiText.Resource(R.string.something_went_wrong))

    }
}