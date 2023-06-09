package com.masterplus.trdictionary.core.data.preferences

import android.content.SharedPreferences
import com.masterplus.trdictionary.core.domain.constants.KPref
import com.masterplus.trdictionary.core.domain.preferences.model.EnumPrefKey
import com.masterplus.trdictionary.core.domain.preferences.model.IEnumPrefValue
import com.masterplus.trdictionary.core.domain.preferences.model.PrefKey
import com.masterplus.trdictionary.core.domain.preferences.AppPreferences
import javax.inject.Inject

class AppPreferencesImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
): AppPreferences {

    override fun <T>setItem(item: PrefKey<T>, value: T){
        when(value){
            is String->{
                sharedPreferences.edit().putString(item.key,value).apply()
            }
            is Int->{
                sharedPreferences.edit().putInt(item.key,value).apply()
            }
            is Boolean->{
                sharedPreferences.edit().putBoolean(item.key,value).apply()
            }
            is Float->{
                sharedPreferences.edit().putFloat(item.key,value).apply()
            }
            is Long->{
                sharedPreferences.edit().putLong(item.key,value).apply()
            }
        }
    }

    override fun<T>getItem(item: PrefKey<T>): T{
        val result = when(item.default){
            is String->{
                sharedPreferences.getString(item.key,item.default)
            }
            is Int->{
                sharedPreferences.getInt(item.key,item.default)
            }
            is Boolean->{
                sharedPreferences.getBoolean(item.key,item.default)
            }
            is Float->{
                sharedPreferences.getFloat(item.key,item.default)
            }
            is Long->{
                sharedPreferences.getLong(item.key,item.default)
            }
            else -> {
                null
            }
        } as T?

        return result?:item.default
    }

    override fun <E:Enum<E>> setEnumItem(criteria: EnumPrefKey<E>, value: IEnumPrefValue){
        sharedPreferences.edit().putInt(criteria.key,value.keyValue).apply()
    }

    override fun <E:Enum<E>> getEnumItem(criteria: EnumPrefKey<E>): E{
        val keyValue = sharedPreferences.getInt(criteria.key,criteria.default.keyValue)
        return criteria.from(keyValue)
    }

    override fun clear(){
        sharedPreferences.edit().clear().apply()
    }

    override fun toDict(): Map<String, Any> {
        val result = mutableMapOf<String,Any>()
        KPref.prefValues.forEach { pref->
            val value = getItem(pref)
            result[pref.key] = mapOf("value" to value, "type" to "classic")
        }
        KPref.prefEnumValues.forEach { pref->
            val value = getEnumItem(pref)
            if(value is IEnumPrefValue){
                result[pref.key] = mapOf("value" to value.keyValue, "type" to "enum")
            }

        }
        return result
    }

    override fun fromDict(map: Map<String, Any>) {
        for (key in map.keys){
            val valueDict = (map[key] as? Map<*, *>) ?: continue
            val value = valueDict["value"] ?: continue
            val type = valueDict["type"] ?: continue

            when(type){
                "classic"->{
                    KPref.prefValues.find { it.key == key }?.let { pref->
                        setItem(pref,value)
                    }
                }
                "enum"->{
                    val keyValue = value.toString().toFloatOrNull()?.toInt() ?: continue
                    KPref.prefEnumValues.find { it.key == key }?.let {
                        sharedPreferences.edit().putInt(key,keyValue).apply()
                    }
                }
            }
        }
    }
}