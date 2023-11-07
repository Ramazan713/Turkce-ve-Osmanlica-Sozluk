package com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.use_case.tts

import com.masterplus.trdictionary.core.domain.ConnectivityProvider
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.AudioState
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class TTSNetworkAudioUseCase @Inject constructor(
    private val ttsUseCase: TextToSpeechUseCase,
    private val connectivityProvider: ConnectivityProvider
) {

    private val _audioState = MutableStateFlow(AudioState())

    val audioState: Flow<AudioState> get() =
        combine(_audioState,connectivityProvider.observeConnection()){audio,connectivity->
            audio.copy(isVisible = connectivity == ConnectivityProvider.Status.Connected)
        }.distinctUntilChanged()

    init {
        ttsListener()
    }

    private fun ttsListener(){
        ttsUseCase.setListener {isPlaying->
            _audioState.value = _audioState.value.copy(
                isPlaying = isPlaying
            )
        }
    }

    suspend operator fun invoke(text: String){
        ttsUseCase.synthesizeAndPlay(text)
    }

    fun dispose(){
        ttsUseCase.dispose()
    }
}