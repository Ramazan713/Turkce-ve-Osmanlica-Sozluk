package com.masterplus.trdictionary.features.home.data.manager

import android.content.SharedPreferences
import android.util.Log
import com.masterplus.trdictionary.core.domain.enums.ProverbIdiomEnum
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.core.domain.preferences.AppPreferences
import com.masterplus.trdictionary.features.home.domain.enums.ShortInfoEnum
import com.masterplus.trdictionary.features.home.domain.manager.ShortInfoManager
import com.masterplus.trdictionary.features.home.domain.models.ShortInfoCollectionResult
import com.masterplus.trdictionary.features.home.domain.repo.ShortInfoRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import java.util.Calendar
import javax.inject.Inject

class ShortInfoManagerImpl @Inject constructor(
    private val shortInfoRepo: ShortInfoRepo,
    private val sharedPreferences: SharedPreferences,
    private val appPreferences: AppPreferences
): ShortInfoManager {
    private val keyForDayRefreshing = "shortInfoDay"
    private val shortInfoKeys = ShortInfoEnum.values().map { it.saveKey }

    override suspend fun getWord(shortInfoEnum: ShortInfoEnum, refresh: Boolean): SimpleWordResult?{
        val randomNumber = getRandomNumber(shortInfoEnum,refresh)
        return getWord(shortInfoEnum,randomNumber)
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

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getWordsFlow(): Flow<ShortInfoCollectionResult>{
//        return appPreferences.changedKeyFlow.filter { key->
//            shortInfoKeys.contains(key)
//        }.mapLatest {_->
//            getWords(false)
//        }
        return flow {  }
    }


    override suspend fun refreshWords(){
        ShortInfoEnum.values().forEach { info->
            refreshNumber(info)
        }
    }

    override suspend fun refreshWord(shortInfo: ShortInfoEnum){
        refreshNumber(shortInfo)
    }

    override suspend fun checkDayForRefresh(): Boolean{
        val dayOfMonth = sharedPreferences.getInt(keyForDayRefreshing,0)
        val currentDayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        val refresh = currentDayOfMonth != dayOfMonth

        if(refresh){
            sharedPreferences.edit().putInt(keyForDayRefreshing,currentDayOfMonth).apply()
            refreshWords()
        }
        return refresh
    }


    private suspend fun getRandomNumber(shortInfo: ShortInfoEnum,refresh: Boolean): Int {
        if(refresh)
            return refreshNumber(shortInfo)
        return sharedPreferences.getInt(shortInfo.saveKey,0)
    }

    private suspend fun refreshNumber(shortInfo: ShortInfoEnum): Int{
        val randomNumber: Int = when(shortInfo){
            ShortInfoEnum.Proverb -> {
                val proverbCount = shortInfoRepo.countWordsByTypeId(ProverbIdiomEnum.Proverb)
                (0..proverbCount).random()
            }
            ShortInfoEnum.Idiom -> {
                val idiomCount = shortInfoRepo.countWordsByTypeId(ProverbIdiomEnum.Idiom)
                (0..idiomCount).random()
            }
            ShortInfoEnum.Word -> {
                val wordCount = shortInfoRepo.countWordsByTypeId(ProverbIdiomEnum.Idiom)
                (0..wordCount).random()
            }
        }
        sharedPreferences.edit().putInt(shortInfo.saveKey,randomNumber).apply()
        return randomNumber
    }

    private suspend fun getWord(shortInfo: ShortInfoEnum, randomNumber: Int): SimpleWordResult?{
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
}