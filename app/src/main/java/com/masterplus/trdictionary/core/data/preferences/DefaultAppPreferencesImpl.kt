package com.masterplus.trdictionary.core.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.masterplus.trdictionary.core.domain.preferences.AppPreferences
import com.masterplus.trdictionary.core.domain.preferences.model.EnumPrefKey
import com.masterplus.trdictionary.core.domain.preferences.model.IEnumPrefValue
import com.masterplus.trdictionary.core.domain.preferences.model.PrefKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.transformLatest
import javax.inject.Inject

class DefaultAppPreferencesImpl @Inject constructor(
    private val pref: DataStore<Preferences>
): AppPreferences {


    override val dataFlow: Flow<Preferences>
        get() = pref.data


    override suspend fun <T> getItem(key: Preferences.Key<T>, defaultValue: T): T {
        return pref.data.first()[key] ?: defaultValue
    }

    override suspend fun <T> getItem(item: PrefKey<T>): T{
        return pref.data.first()[item]
    }

    override suspend fun <E : Enum<E>> getEnumItem(criteria: EnumPrefKey<E>): E {
        return pref.data.first()[criteria]
    }




    override suspend fun <T> setItem(key: Preferences.Key<T>, value: T) {
        pref.edit {
            it[key] = value
        }
    }

    override suspend fun <T> setItem(item: PrefKey<T>, value: T){
        pref.edit {
            it[item] = value
        }
    }

    override suspend fun <E : Enum<E>> setEnumItem(criteria: EnumPrefKey<E>, value: IEnumPrefValue) {
        pref.edit {
            it[criteria] = value
        }
    }



    override suspend fun edit(transform: suspend (MutablePreferences) -> Unit){
        pref.edit(transform)
    }


    override suspend fun clear() {
       pref.edit {
           it.clear()
       }
    }

    override fun dispose() {

    }

}



operator fun<T> Preferences.get(item: PrefKey<T>): T{
    val prefKey = getPreferencesKey(item.key,item.default) ?: return item.default
    return get(prefKey) ?: item.default
}

operator fun<E : Enum<E>> Preferences.get(item: EnumPrefKey<E>): E{
    val defaultValue = item.from(item.default.keyValue)
    val prefKey = getPreferencesKey(item.key,item.default.keyValue) ?: return defaultValue
    val prefValue = get(prefKey) ?: return defaultValue
    return item.from(prefValue)
}



operator fun<T> MutablePreferences.set(item: PrefKey<T>, value: T){
    val prefKey = getPreferencesKey(item.key,item.default) ?: return
    set(prefKey,value)
}

operator fun<E : Enum<E>> MutablePreferences.set(item: EnumPrefKey<E>, value: IEnumPrefValue){
    val prefKey = getPreferencesKey(item.key,item.default.keyValue) ?: return
    set(prefKey,value.keyValue)
}


private fun <T> getPreferencesKey(key: String, default: T): Preferences.Key<T>?{
    return when(default){
        is String -> stringPreferencesKey(key) as Preferences.Key<T>
        is Int -> intPreferencesKey(key) as Preferences.Key<T>
        is Boolean -> booleanPreferencesKey(key) as Preferences.Key<T>
        is Float -> floatPreferencesKey(key) as Preferences.Key<T>
        is Double -> doublePreferencesKey(key) as Preferences.Key<T>
        is Long -> longPreferencesKey(key) as Preferences.Key<T>
        is List<*> -> stringSetPreferencesKey(key) as Preferences.Key<T>
        else -> null
    }
}
