package com.masterplus.trdictionary.features.home.domain.use_cases

import android.content.SharedPreferences
import com.masterplus.trdictionary.core.domain.enums.ProverbIdiomEnum
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.features.home.domain.ShortInfoEnum
import com.masterplus.trdictionary.features.home.domain.repo.ShortInfoRepo
import javax.inject.Inject

class GetShortInfo @Inject constructor(
    private val shortInfoRepo: ShortInfoRepo,
    private val sharedPreferences: SharedPreferences,
) {

    suspend operator fun invoke(shortInfo: ShortInfoEnum, refresh: Boolean): SimpleWordResult?{

        val randomNumber = if(refresh) refreshNumber(shortInfo) else
            getRandomNumber(shortInfo)

        return getWord(shortInfo, randomNumber)
    }

    private fun getRandomNumber(shortInfo: ShortInfoEnum): Int {
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