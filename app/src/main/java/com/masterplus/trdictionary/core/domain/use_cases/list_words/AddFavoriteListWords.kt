package com.masterplus.trdictionary.core.domain.use_cases.list_words

import com.masterplus.trdictionary.core.domain.model.ListWords
import com.masterplus.trdictionary.core.domain.model.ListModel
import com.masterplus.trdictionary.core.domain.repo.ListWordsRepo
import com.masterplus.trdictionary.core.domain.repo.ListRepo
import com.masterplus.trdictionary.core.domain.repo.ListViewRepo
import javax.inject.Inject

class AddFavoriteListWords  @Inject constructor(
    private val listWordsRepo: ListWordsRepo,
    private val listViewRepo: ListViewRepo,
    private val listRepo: ListRepo
) {

    suspend operator fun invoke(wordId: Int){
        val favoriteList = listViewRepo.getFavoriteList()
        if(favoriteList==null){
            val listId = listRepo.insertList(
                ListModel(
                    id = null,
                    name = "Favoriler",
                    isRemovable = false,
                    isArchive = false,
                    pos = 1
                )
            )
            listWordsRepo.insertListWord(
                ListWords(listId.toInt(), wordId,1)
            )
        }else{
            listWordsRepo.getListWord(wordId,favoriteList.id?:0)?.let { listWord ->
                listWordsRepo.deleteListWord(listWord)
            }?: kotlin.run {
                listWordsRepo.insertListWord(
                    ListWords(favoriteList.id?:0, wordId,favoriteList.contentMaxPos + 1)
                )
            }
        }
    }
}