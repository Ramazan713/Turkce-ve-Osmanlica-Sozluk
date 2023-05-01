package com.masterplus.trdictionary.core.domain.util

import com.masterplus.trdictionary.core.domain.enums.DateFormatEnum
import java.text.SimpleDateFormat
import java.util.*

object DateFormatHelper {

    fun getReadableDate(date: Date, formatEnum: DateFormatEnum): String{
        return SimpleDateFormat(formatEnum.format, Locale.getDefault()).format(date.time)
    }

    fun getReadableDate(milliSeconds: Long,formatEnum: DateFormatEnum): String{
        val date = Calendar.getInstance().time.apply { time = milliSeconds }
        return getReadableDate(date,formatEnum)
    }

    fun toDate(source: String,formatEnum: DateFormatEnum): Date?{
        return SimpleDateFormat(formatEnum.format, Locale.getDefault()).parse(source)
    }

    fun toDateMillis(timeMillis: Long): Long {
        val source = getReadableDate(timeMillis,DateFormatEnum.DateFull)
        return toDate(source,DateFormatEnum.DateFull)?.time ?: 0L
    }
}