package com.masterplus.trdictionary.features.home.data.manager

import android.util.Log
import com.masterplus.trdictionary.core.domain.enums.ProverbIdiomEnum
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.features.home.domain.enums.ShortInfoEnum
import com.masterplus.trdictionary.features.home.domain.manager.ShortInfoManager
import com.masterplus.trdictionary.features.home.domain.models.ShortInfoCollectionResult
import com.masterplus.trdictionary.features.home.domain.repo.ShortInfoPreference
import com.masterplus.trdictionary.features.home.domain.repo.ShortInfoRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class ShortInfoManagerImpl @Inject constructor(
    private val shortInfoRepo: ShortInfoRepo,
    private val preference: ShortInfoPreference
): ShortInfoManager {

    override suspend fun getWord(shortInfoEnum: ShortInfoEnum, refresh: Boolean): SimpleWordResult?{
        val randomNumber = getRandomNumber(shortInfoEnum,refresh)
        return getWord(shortInfoEnum,randomNumber)
    }

    override suspend fun getWord(shortInfo: ShortInfoEnum, randomNumber: Int): SimpleWordResult?{
        return when(shortInfo){
            ShortInfoEnum.Proverb -> {
                shortInfoRepo.getWordByTypeId(ProverbIdiomEnum.Proverb,randomNumber)
            }
            ShortInfoEnum.Idiom -> {
                shortInfoRepo.getWordByTypeId(ProverbIdiomEnum.Idiom,randomNumber)
            }
            ShortInfoEnum.Word -> {
                shortInfoRepo.getWord(randomNumber)
            }
        }
    }

    override suspend fun getWords(refresh: Boolean): ShortInfoCollectionResult{
        val word = getWord(ShortInfoEnum.Word,refresh)
        val idiom = getWord(ShortInfoEnum.Idiom,refresh)
        val proverb = getWord(ShortInfoEnum.Proverb,refresh)

        return ShortInfoCollectionResult(
            word = word,
            idiom = idiom,
            proverb = proverb
        )
    }

    override fun getWordsFlow(): Flow<ShortInfoCollectionResult>{
        return preference.dataFlow.map {
            val word = getWord(ShortInfoEnum.Word, it.wordRandomNumber)
            val idiom = getWord(ShortInfoEnum.Idiom, it.idiomRandomNumber)
            val proverb = getWord(ShortInfoEnum.Proverb, it.proverbRandomNumber)

            ShortInfoCollectionResult(
                word = word,
                idiom = idiom,
                proverb = proverb
            )
        }
    }


    override suspend fun refreshWords(){
        ShortInfoEnum.entries.forEach { info->
            refreshNumber(info)
        }
    }

    override suspend fun refreshWord(shortInfo: ShortInfoEnum){
        refreshNumber(shortInfo)
    }

    override suspend fun checkDayForRefresh(): Boolean{
        val dayOfMonth = preference.getData().dayForDayRefreshing
        val currentDayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        val refresh = currentDayOfMonth != dayOfMonth

        if(refresh){
            preference.updateData { it.copy(dayForDayRefreshing = currentDayOfMonth) }
            refreshWords()
        }
        return refresh
    }


    private suspend fun getRandomNumber(shortInfo: ShortInfoEnum,refresh: Boolean): Int {
        if(refresh)
            return refreshNumber(shortInfo)
        return preference.getData().getRandomNumber(shortInfo)
    }

    private suspend fun refreshNumber(shortInfo: ShortInfoEnum): Int{
        val randomNumber: Int = when(shortInfo){
            ShortInfoEnum.Proverb -> {
                val proverbCount = shortInfoRepo.countWordsByTypeId(ProverbIdiomEnum.Proverb)
                val random = (0..<max(1,proverbCount)).random()
                preference.updateData { it.copy(proverbRandomNumber = random) }
                random
            }
            ShortInfoEnum.Idiom -> {
                val idiomCount = shortInfoRepo.countWordsByTypeId(ProverbIdiomEnum.Idiom)
                val random = (0..<max(1,idiomCount)).random()
                preference.updateData { it.copy(idiomRandomNumber = random) }
                random
            }
            ShortInfoEnum.Word -> {
                val wordCount = shortInfoRepo.countWords()
                val random = (0..<max(1,wordCount)).random()
                preference.updateData { it.copy(wordRandomNumber = random) }
                random
            }
        }
        return randomNumber
    }
}