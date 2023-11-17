package com.masterplus.trdictionary.features.search.data

import com.masterplus.trdictionary.core.data.local.entities.relations.SimpleWordResultRelation
import com.masterplus.trdictionary.core.data.local.entities.relations.WordWithSimilarRelation
import com.masterplus.trdictionary.core.data.local.mapper.toSimpleResult
import com.masterplus.trdictionary.core.data.local.mapper.toWordWithSimilar
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.ProverbIdiomEnum
import com.masterplus.trdictionary.core.domain.enums.DictType
import com.masterplus.trdictionary.core.data.local.services.SearchDao
import com.masterplus.trdictionary.core.domain.constants.KPref
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.core.domain.preferences.AppPreferences
import com.masterplus.trdictionary.core.domain.preferences.SettingsPreferences
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilar
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.use_case.word_details_completed.WordDetailsCompletedUseCases
import com.masterplus.trdictionary.features.search.domain.constants.SearchKind
import com.masterplus.trdictionary.features.search.domain.repo.SearchRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchRepoImpl @Inject constructor(
    private val searchDao: SearchDao,
    private val settingsPreferences: SettingsPreferences,
    private val wordDetailsCompletedUseCases: WordDetailsCompletedUseCases
): SearchRepo {

    override fun search(
        query: String,
        categoryEnum: CategoryEnum,
        searchKind: SearchKind,
        searchCount: Int?
    ): Flow<List<WordWithSimilar>> {
        return flow {
            val settings = settingsPreferences.getData()

            val searchResultCount = searchCount ?: settings.searchResultCount

            val result = search(
                query,categoryEnum, searchKind,searchResultCount
            ).map {items->
                items.map {
                    wordDetailsCompletedUseCases.completedWordInfo(it.toWordWithSimilar())
                }
            }
            emitAll(result)
        }
    }


    private fun search(
        query: String,
        categoryEnum: CategoryEnum,
        searchKind: SearchKind,
        searchResultCount: Int
    ): Flow<List<WordWithSimilarRelation>> {

        val escapedQuery = query.replace(Regex.fromLiteral("\""), "\"\"")

        val likeQueryForSearch = query.let { "%$it%" }.split(" ").joinToString("%")
        val likeQueryForOrder = query.let { "$it%" }.split(" ").joinToString("%")

        val ftsQueryForSearch = escapedQuery.split(" ").joinToString("") { "\"$it*\"" }

        val queryRaw = query.lowercase()

        return when(categoryEnum){
            CategoryEnum.AllDict -> {
                when(searchKind){
                    SearchKind.Word -> {
                        searchDao.searchWords(ftsQueryForSearch, likeQueryForOrder,queryRaw,searchResultCount)
                    }
                    SearchKind.Meaning -> {
                        searchDao.searchWordsFromMeaning(likeQueryForSearch, likeQueryForOrder,queryRaw,searchResultCount)
                    }
                }
            }
            CategoryEnum.TrDict -> {
                when(searchKind){
                    SearchKind.Word -> {
                        searchDao.searchWordsWithDictType(ftsQueryForSearch, likeQueryForOrder,queryRaw,DictType.TR.dictId,searchResultCount)
                    }
                    SearchKind.Meaning -> {
                        searchDao.searchWordsFromMeaningWithDictType(likeQueryForSearch, likeQueryForOrder,queryRaw,DictType.TR.dictId,searchResultCount)
                    }
                }
            }
            CategoryEnum.OsmDict -> {
                when(searchKind){
                    SearchKind.Word -> {
                        searchDao.searchWordsWithDictType(ftsQueryForSearch, likeQueryForOrder,queryRaw,DictType.OSM.dictId,searchResultCount)
                    }
                    SearchKind.Meaning -> {
                        searchDao.searchWordsFromMeaningWithDictType(likeQueryForSearch, likeQueryForOrder,queryRaw,DictType.OSM.dictId,searchResultCount)
                    }
                }
            }
            CategoryEnum.ProverbDict -> {
                when(searchKind){
                    SearchKind.Word -> {
                        searchDao.searchWordsWithTypeId(ftsQueryForSearch, likeQueryForOrder,queryRaw,ProverbIdiomEnum.Proverb.typeId,searchResultCount)
                    }
                    SearchKind.Meaning -> {
                        searchDao.searchWordsFromMeaningWithTypeId(likeQueryForSearch, likeQueryForOrder,queryRaw,ProverbIdiomEnum.Proverb.typeId,searchResultCount)
                    }
                }
            }
            CategoryEnum.IdiomDict -> {
                when(searchKind){
                    SearchKind.Word -> {
                        searchDao.searchWordsWithTypeId(ftsQueryForSearch, likeQueryForOrder,queryRaw,ProverbIdiomEnum.Idiom.typeId,searchResultCount)
                    }
                    SearchKind.Meaning -> {
                        searchDao.searchWordsFromMeaningWithTypeId(likeQueryForSearch, likeQueryForOrder,queryRaw,ProverbIdiomEnum.Idiom.typeId,searchResultCount)
                    }
                }
            }
        }

    }

}