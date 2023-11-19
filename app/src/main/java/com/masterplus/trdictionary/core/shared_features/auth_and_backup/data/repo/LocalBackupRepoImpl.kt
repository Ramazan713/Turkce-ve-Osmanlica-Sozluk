package com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.repo

import com.google.gson.reflect.TypeToken
import com.masterplus.trdictionary.core.data.local.TransactionProvider
import com.masterplus.trdictionary.core.domain.preferences.AppPreferences
import com.masterplus.trdictionary.core.data.local.services.LocalBackupDao
import com.masterplus.trdictionary.core.domain.JsonParser
import com.masterplus.trdictionary.core.domain.preferences.SettingsPreferences
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.mapper.toBackupData
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.mapper.toData
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.mapper.toHistoryBackup
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.mapper.toHistoryEntity
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.mapper.toListBackup
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.mapper.toListEntity
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.mapper.toListWordsBackup
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.mapper.toListWordsEntity
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.mapper.toSavePointBackup
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.mapper.toSavePointEntity
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.AllBackupData
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.HistoryBackup
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.ListBackup
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.ListWordsBackup
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.SavePointBackup
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo.BackupParserRepo
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo.LocalBackupRepo
import java.lang.reflect.Type
import javax.inject.Inject

class LocalBackupRepoImpl @Inject constructor(
    private val backupDao: LocalBackupDao,
    private val backupParserRepo: BackupParserRepo,
    private val settingsPreferences: SettingsPreferences,
    private val transactionProvider: TransactionProvider
): LocalBackupRepo {

    override suspend fun getJsonData(): String? {
        val histories = backupDao.getHistories().map { it.toHistoryBackup() }
        val lists = backupDao.getLists().map { it.toListBackup() }
        val listWords = backupDao.getListWords().map { it.toListWordsBackup() }
        val savePoints = backupDao.getSavePoints().map { it.toSavePointBackup() }

        val settingsData = settingsPreferences.getData().toBackupData()

        val backupData = AllBackupData(
            histories = histories,
            lists = lists,
            listWords = listWords,
            savePoints = savePoints,
            settingsPreferences = settingsData
        )
        return backupParserRepo.toJson(backupData)
    }


    override suspend fun fromJsonData(data: String, removeAllData: Boolean, addOnLocalData: Boolean){

        val backupData = backupParserRepo.fromJson(data) ?: return

        transactionProvider.runAsTransaction {
            if(removeAllData){
                backupDao.deleteUserData()
            }
            backupDao.insertHistories(backupData.histories.map { it.toHistoryEntity() })

            if(addOnLocalData){
                val clearedListWordsEntity = backupData.listWords.map { it.toListWordsEntity() }
                var pos = backupDao.getListMaxPos() + 1
                backupData.lists.forEach { list->
                    val newListId = backupDao.insertList(list.copy(id = null, isRemovable = true, pos = pos).toListEntity()).toInt()
                    pos += 1
                    val updatedListContents = clearedListWordsEntity.filter { it.listId == list.id }
                        .map { it.copy(listId = newListId) }
                    backupDao.insertListWords(updatedListContents)
                }
            }else{
                backupDao.insertLists(backupData.lists.map { it.toListEntity() })
                backupDao.insertListWords(backupData.listWords.map { it.toListWordsEntity() })
            }
            backupDao.insertSavePoints(backupData.savePoints.map { it.toSavePointEntity() })

            backupData.settingsPreferences?.let { settings->
                settingsPreferences.updateData {
                    settings.toData()
                }
            }
        }
    }

    override suspend fun deleteAllLocalUserData() {
        backupDao.deleteUserData()
    }
}