package com.masterplus.trdictionary.core.domain.use_cases.list_words

import com.masterplus.trdictionary.core.domain.model.ListWords
import com.masterplus.trdictionary.core.domain.model.ListView
import com.masterplus.trdictionary.core.domain.repo.ListWordsRepo
import javax.inject.Inject

class AddListWords @Inject constructor(
    private val listWordsRepo: ListWordsRepo,
) {

    suspend operator fun invoke(listView: ListView, wordId: Int){
        val listWords = listWordsRepo.getListWord(wordId,listView.id?:0)
        if(listWords!=null){
            listWordsRepo.deleteListWord(listWords)
        }else{
            listWordsRepo.insertListWord(
                ListWords(
                    listView.id?:0,
                    wordId,
                    pos = listView.contentMaxPos + 1
                )
            )
        }
    }
}