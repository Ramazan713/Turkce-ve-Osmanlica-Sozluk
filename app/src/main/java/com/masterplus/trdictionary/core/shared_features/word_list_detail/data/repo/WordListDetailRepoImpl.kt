package com.masterplus.trdictionary.core.shared_features.word_list_detail.data.repo

import androidx.paging.*
import com.masterplus.trdictionary.core.data.local.entities.relations.WordWithSimilarRelation
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.ProverbIdiomEnum
import com.masterplus.trdictionary.core.domain.enums.SubCategoryEnum
import com.masterplus.trdictionary.core.domain.enums.DictType
import com.masterplus.trdictionary.core.data.local.services.WordListDetailDao
import com.masterplus.trdictionary.core.data.local.mapper.toWordWithSimilar
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilarRelationModel
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.repo.WordListDetailRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WordListDetailRepoImpl @Inject constructor(
    private val wordListDao: WordListDetailDao
): WordListDetailRepo {

    override fun getWords(
        categoryEnum: CategoryEnum,
        subCategoryEnum: SubCategoryEnum,
        c: String?,
        pageConfig: PagingConfig
    ): Flow<PagingData<WordWithSimilarRelationModel>> {
        val pager = Pager(
            pageConfig,
            pagingSourceFactory = {getWordPagingSource(categoryEnum, subCategoryEnum, c)}
        )
        return pager.flow.map { pagingData-> pagingData.map { it.toWordWithSimilar() } }
    }

    override fun getWordsByListId(listId: Int, pageConfig: PagingConfig): Flow<PagingData<WordWithSimilarRelationModel>> {
        val pager = Pager(
            pageConfig,
            pagingSourceFactory = {wordListDao.getWordsByListId(listId)}
        )
        return pager.flow.map { pagingData-> pagingData.map { it.toWordWithSimilar() } }
    }

    private fun getWordPagingSource(
        categoryEnum: CategoryEnum,
        subCategoryEnum: SubCategoryEnum,
        c: String?
    ): PagingSource<Int, WordWithSimilarRelation> {
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