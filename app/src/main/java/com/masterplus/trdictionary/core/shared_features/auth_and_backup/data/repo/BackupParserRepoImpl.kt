package com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.repo

import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.AllBackupData
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.HistoryBackup
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.ListBackup
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.ListWordsBackup
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.SavePointBackup
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo.BackupParserRepo
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

class BackupParserRepoImpl: BackupParserRepo {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override fun toJson(backupData: AllBackupData): String? {
        return try {
            json.encodeToString(AllBackupData.serializer(),backupData)
        }catch (e: SerializationException){
            return null
        }
    }

    override fun fromJson(data: String): AllBackupData? {
        return try {
            json.decodeFromString(AllBackupData.serializer(),data)
        }catch (e: IllegalArgumentException){
            return fromJsonLegacy(data)
        }
    }

    //Will be removed for future release
    private fun fromJsonLegacy(data: String): AllBackupData?{
        return try {
            val result = json.decodeFromString<Map<String,String>>(data)

            val historiesString = result["histories"] ?: return null
            val histories = json.decodeFromString<List<HistoryBackup>>(serializer(),historiesString)

            val listsString = result["lists"] ?: return null
            val lists = json.decodeFromString<List<ListBackup>>(serializer(),listsString)

            val listWordsString = result["listWords"] ?: return null
            val listWords = json.decodeFromString<List<ListWordsBackup>>(serializer(),listWordsString)

            val savePointsString = result["savePoints"] ?: return null
            val savePoints = json.decodeFromString<List<SavePointBackup>>(serializer(),savePointsString)

            AllBackupData(
                histories = histories,
                lists = lists,
                listWords = listWords,
                savePoints = savePoints,
                settingsPreferences = null
            )
        }catch (e: Exception){
            null
        }
    }



}