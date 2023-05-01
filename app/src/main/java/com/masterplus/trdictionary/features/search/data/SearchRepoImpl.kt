package com.masterplus.trdictionary.features.search.data

import com.masterplus.trdictionary.core.data.local.entities.relations.SimpleWordResultRelation
import com.masterplus.trdictionary.core.data.local.mapper.toSimpleResult
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.ProverbIdiomEnum
import com.masterplus.trdictionary.core.domain.enums.WordType
import com.masterplus.trdictionary.core.data.local.services.SearchDao
import com.masterplus.trdictionary.core.domain.constants.KPref
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.core.domain.preferences.AppPreferences
import com.masterplus.trdictionary.features.search.domain.constants.SearchKind
import com.masterplus.trdictionary.features.search.domain.repo.SearchRepo
import javax.inject.Inject

class SearchRepoImpl @Inject constructor(
    private val searchDao: SearchDao,
    private val appPreferences: AppPreferences
): SearchRepo {

    override suspend fun searchSimple(
        query: String,
        categoryEnum: CategoryEnum,
        searchKind: SearchKind,
        searchCount: Int?
    ): List<SimpleWordResult> {

        val searchResultCount = searchCount ?: appPreferences.getEnumItem(KPref.searchResultCountEnum).resultNum

        return search(
            query,categoryEnum, searchKind,searchResultCount
        ).map { it.toSimpleResult() }
    }


    private suspend fun search(
        query: String,
        categoryEnum: CategoryEnum,
        searchKind: SearchKind,
        searchResultCount: Int
    ): List<SimpleWordResultRelation>{

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
                        searchDao.searchWordsWithDictType(ftsQueryForSearch, likeQueryForOrder,queryRaw,WordType.TR.dictId,searchResultCount)
                    }
                    SearchKind.Meaning -> {
                        searchDao.searchWordsFromMeaningWithDictType(likeQueryForSearch, likeQueryForOrder,queryRaw,WordType.TR.dictId,searchResultCount)
                    }
                }
            }
            CategoryEnum.OsmDict -> {
                when(searchKind){
                    SearchKind.Word -> {
                        searchDao.searchWordsWithDictType(ftsQueryForSearch, likeQueryForOrder,queryRaw,WordType.OSM.dictId,searchResultCount)
                    }
                    SearchKind.Meaning -> {
                        searchDao.searchWordsFromMeaningWithDictType(likeQueryForSearch, likeQueryForOrder,queryRaw,WordType.OSM.dictId,searchResultCount)
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