package com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.repo

import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.AudioState
import com.masterplus.trdictionary.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface TTSRepo {

    val audioState: Flow<AudioState>

    suspend fun synthesizeAndPlay(key: String, text: String): Resource<Unit>

    fun dispose()

}