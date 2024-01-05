package com.masterplus.trdictionary.features.home.data.manager

import com.masterplus.trdictionary.core.domain.enums.WordType
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.core.utils.sample_data.shortInfoCollectionResult
import com.masterplus.trdictionary.core.utils.sample_data.simpleWordResult
import com.masterplus.trdictionary.core.utils.toWordType
import com.masterplus.trdictionary.features.home.domain.enums.ShortInfoEnum
import com.masterplus.trdictionary.features.home.domain.manager.ShortInfoManager
import com.masterplus.trdictionary.features.home.domain.models.ShortInfoCollectionResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class ShortInfoManagerFake: ShortInfoManager {


    var returnedCheckDayForRefresh: Boolean = false

    var initCollection = shortInfoCollectionResult()

    private val _state = MutableStateFlow<ShortInfoCollectionResult>(initCollection)



    override suspend fun getWord(
        shortInfoEnum: ShortInfoEnum,
        refresh: Boolean
    ): SimpleWordResult? {
        if(refresh){
            refreshCollection(shortInfoEnum)
        }
        return getWord(shortInfoEnum)
    }

    override suspend fun getWord(shortInfo: ShortInfoEnum, randomNumber: Int): SimpleWordResult? {
        return simpleWordResult(wordId = randomNumber, wordType = shortInfo.toWordType())
    }

    override suspend fun getWords(refresh: Boolean): ShortInfoCollectionResult {
        if(refresh){
            refreshCollection()
        }
        return _state.value
    }

    override fun getWordsFlow(): Flow<ShortInfoCollectionResult> {
        return _state.asStateFlow()
    }

    override suspend fun refreshWords() {
        refreshCollection()
    }

    override suspend fun refreshWord(shortInfo: ShortInfoEnum) {
        refreshCollection(shortInfo)
    }

    override suspend fun checkDayForRefresh(): Boolean {
        if(returnedCheckDayForRefresh){
            refreshCollection()
        }
        return returnedCheckDayForRefresh
    }



    private fun refreshCollection(shortInfo: ShortInfoEnum){
        _state.update { state->
            when(shortInfo){
                ShortInfoEnum.Proverb -> {
                    state.copy(proverb = simpleWordResult(wordId = state.proverb!!.wordId + 1, wordType = WordType.Proverb))
                }
                ShortInfoEnum.Idiom -> {
                    state.copy(idiom = simpleWordResult(wordId = state.idiom!!.wordId + 1, wordType = WordType.Idiom))
                }
                ShortInfoEnum.Word -> {
                    state.copy(word = simpleWordResult(wordId = state.word!!.wordId + 1, wordType = WordType.PureWord))
                }
            }
        }
    }

    private fun refreshCollection(){
        _state.update { state->
            state.copy(
                proverb = simpleWordResult(wordId = state.proverb!!.wordId + 1, wordType = WordType.Proverb),
                idiom = simpleWordResult(wordId = state.idiom!!.wordId + 1, wordType = WordType.Idiom),
                word = simpleWordResult(wordId = state.word!!.wordId + 1, wordType = WordType.PureWord)
            )
        }
    }

    private fun getWord(shortInfo: ShortInfoEnum): SimpleWordResult?{
        return _state.value.let { state->
            when(shortInfo){
                ShortInfoEnum.Proverb -> state.proverb
                ShortInfoEnum.Idiom -> state.idiom
                ShortInfoEnum.Word -> state.word
            }
        }
    }
}