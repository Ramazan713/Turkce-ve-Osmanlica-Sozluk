package com.masterplus.trdictionary.features.home.domain.use_cases

import android.content.SharedPreferences
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.features.home.domain.enums.ShortInfoEnum
import java.util.*
import javax.inject.Inject

class GetShortInfosResult @Inject constructor(
    private val getShortInfo: GetShortInfo,
    private val sharedPreferences: SharedPreferences
) {

    suspend operator fun invoke(): ShortInfosResult{
        val dayOfMonth = sharedPreferences.getInt("shortInfoDay",0)
        val currentDayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        val refresh = currentDayOfMonth != dayOfMonth

        if(refresh){
            sharedPreferences.edit().putInt("shortInfoDay",currentDayOfMonth).apply()
        }
        return getResult(refresh)
    }

    private suspend fun getResult(refresh: Boolean): ShortInfosResult {
        val word = getShortInfo(ShortInfoEnum.Word,refresh)
        val idiom = getShortInfo(ShortInfoEnum.Idiom,refresh)
        val proverb = getShortInfo(ShortInfoEnum.Proverb,refresh)
        return ShortInfosResult(
            word, idiom, proverb
        )
    }
}

data class ShortInfosResult(
    val word: SimpleWordResult?,
    val idiom: SimpleWordResult?,
    val proverb: SimpleWordResult?,
)
