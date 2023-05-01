package com.masterplus.trdictionary.core.domain.enums

enum class DateFormatEnum(val format: String) {
    Readable("dd MMM yyyy HH:mm"),
    DateTimeFull("yyyy-MM-dd HH:mm:ss"),
    DateFull("yyyy-MM-dd")
}