package com.masterplus.trdictionary.features.search.data

import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.DictType
import com.masterplus.trdictionary.core.domain.enums.WordType
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilar
import com.masterplus.trdictionary.core.utils.sample_data.wordWithSimilar
import com.masterplus.trdictionary.features.search.domain.constants.SearchKind
import com.masterplus.trdictionary.features.search.domain.repo.SearchRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.junit.jupiter.api.Assertions.*

class SearchRepoFake: SearchRepo{

    var data = listOf(
        wordWithSimilar(wordId = 1, wordType = WordType.Proverb),
        wordWithSimilar(wordId = 2, wordType = WordType.Idiom),
        wordWithSimilar(wordId = 3, wordType = WordType.Default),
        wordWithSimilar(wordId = 4, wordType = WordType.Unknown),
        wordWithSimilar(wordId = 5, wordType = WordType.PureWord),
        wordWithSimilar(wordId = 6, dictType = DictType.OSM)
    )

    var flowData = flow {
        emit(data)
    }


    override fun search(
        query: String,
        categoryEnum: CategoryEnum,
        searchKind: SearchKind,
        searchCount: Int?
    ): Flow<List<WordWithSimilar>> {
        return flowData
            .map { items->
                items.filter { item->
                    if(searchKind == SearchKind.Word){
                        item.wordDetail.word.contains(query.lowercase())
                    }else {
                        item.wordDetailMeanings.meanings.any {
                            it.meaning.meaning.contains(query.lowercase())
                        }
                    } &&
                    when(categoryEnum){
                        CategoryEnum.AllDict -> true
                        CategoryEnum.TrDict -> item.wordDetail.dictType == DictType.TR
                        CategoryEnum.OsmDict -> item.wordDetail.dictType == DictType.OSM
                        CategoryEnum.ProverbDict -> item.wordDetail.wordType == WordType.Proverb
                        CategoryEnum.IdiomDict -> item.wordDetail.wordType == WordType.Idiom
                    }
                }.let {items->
                    if(searchCount != null){
                        items.subList(0,searchCount)
                    }
                    items
                }
            }
    }

}