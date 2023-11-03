package com.masterplus.trdictionary.core.domain.model
import com.masterplus.trdictionary.core.domain.enums.AutoType
import com.masterplus.trdictionary.core.domain.enums.DateFormatEnum
import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.util.DateFormatHelper
import java.util.*

data class SavePoint(
    val id: Int?,
    val title: String,
    val itemPosIndex: Int,
    val savePointDestination: SavePointDestination,
    val modifiedDate: Calendar,
    val autoType: AutoType
){

    fun getReadableDate(): String{
        return DateFormatHelper.getReadableDate(modifiedDate.time, DateFormatEnum.Readable)
    }

    companion object{

        fun getTitle(shortTitle: String,
                     autoType: AutoType = AutoType.Default,
                     dateParam: Calendar? = null
        ): String{
            val date = dateParam ?: Calendar.getInstance()
            val readableDate = DateFormatHelper.getReadableDate(date.time,DateFormatEnum.Readable)
            val buildTitle = buildString {
                autoType.label?.let { label->
                    append("$label - ")
                }
                append("$shortTitle - ")
                append(readableDate)
            }
            return buildTitle
        }
    }

    override fun equals(other: Any?): Boolean {
        if(other is SavePoint){
            return id == other.id
        }
        return super.equals(other)
    }
}
