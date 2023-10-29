package com.masterplus.trdictionary.features.word_detail.data.repo

import androidx.paging.*
import com.masterplus.trdictionary.core.data.local.entities.relations.SimpleWordResultRelation
import com.masterplus.trdictionary.core.data.local.mapper.toSimpleResult
import com.masterplus.trdictionary.core.domain.enums.ProverbIdiomEnum
import com.masterplus.trdictionary.core.domain.enums.DictType
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.core.data.local.services.WordListDao
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.SubCategoryEnum
import com.masterplus.trdictionary.features.word_detail.domain.repo.WordListRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WordListRepoImpl @Inject constructor(
    private val wordListDao: WordListDao
): WordListRepo {

    override fun getWords(
        categoryEnum: CategoryEnum,
        subCategoryEnum: SubCategoryEnum,
        c: String?,
        pageConfig: PagingConfig
    ): Flow<PagingData<SimpleWordResult>> {

        val pager = Pager(
            pageConfig,
            pagingSourceFactory = {getWordPagingSource(categoryEnum, subCategoryEnum, c)}
        )
        return pager.flow.map { pagingData-> pagingData.map { it.toSimpleResult() } }
    }

    override fun getWordsByListId(listId: Int, pageConfig: PagingConfig): Flow<PagingData<SimpleWordResult>>{
        val pager = Pager(
            pageConfig,
            pagingSourceFactory = { wordListDao.getWordsByListId(listId)}
        )
        return pager.flow.map { pagingData-> pagingData.map { it.toSimpleResult() } }
    }

    override suspend fun getAllDictPosByRandomOrder(randomOrder: Int): Int {
        return wordListDao.getAllDictPosByRandomOrder(randomOrder)
    }

    private fun getWordPagingSource(
        categoryEnum: CategoryEnum,
        subCategoryEnum: SubCategoryEnum,
        c: String?
    ): PagingSource<Int, SimpleWordResultRelation> {
        return when(categoryEnum){
            CategoryEnum.AllDict -> {
                when(subCategoryEnum){
                    SubCategoryEnum.All -> {
                        wordListDao.getAllWords()
                    }
                    SubCategoryEnum.Random -> {
                        wordListDao.getAllWordsRandomOrder()
                    }
                    SubCategoryEnum.Alphabetic -> {
                        wordListDao.getAllAlphabeticWords(c?:"a")
                    }
                }
            }
            CategoryEnum.TrDict -> {
                when(subCategoryEnum){
                    SubCategoryEnum.All -> {
                        wordListDao.getWordsWithDictType(DictType.TR.dictId)
                    }
                    SubCategoryEnum.Random -> {
                        wordListDao.getWordsWithDictTypeRandomOrder(DictType.TR.dictId)
                    }
                    SubCategoryEnum.Alphabetic -> {
                        wordListDao.getAlphabeticWordsWithDictType(DictType.TR.dictId,c?:"a")
                    }
                }
            }
            CategoryEnum.OsmDict -> {
                when(subCategoryEnum){
                    SubCategoryEnum.All -> {
                        wordListDao.getWordsWithDictType(DictType.OSM.dictId)
                    }
                    SubCategoryEnum.Random -> {
                        wordListDao.getWordsWithDictTypeRandomOrder(DictType.OSM.dictId)
                    }
                    SubCategoryEnum.Alphabetic -> {
                        wordListDao.getAlphabeticWordsWithDictType(DictType.OSM.dictId,c?:"a")
                    }
                }
            }
            CategoryEnum.ProverbDict -> {
                val typeId = ProverbIdiomEnum.Proverb.typeId
                when(subCategoryEnum){
                    SubCategoryEnum.All -> {
                        wordListDao.getAllProverbIdiomWords(typeId)
                    }
                    SubCategoryEnum.Random -> {
                        wordListDao.getAllProverbIdiomWordsRandomOrder(typeId)
                    }
                    SubCategoryEnum.Alphabetic -> {
                        wordListDao.getAllProverbIdiomAlphabeticWords(typeId,c?:"a")
                    }
                }
            }
            CategoryEnum.IdiomDict -> {
                val typeId = ProverbIdiomEnum.Idiom.typeId
                when(subCategoryEnum){
                    SubCategoryEnum.All -> {
                        wordListDao.getAllProverbIdiomWords(typeId)
                    }
                    SubCategoryEnum.Random -> {
                        wordListDao.getAllProverbIdiomWordsRandomOrder(typeId)
                    }
                    SubCategoryEnum.Alphabetic -> {
                        wordListDao.getAllProverbIdiomAlphabeticWords(typeId,c?:"a")
                    }
                }
            }
        }
    }

}