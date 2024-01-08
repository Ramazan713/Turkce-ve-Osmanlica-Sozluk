package com.masterplus.trdictionary.core.shared_features.word_list_detail.data.repo

import com.masterplus.trdictionary.core.domain.utils.Resource
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.AudioState
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.repo.TTSRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Assertions.*

class TTSRepoFake: TTSRepo{

    var returnedSynthesizeAndPlay: Resource<Unit> = Resource.Success(Unit)

    var returnedAudioStateFlow = flow<AudioState>{

    }

    override val audioState: Flow<AudioState>
        get() = returnedAudioStateFlow

    override suspend fun synthesizeAndPlay(key: String, text: String): Resource<Unit> {
        return returnedSynthesizeAndPlay
    }

    override fun dispose() {

    }

}