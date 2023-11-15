package com.masterplus.trdictionary.core.data.preferences

import android.content.SharedPreferences
import android.util.Log
import com.masterplus.trdictionary.core.domain.constants.KPref
import com.masterplus.trdictionary.core.domain.preferences.model.EnumPrefKey
import com.masterplus.trdictionary.core.domain.preferences.model.IEnumPrefValue
import com.masterplus.trdictionary.core.domain.preferences.model.PrefKey
import com.masterplus.trdictionary.core.domain.preferences.AppPreferences
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import kotlin.reflect.typeOf

class AppPreferencesImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
): AppPreferences {

    private val mutableChangedPrefKeyFlow = MutableSharedFlow<PrefKey<Any>>(replay = 1)
    private val mutableChangedEnumPrefKeyFlow = MutableSharedFlow<EnumPrefKey<*>>(replay = 1)
    private val mutableChangedKeyFlow = MutableSharedFlow<String>(replay = 1)

    override val changedPrefKeyFlow: SharedFlow<PrefKey<Any>>
        get() = mutableChangedPrefKeyFlow.asSharedFlow()
    override val changedKeyFlow: SharedFlow<String>
        get() = mutableChangedKeyFlow.asSharedFlow()

    override val changedEnumPrefKeyFlow: SharedFlow<EnumPrefKey<*>>
        get() = mutableChangedEnumPrefKeyFlow.asSharedFlow()

    private val listener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if(key != null){
                KPref.prefValues.firstOrNull { it.key == key }?.let { prefKey->
                    mutableChangedPrefKeyFlow.tryEmit(prefKey)
                }
                KPref.prefEnumValues.firstOrNull { it.key == key }?.let { enumPrefKey ->
                    mutableChangedEnumPrefKeyFlow.tryEmit(enumPrefKey)
                }
                mutableChangedKeyFlow.tryEmit(key)
            }
        }

    init {
        addListener()
    }

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
                        var updatedValue = value
                        if(pref.default is Int){
                            updatedValue = (value).toString().toDoubleOrNull()?.toInt() ?: return@let
                        }
                        setItem(pref,updatedValue)
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

    private fun addListener(){
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun dispose() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }
}