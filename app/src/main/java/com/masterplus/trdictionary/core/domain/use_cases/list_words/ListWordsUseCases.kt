package com.masterplus.trdictionary.core.domain.use_cases.list_words

data class ListWordsUseCases(
    val getSelectableLists: GetSelectableLists,
    val addListWords: AddListWords,
    val addFavoriteListWords: AddFavoriteListWords
)
