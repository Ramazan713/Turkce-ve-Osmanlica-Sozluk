package com.masterplus.trdictionary.features.word_detail.domain.use_case.word_details_completed

data class WordDetailsCompletedUseCases(
    val getCompletedWordFlow: GetCompletedWordFlow,
    val getCategoryCompletedWordsPaging: GetCategoryCompletedWordsPaging,
    val getListCompletedWordsPaging: GetListCompletedWordsPaging
)
