package com.masterplus.trdictionary.features.settings.data.local.repo

import com.google.gson.reflect.TypeToken
import com.masterplus.trdictionary.core.data.local.TransactionProvider
import com.masterplus.trdictionary.core.domain.preferences.AppPreferences
import com.masterplus.trdictionary.core.data.local.services.LocalBackupDao
import com.masterplus.trdictionary.features.settings.data.mapper.*
import com.masterplus.trdictionary.features.settings.domain.JsonParser
import com.masterplus.trdictionary.features.settings.domain.model.*
import com.masterplus.trdictionary.features.settings.domain.repo.LocalBackupRepo
import java.lang.reflect.Type
import javax.inject.Inject

class LocalBackupRepoImpl @Inject constructor(
    private val backupDao: LocalBackupDao,
    private val jsonParser: JsonParser,
    private val appPreferences: AppPreferences,
    private val transactionProvider: TransactionProvider
): LocalBackupRepo {

    override suspend fun getJsonData(): String{
        val histories = backupDao.getHistories().map { it.toHistoryBackup() }
        val lists = backupDao.getLists().map { it.toListBackup() }
        val listWords = backupDao.getListWords().map { it.toListWordsBackup() }
        val savePoints = backupDao.getSavePoints().map { it.toSavePointBackup() }

        val preferencesDict = appPreferences.toDict()

        val resultMap = mapOf(
            "histories" to jsonParser.toJson(histories),
            "lists" to jsonParser.toJson(lists),
            "listWords" to jsonParser.toJson(listWords),
            "savePoints" to jsonParser.toJson(savePoints),
            "appPreferences" to jsonParser.toJson(preferencesDict),
        )
        return jsonParser.toJson(resultMap)
    }


    override suspend fun fromJsonData(data: String, removeAllData: Boolean, addOnLocalData: Boolean){
        val dataMap = jsonParser.fromJson<Map<String,Any>>(data,TypeToken.get(Map::class.java).type)

        val historiesJson = dataMap["histories"]?.toString() ?: return
        val listsJson = dataMap["lists"]?.toString() ?: return
        val listWordsJson = dataMap["listWords"]?.toString()  ?: return
        val savePointsJson = dataMap["savePoints"]?.toString() ?: return
        val appPreferencesJson = dataMap["appPreferences"]?.toString() ?: return

        val histories = jsonParser.fromJson<List<HistoryBackup>>(historiesJson,getListType<HistoryBackup>())
        val lists = jsonParser.fromJson<List<ListBackup>>(listsJson,getListType<ListBackup>())
        val listWords = jsonParser.fromJson<List<ListWordsBackup>>(listWordsJson,getListType<ListWordsBackup>())
        val savePoints = jsonParser.fromJson<List<SavePointBackup>>(savePointsJson,getListType<SavePointBackup>())
        val appPreferencesMap = jsonParser.fromJson<Map<String, Any>>(appPreferencesJson,TypeToken.get(Map::class.java).type)

        transactionProvider.runAsTransaction {
            if(removeAllData){
                backupDao.deleteUserData()
            }
            backupDao.insertHistories(histories.map { it.toHistoryEntity() })

            if(addOnLocalData){
                val clearedListWordsEntity = listWords.map { it.toListWordsEntity() }
                var pos = backupDao.getListMaxPos() + 1
                lists.forEach { list->
                    val newListId = backupDao.insertList(list.copy(id = null, isRemovable = true, pos = pos).toListEntity()).toInt()
                    pos += 1
                    val updatedListContents = clearedListWordsEntity.filter { it.listId == list.id }
                        .map { it.copy(listId = newListId) }
                    backupDao.insertListWords(updatedListContents)
                }
            }else{
                backupDao.insertLists(lists.map { it.toListEntity() })
                backupDao.insertListWords(listWords.map { it.toListWordsEntity() })
            }
            backupDao.insertSavePoints(savePoints.map { it.toSavePointEntity() })
        }
        appPreferences.fromDict(appPreferencesMap)
    }

    override suspend fun deleteAllLocalUserData() {
        backupDao.deleteUserData()
    }

    private inline fun <reified T> getListType(): Type {
        return object: TypeToken<List<T>>(){}.type
    }

}