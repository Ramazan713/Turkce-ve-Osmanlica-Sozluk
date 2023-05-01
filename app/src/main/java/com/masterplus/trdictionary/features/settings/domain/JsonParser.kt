package com.masterplus.trdictionary.features.settings.domain

import java.lang.reflect.Type

interface JsonParser {

    fun toJson(data: Any): String

    fun <T> fromJson(data: String, type: Type): T

}