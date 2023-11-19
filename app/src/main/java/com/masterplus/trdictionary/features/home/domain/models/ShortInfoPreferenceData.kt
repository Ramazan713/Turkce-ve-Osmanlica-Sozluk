package com.masterplus.trdictionary.features.home.domain.models

import com.masterplus.trdictionary.core.domain.BaseAppSerializer
import com.masterplus.trdictionary.features.home.domain.enums.ShortInfoEnum
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import javax.inject.Singleton

@Serializable
data class ShortInfoPreferenceData(
    val wordRandomNumber: Int = 0,
    val proverbRandomNumber: Int = 0,
    val idiomRandomNumber: Int = 0,
    val dayForDayRefreshing: Int = 0
){

    fun getRandomNumber(shortInfo: ShortInfoEnum): Int{
        return when(shortInfo){
            ShortInfoEnum.Proverb -> proverbRandomNumber
            ShortInfoEnum.Idiom -> idiomRandomNumber
            ShortInfoEnum.Word -> wordRandomNumber
        }
    }

}


@Singleton
class ShortInfoPreferenceDataSerializer: BaseAppSerializer<ShortInfoPreferenceData>(){

    override val serializer: KSerializer<ShortInfoPreferenceData>
        get() = ShortInfoPreferenceData.serializer()

    override val defaultValue: ShortInfoPreferenceData
        get() = ShortInfoPreferenceData()
}