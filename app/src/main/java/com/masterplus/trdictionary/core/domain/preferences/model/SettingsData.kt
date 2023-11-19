package com.masterplus.trdictionary.core.domain.preferences.model

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.masterplus.trdictionary.core.domain.enums.ThemeEnum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Singleton

@Serializable
data class SettingsData(
    val themeEnum: ThemeEnum = ThemeEnum.defaultValue,
    val useThemeDynamic: Boolean = false,
    val useArchiveLikeList: Boolean = false,
    val showBackupSectionForLogin: Boolean = false,
    val searchResultCount: Int = 10
)


@Singleton
class SettingsDataSerializer: Serializer<SettingsData> {
    override val defaultValue: SettingsData = SettingsData()

    private val json = Json {
        ignoreUnknownKeys = true
    }

    override suspend fun readFrom(input: InputStream): SettingsData {
        try {
            return withContext(Dispatchers.IO){
                json.decodeFromString(SettingsData.serializer(),input.readBytes().decodeToString())
            }
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }


    override suspend fun writeTo(
        t: SettingsData,
        output: OutputStream
    ){
        withContext(Dispatchers.IO) {
            output.write(
                json.encodeToString(SettingsData.serializer(), t).encodeToByteArray()
            )
        }
    }

}