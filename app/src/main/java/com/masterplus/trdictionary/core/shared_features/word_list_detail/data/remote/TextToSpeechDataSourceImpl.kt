package com.masterplus.trdictionary.core.shared_features.word_list_detail.data.remote

import android.app.Application
import android.util.Base64
import com.masterplus.trdictionary.BuildConfig
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.use_cases.GetAppSha1UseCase
import com.masterplus.trdictionary.core.domain.utils.Resource
import com.masterplus.trdictionary.core.domain.utils.UiText
import javax.inject.Inject


class TextToSpeechDataSourceImpl @Inject constructor(
    private val service: TextToSpeechApiService,
    private val getAppSha1UseCase: GetAppSha1UseCase,
    private val application: Application
) : TextToSpeechDataSource {

    override suspend fun synthesize(text: String): Resource<ByteArray> {
        val sha1 = getAppSha1UseCase.invoke() ?: return Resource.Error(UiText.Resource(R.string.something_went_wrong))
        val packageName = application.packageName

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
                request = request,
                packageName = packageName,
                cert = sha1
            )
            if(response.isSuccessful){
                val bytes = Base64.decode(response.body()?.audioContent, Base64.DEFAULT)
                return Resource.Success(bytes)
            }
        }catch (e: Exception){}

        return Resource.Error(UiText.Resource(R.string.something_went_wrong))

    }
}