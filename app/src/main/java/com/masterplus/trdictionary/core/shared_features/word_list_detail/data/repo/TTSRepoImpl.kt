package com.masterplus.trdictionary.core.shared_features.word_list_detail.data.repo

import android.app.Application
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.ConnectivityProvider
import com.masterplus.trdictionary.core.domain.repo.AppFileRepo
import com.masterplus.trdictionary.core.shared_features.word_list_detail.data.remote.TextToSpeechDataSource
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.AudioState
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.repo.TTSRepo
import com.masterplus.trdictionary.core.util.Resource
import com.masterplus.trdictionary.core.util.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File
import javax.inject.Inject

class TTSRepoImpl @Inject constructor(
    application: Application,
    private val dataSource: TextToSpeechDataSource,
    private val appFileRepo: AppFileRepo,
    private val connectivityProvider: ConnectivityProvider
): TTSRepo {

    private val exoPlayer = ExoPlayer.Builder(application).build()

    private val _audioState = MutableStateFlow(AudioState())

    override val audioState: Flow<AudioState>
        get() = _audioState

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            _audioState.value = _audioState.value.copy(isPlaying = isPlaying)
        }
    }


    init {
        listenPlayingChange()
    }

    override suspend fun synthesizeAndPlay(key: String, text: String): Resource<Unit> {
        val fileName = "tts/$key.mp3"
        val file = appFileRepo.getFile(fileName)
        if(file != null){
            playWithExpo(file)
            return Resource.Success(Unit)
        }
        if(!connectivityProvider.hasConnection()){
            return Resource.Error(UiText.Resource(R.string.check_your_internet_connection))
        }

        return when(val result = dataSource.synthesize(text)){
            is Resource.Success -> {
                val fileResult = appFileRepo.insertFile(fileName,result.data)
                playWithExpo(fileResult)
                Resource.Success(Unit)
            }
            is Resource.Error -> {
                Resource.Error(result.error)
            }
        }
    }

    private fun playWithExpo(file: File){
        exoPlayer.setMediaItem(MediaItem.fromUri(file.absolutePath))
        exoPlayer.prepare()
        exoPlayer.play()
    }



    private fun listenPlayingChange(){
        exoPlayer.addListener(playerListener)
    }


    override fun dispose() {
        exoPlayer.removeListener(playerListener)
        exoPlayer.release()
    }
}