package com.masterplus.trdictionary.core.shared_features.word_list_detail.data.remote

import com.google.gson.annotations.SerializedName

data class TextToSpeechRequest(
    val input: SynthesisInputDto,
    val voice: VoiceSelectionParamsDto,
    val audioConfig: AudioConfigDto,
)


data class SynthesisInputDto(
    @SerializedName("text")
    val text: String?,
)

data class AudioConfigDto(
    @SerializedName("audioEncoding")
    val audioEncoding: String,
)

data class VoiceSelectionParamsDto(
    val languageCode: String,
    val name: String
)