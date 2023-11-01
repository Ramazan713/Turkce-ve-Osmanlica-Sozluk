package com.masterplus.trdictionary.features.word_detail.domain.use_case.tts

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.masterplus.trdictionary.core.util.Resource
import com.masterplus.trdictionary.features.word_detail.data.remote.TextToSpeechDataSource
import java.io.File
import javax.inject.Inject

class TextToSpeechUseCase @Inject constructor(
    private val dataSource: TextToSpeechDataSource,
    private val context: Context
){
    private val exoPlayer = ExoPlayer.Builder(context).build()


    fun setListener(listener: (Boolean)->Unit){
        exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                listener.invoke(isPlaying)
            }
        })
    }

    suspend fun synthesizeAndPlay(text: String): Resource<Unit> {
        return when(val result = dataSource.synthesize(text)){
            is Resource.Success -> {
                playWithExpo(result.data)
                Resource.Success(Unit)
            }
            is Resource.Error -> {
                Resource.Error(result.error)
            }
        }
    }

    private fun playWithExpo(bytes: ByteArray){
        val file = File(context.cacheDir, "audio.mp3")
        if(!file.exists()){
            file.createNewFile()
        }

        file.outputStream().use {
            it.write(bytes)
        }
        exoPlayer.setMediaItem(MediaItem.fromUri(file.absolutePath))
        exoPlayer.prepare()
        exoPlayer.play()
    }


    fun dispose(){
        exoPlayer.stop()
    }

}