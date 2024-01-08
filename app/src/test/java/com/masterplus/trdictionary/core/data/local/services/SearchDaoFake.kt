package com.masterplus.trdictionary.core.data.local.services

import com.masterplus.trdictionary.core.data.local.entities.relations.WordWithSimilarRelation
import com.masterplus.trdictionary.core.utils.sample_data.wordWithSimilar
import com.masterplus.trdictionary.core.utils.sample_data.wordWithSimilarRelation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.math.min

class SearchDaoFake: SearchDao {

    var initData = listOf<WordWithSimilarRelation>(
        wordWithSimilarRelation()
    )

    var flowData = flow<List<WordWithSimilarRelation>> {
        emit(initData)
    }

    override fun searchWords(
        ftsSearch: String,
        queryOrderForLike: String,
        queryRaw: String,
        limit: Int
    ): Flow<List<WordWithSimilarRelation>> {
        return flowData.map { items->
            items.filter { item->
                item.wordInfo.word.let { word->
                    word.showTTS &&
                            word.word.contains(queryRaw)
                }
            }.let {
                it.subList(0, min(limit, it.size))
            }
        }
    }

    override fun searchWordsWithDictType(
        ftsSearch: String,
        queryOrderForLike: String,
        queryRaw: String,
        dictTypeId: Int,
        limit: Int
    ): Flow<List<WordWithSimilarRelation>> {
        return flowData.map { items->
            items.filter { item->
                item.wordInfo.word.let { word->
                    word.showTTS &&
                            word.word.contains(queryRaw) &&
                            word.dictTypeId == dictTypeId
                }
            }.let {
                it.subList(0, min(limit, it.size))
            }
        }
    }

    override fun searchWordsWithTypeId(
        ftsSearch: String,
        queryOrderForLike: String,
        queryRaw: String,
        wordTypeId: Int,
        limit: Int
    ): Flow<List<WordWithSimilarRelation>> {
        return flowData.map { items->
            items.filter { item->
                item.wordInfo.word.let { word->
                    word.showTTS &&
                            word.word.contains(queryRaw) &&
                            word.wordTypeId == wordTypeId
                }
            }.let {
                it.subList(0, min(limit, it.size))
            }
        }
    }

    override fun searchWordsFromMeaning(
        querySearchFull: String,
        queryOrderForLike: String,
        queryRaw: String,
        limit: Int
    ): Flow<List<WordWithSimilarRelation>> {
        return flowData.map { items->
            items.filter { item->
                item.wordInfo.let { info->
                    info.word.showTTS &&
                            info.meanings.any { it.meaning.meaning.contains(queryRaw) }
                }
            }.let {
                it.subList(0, min(limit, it.size))
            }
        }
    }

    override fun searchWordsFromMeaningWithDictType(
        querySearchFull: String,
        queryOrderForLike: String,
        queryRaw: String,
        dictTypeId: Int,
        limit: Int
    ): Flow<List<WordWithSimilarRelation>> {
        return flowData.map { items->
            items.filter { item->
                item.wordInfo.let { info->
                    info.word.showTTS &&
                            info.meanings.any { it.meaning.meaning.contains(queryRaw) } &&
                            info.word.dictTypeId == dictTypeId
                }
            }.let {
                it.subList(0, min(limit, it.size))
            }
        }
    }

    override fun searchWordsFromMeaningWithTypeId(
        querySearchFull: String,
        queryOrderForLike: String,
        queryRaw: String,
        wordTypeId: Int,
        limit: Int
    ): Flow<List<WordWithSimilarRelation>> {
        return flowData.map { items->
            items.filter { item->
                item.wordInfo.let { info->
                    info.word.showTTS &&
                            info.meanings.any { it.meaning.meaning.contains(queryRaw) } &&
                            info.word.wordTypeId == wordTypeId
                }
            }.let {
                it.subList(0, min(limit, it.size))
            }
        }
    }
}