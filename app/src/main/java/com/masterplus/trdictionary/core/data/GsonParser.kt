package com.masterplus.trdictionary.core.data

import com.google.gson.Gson
import com.masterplus.trdictionary.core.domain.JsonParser
import java.lang.reflect.Type

class GsonParser : JsonParser {

    private val gson = Gson()

    override fun toJson(data: Any): String{
        return gson.toJson(data)
    }

    override fun <T> fromJson(data: String, type: Type): T{
        return gson.fromJson(data,type)
    }

}